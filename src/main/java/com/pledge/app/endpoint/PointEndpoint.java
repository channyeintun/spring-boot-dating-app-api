package com.pledge.app.endpoint;

import com.pledge.app.entity.User;
import com.pledge.app.exception.CustomErrorException;
import com.pledge.app.payload.Point;
import com.pledge.app.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Optional;

@RestController
@RequestMapping("/points")
public class PointEndpoint {

    @Autowired
    ModelMapper mapper;

    @Autowired
    UserService userService;

    @GetMapping
    public ResponseEntity<?> showPoints(Principal principal){
        Long point=userService.getPointByUsername(principal.getName());
        return ResponseEntity.ok(new Point(point));
    }

    @GetMapping("/add")
    public ResponseEntity<?> addPoints(Principal principal, @RequestParam("point")Long point){
        Optional<User> opt=userService.findByUsername(principal.getName());
        if(opt.isPresent() && point>-1){
            User user=opt.get();
            Long currentPoint=user.getPoint();
            Long afterAdded=currentPoint+point;
            user.setPoint(afterAdded);
            User saved=userService.save(user);
            return ResponseEntity.ok(new Point(saved.getPoint()));
        }
        throw new CustomErrorException(HttpStatus.NOT_FOUND,"User not found");
    }

    @GetMapping("/subtract")
    public ResponseEntity<?> subtractPoint(Principal principal, @RequestParam("point")Long point){
        Optional<User> opt=userService.findByUsername(principal.getName());
        if(opt.isPresent() && point>-1){
            User user=opt.get();
            if(user.getPoint()==0){
                throw new CustomErrorException(HttpStatus.BAD_REQUEST,"User's current point is zero.");
            }
            Long currentPoint=user.getPoint();
            Long afterSubtracted=currentPoint-point;
            if(afterSubtracted<0){
                throw new CustomErrorException(HttpStatus.BAD_REQUEST,"Exceeded user's current point.");
            }
            user.setPoint(afterSubtracted);
            User saved=userService.save(user);
            return ResponseEntity.ok(new Point(saved.getPoint()));
        }
        throw new CustomErrorException(HttpStatus.NOT_FOUND,"User not found");
    }
}
