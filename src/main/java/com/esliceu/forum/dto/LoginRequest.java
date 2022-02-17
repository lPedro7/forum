package com.esliceu.forum.dto;

import com.sun.istack.NotNull;
import lombok.Data;

@Data
public class LoginRequest {
    @NotNull
    private String email;

    @NotNull
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
