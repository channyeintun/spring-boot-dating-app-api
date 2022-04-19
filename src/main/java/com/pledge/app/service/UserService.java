package com.pledge.app.service;

import com.pledge.app.config.jwt.JwtTokenProvider;
import com.pledge.app.dao.RoleDAO;
import com.pledge.app.dao.UserDAO;
import com.pledge.app.entity.OTP;
import com.pledge.app.entity.Role;
import com.pledge.app.entity.RoleName;
import com.pledge.app.entity.User;
import com.pledge.app.exception.CustomErrorException;
import com.pledge.app.util.Mail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mail.MailException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService implements UserDetailsService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private RoleDAO roleDAO;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private OTPService otpService;

    @Autowired
    private EmailService emailService;

    public Map<String, Object> loginUser(String username, String password) {
        authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(username, password));
        Optional<User> opt = userDAO.findByUsername(username);
        log.info("user find query executed");
        if (opt.isEmpty()) {
            log.warn("User not found");
            throw new UsernameNotFoundException("User not found in the database");
        } else {
            User user = opt.get();
            log.info("user found");
            if (validateUser(user)) {
                return tokenProvider.createToken(username, user.getRoles());
            } else {
                throw new CustomErrorException(HttpStatus.UNAUTHORIZED, getErrorMessage(user));
            }
        }
    }

    public boolean validateUser(User user) {
        return user.isVerified() && !user.isDeactivated() && !user.isLocked();
    }

    public String getErrorMessage(User user) {
        if (!user.isVerified()) {
            return "Check verification email.";
        }
        if (user.isDeactivated()) {
            return "This account has been deactivated.";
        }
        if (user.isLocked()) {
            return "This account has been locked.";
        }
        return "This account has problem with logging in.";
    }

    public User registerUser(User user) {
        log.info("registering user {}", user.getUsername());
        Optional<User> opt = userDAO.findByUsername(user.getUsername());
        if (opt.isPresent()) {
            User searchedUser = opt.get();
            if (searchedUser.isVerified()) {
                log.info("username {} already exists.", user.getUsername());
                throw new CustomErrorException(HttpStatus.BAD_REQUEST,
                        String.format("username %s already exists", user.getUsername()));
            } else {
                log.info("User is going to be saved");
                searchedUser.setName(user.getName());
                searchedUser.setPassword(passwordEncoder.encode(user.getPassword()));
                sendMail(searchedUser); // send otp to verify account
                return userDAO.save(searchedUser);
            }
        }
        log.info("User is going to be saved");
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(new ArrayList<>() {{
            Role role = roleDAO.findByName(RoleName.ROLE_USER);
            add(role);
        }});
        User saved = userDAO.save(user);
        log.info("user is saved");
        sendMail(user); // send otp to verify account
        log.info("Confirmation code is sent.");
        return saved;
    }

    public User save(User user) {
        log.info("User is saved {}", user.getUsername());
        return userDAO.save(user);
    }

    public Map<String, Object> refreshToken(HttpServletRequest httpServletRequest) {
        try {
            String refreshToken = tokenProvider.resolveToken(httpServletRequest);
            if (refreshToken != null && tokenProvider.validateToken(refreshToken)) {
                UsernamePasswordAuthenticationToken authenticationToken = tokenProvider.getAuthenticationToken(refreshToken);
                List<Role> roles = authenticationToken.getAuthorities()
                        .stream()
                        .map(r -> r.getAuthority().equals("ROLE_USER")
                                ? new Role(RoleName.ROLE_USER)
                                : new Role(RoleName.ROLE_ADMIN))
                        .collect(Collectors.toList());
                return tokenProvider.createToken(tokenProvider
                        .getUsername(refreshToken), roles);
            }
        } catch (Exception ex) {
            SecurityContextHolder.clearContext();
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid Token", null);
        }
        return null;
    }

    public Optional<User> findByUsername(String username) {
        log.info("retrieving user {}", username);
        return userDAO.findByUsername(username);
    }

    public Optional<User> findById(Long id) {
        log.info("retrieving user {}", id);
        return userDAO.findById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> opt = userDAO.findByUsername(username);
        if (opt.isEmpty()) {
            log.error("User not found in the database");
            throw new UsernameNotFoundException("User not found in the database");
        } else {
            log.info("User found in the database: {}", opt.get().getUsername());
            final User user = opt.get();
            return org.springframework.security.core.userdetails.User//
                    .withUsername(username)//
                    .password(user.getPassword())//
                    .authorities(user.getRoles())//
                    .accountExpired(false)//
                    .accountLocked(false)//
                    .credentialsExpired(false)//
                    .disabled(false)//
                    .build();
        }
    }

    public void saveRole(Role role) {
        Boolean isExist = roleDAO.existsByName(role.getName());
        if (!isExist) {
            roleDAO.save(role);
        }
    }

    public void sendMail(User user) {
        String token = otpService.generateToken(6);
        OTP savedToken = otpService.createOTPToken(token, user);
        String emailText = getEmailTemplate(user, savedToken);
        Mail mail = new Mail("Pledge<noreply@foeim.org>",
                user.getUsername(), "Pledge Verification Code",
                emailText);
        try {
            emailService.sendMail(mail);
        } catch (MailException me) {
            log.info(me.getMessage());
        } catch (MessagingException me) {
            log.info(me.getMessage());
        }
    }

    public void updateFcmToken(String token, String username) {
        try {
            userDAO.updateFcmToken(token, username);
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to update FCM Token.");
        }
    }

    public String getFcmTokenByUserId(Long userId) {
        return userDAO.getFcmTokenByUserId(userId);
    }

    public Long getPointByUsername(String username) {
        return userDAO.getPointByUsername(username);
    }

    public int countAll() {
        return userDAO.countAll();
    }

    private String getEmailTemplate(User user, OTP savedToken) {
        return "<html>" +
                "<head> " +
                "      <style> " +
                "            @import url('https://fonts.googleapis.com/css2?family=Lato:ital,wght@0,400;0,700;0,900;1,400;1,700&family=Montserrat:wght@700&display=swap'); " +
                "            * { " +
                "                  margin: 0; " +
                "                  padding: 0; " +
                "                  box-sizing: border-box; " +
                "                  font-family: 'Lato', sans-serif; " +
                "                  font-family: 'Montserrat', sans-serif; " +
                "            } " +
                "            .container{ " +
                "                  width: 100%; " +
                "                  text-align: center; " +
                "                  border-spacing: 20px; " +
                "                  background: #F9F9F9;" +
                "            } " +
                "            h1 { " +
                "                  color: #E24E59; " +
                "                  font-size: 38px; " +
                "            } " +
                "            .textimonial { " +
                "                  width: 80%; " +
                "                  background: white; " +
                "                  padding:30px 20px; " +
                "                  text-align:left;" +
                "                  margin:auto;" +
                "            } " +
                "            h2{ " +
                "                  font-size: 20px; " +
                "                  font-weight: 500; " +
                "                  color:#4F545C; " +
                "                  letter-spacing: 0.27px; " +
                "            } " +
                "            .textimonial p{ " +
                "                  padding: 20px 0; " +
                "                  font-size: 16px; " +
                "                  line-height: 24px; " +
                "                  text-align: left; " +
                "                  color:#737F8D; " +
                "            } " +
                "            h3{ " +
                "                  text-align:center;" +
                "                  width:100%;" +
                "                  font-size: 15px; " +
                "                  letter-spacing: 0.05em; " +
                "                  color:#4F545C; " +
                "                  padding:20px 30px; " +
                "            } " +
                "            .footer>p{ " +
                "                  font-size: 12px; " +
                "                  line-height: 24px; " +
                "                  color:#99AAB5; " +
                "            } " +
                "      </style> " +
                "</head> " +
                "<body> " +
                "      <table class=\"container\"> " +
                "            <tr> " +
                "                  <td> " +
                "                        <h1>Pledge</h1>  " +
                "                  </td> " +
                "            </tr> " +
                "            <tr> " +
                "                  <td> " +
                "                        <table class=\"textimonial\"> " +
                "                              <tr> " +
                "                                    <td> " +
                "                                          <h2>Hey " + user.getName() + ",</h2>  " +
                "                                    </td> " +
                "                              </tr> " +
                "                              <tr> " +
                "                                    <td> " +
                "                                          <p>You registered in Pledge. Your confirmation code is below. If you did not register,  " +
                "                                                please ignore this email.</p> " +
                "                                    </td> " +
                "                              </tr> " +
                "                              <tr> " +
                "                                    <td> " +
                "                                                <h3> " + savedToken.getToken() +
                "                                                </h3> " +
                "                                    </td> " +
                "                              </tr> " +
                "                        </table> " +
                "                  </td> " +
                "            </tr> " +
                "            <tr> " +
                "                  <td class=\"footer\"> " +
                "                        <p>Sent by Pledge</p> " +
                "                  </td> " +
                "            </tr> " +
                "      </table> " +
                "</body> " +
                "</html>";
    }
}
