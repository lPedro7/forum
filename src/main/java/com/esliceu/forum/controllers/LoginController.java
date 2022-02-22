package com.esliceu.forum.controllers;

import com.esliceu.forum.dto.LoginRequest;
import com.esliceu.forum.dto.RegisterRequest;
import com.esliceu.forum.models.Account;
import com.esliceu.forum.services.AccountServiceImpl;
import com.esliceu.forum.utils.JwtTokenUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.nimbusds.jose.shaded.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.*;

@RestController
public class LoginController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @Autowired
    AccountServiceImpl accountService;

    //Pdte mensaje error
    @PostMapping("/login")
    public Map<String,Object> getLogin(@RequestBody LoginRequest request){
            User user = authenticate(request);
            String token = jwtTokenUtil.generateAccessToken(user);
            Account account = accountService.getUserByEmail(request.getEmail());
            Map<String,Object> map = new HashMap<>();
            map.put("user",account.toJsonMap());
            map.put("token",token);
            return map;
    }

    private User authenticate(LoginRequest request) {

        Authentication autenticate = authenticationManager
                .authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.getEmail(), request.getPassword()
                        )
                );

        return (User) autenticate.getPrincipal();
    }

    @PostMapping("/register")
    public String getRegister(@RequestBody RegisterRequest request) {
        accountService.newAccount(request);
        return "ok";
    }

    @PreAuthorize("hasAnyRole('Moderator','Admin')")
    @GetMapping("/getprofile")
    public Map<String,Object> getProfile(@RequestHeader("Authorization") String auth){

        auth = auth.replace("Bearer ", "");

        String username = jwtTokenUtil.getUsername(auth);
        Account account = accountService.getUserByEmail(username);

        return account.toJsonMap();
    }

    @PreAuthorize("hasAnyRole('Moderator','Admin')")
    @PutMapping("/profile")
    public Map<String, Object> updateProfile(@RequestBody String data, @RequestHeader("Authorization") String auth) throws UnsupportedEncodingException {

        auth = auth.replace("Bearer ", "");

        String accountEmail = jwtTokenUtil.getUsername(auth);

        Account newAccount = new Gson().fromJson(data, Account.class);

        Account account = accountService.getUserByEmail(accountEmail);
        account.setName(newAccount.getName());
        account.setEmail(newAccount.getEmail());
        String avatar = newAccount.getAvatar();
        account.setAvatar(avatar);
        accountService.updateAccount(account);

        Map<String,Object> response = new HashMap<>();
        response.put("user",account.toJsonMap());
        response.put("token",auth);

        return response;
    }

    @PreAuthorize("hasAnyRole('Moderator','Admin')")
    @PutMapping("/profile/password")
    public Map<String, Object> updatePassword(@RequestBody Map<String,Object> mapData, @RequestHeader("Authorization") String auth){

        String token = auth.replace("Bearer ", "");
        String userEmail = jwtTokenUtil.getUsername(token);

        String newPassword = (String) mapData.get("newPassword");
        String currentPassword = (String) mapData.get("currentPassword");

        accountService.updatePassword(userEmail, currentPassword, newPassword);

        Account account = accountService.getUserByEmail(userEmail);

        return account.toJsonMap();
    }
}
