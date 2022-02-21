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
    public String getLogin(@RequestBody LoginRequest request) throws JsonProcessingException {
            User user = authenticate(request);
            String token = jwtTokenUtil.generateAccessToken(user);
            Account account = accountService.getUserByEmail(request.getEmail());

            JSONObject jsonObject = new JSONObject();

            Map<String,Object> map = new HashMap<>();
            map.put("user",account.toJsonMap());
            map.put("token",token);

            jsonObject.appendField("user", account.toJsonMap());
            jsonObject.appendField("token", token);

            return String.valueOf(jsonObject);
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
    public ResponseEntity<String> getRegister(@RequestBody RegisterRequest request) {

        try {
            accountService.newAccount(request);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
        return ResponseEntity.ok().body("ok");
    }


    @GetMapping("/getprofile")
    public Map getProfile(@RequestHeader("Authorization") String auth) throws JsonProcessingException {

        auth = auth.replace("Bearer ", "");

        String username = jwtTokenUtil.getUsername(auth);
        Account account = accountService.getUserByEmail(username);

        System.out.println("Auth = " + auth);
        System.out.println("email = " + username);
        System.out.println("Account = " + account.getName());

        return account.toJsonMap();
    }


    @PutMapping("/profile")
    public ResponseEntity<String> updateProfile(@RequestBody String data, @RequestHeader("Authorization") String auht) throws UnsupportedEncodingException {

        auht = auht.replace("Bearer ", "");

        String accountEmail = jwtTokenUtil.getUsername(auht);

        Account newAccount = new Gson().fromJson(data, Account.class);

        Account account = accountService.getUserByEmail(accountEmail);
        account.setName(newAccount.getName());
        account.setEmail(newAccount.getEmail());

        String avatar = newAccount.getAvatar();

        account.setAvatar(avatar);

        accountService.updateAccount(account);

        JSONObject jsonObject = new JSONObject();

        jsonObject.appendField("user", account);
        jsonObject.appendField("token", auht);

        return ResponseEntity.ok().body(jsonObject.toJSONString());
    }

    @PutMapping("/profile/password")
    public ResponseEntity<String> updatePassword(@RequestBody String data, @RequestHeader("Authorization") String auth) throws RuntimeException {

        String token = auth.replace("Bearer ", "");
        String userEmail = jwtTokenUtil.getUsername(token);

        Map<String, String> mapData = new Gson().fromJson(data, Map.class);
        String newPassword = mapData.get("newPassword");
        String currentPassword = mapData.get("currentPassword");

        accountService.updatePassword(userEmail, currentPassword, newPassword);


        Account account = accountService.getUserByEmail(userEmail);

        JSONObject jsonObject = new JSONObject();
        jsonObject.appendField("user", account);

        return ResponseEntity.ok().body(jsonObject.toJSONString());
    }
}
