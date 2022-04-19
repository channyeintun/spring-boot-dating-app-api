package com.pledge.app.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailValidator {
    private String email;
    public EmailValidator(String email){
        this.email=email;
    }
    public boolean isValid(){
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$");
        Matcher matcher = pattern.matcher(this.email);
        return matcher.matches();
    }
}
