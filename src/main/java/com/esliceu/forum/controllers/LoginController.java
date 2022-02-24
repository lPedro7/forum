package com.esliceu.forum.controllers;

import com.esliceu.forum.dto.LoginRequest;
import com.esliceu.forum.dto.RegisterRequest;
import com.esliceu.forum.models.Account;
import com.esliceu.forum.services.AccountServiceImpl;
import com.esliceu.forum.utils.JwtTokenUtil;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
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

    @PostMapping("/login")
    public Map<String, Object> getLogin(@RequestBody LoginRequest request) {
        User user = authenticate(request);
        String token = jwtTokenUtil.generateAccessToken(user);
        Account account = accountService.getUserByEmail(request.getEmail());
        Map<String, Object> map = new HashMap<>();
        map.put("user", account.toJsonMap());
        map.put("token", token);
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
    public Map<String, Object> getProfile(@RequestHeader("Authorization") String auth) {

        auth = auth.replace("Bearer ", "");

        String username = jwtTokenUtil.getUsername(auth);
        Account account = accountService.getUserByEmail(username);

        return account.toJsonMap();
    }

    @PreAuthorize("hasAnyRole('Moderator','Admin')")
    @PutMapping("/profile")
    public Map<String, Object> updateProfile(@RequestBody Map<String, Object> data, @RequestHeader("Authorization") String auth) throws UnsupportedEncodingException {

        auth = auth.replace("Bearer ", "");

        String accountEmail = jwtTokenUtil.getUsername(auth);

        Account account = accountService.getUserByEmail(accountEmail);
        account.setName((String) data.get("name"));
        account.setEmail((String) data.get("email"));
        byte[] avatar = ((String) data.get("avatar")).getBytes();

        account.setAvatar(avatar);
        accountService.updateAccount(account);

        Map<String, Object> response = new HashMap<>();
        response.put("user", account.toJsonMap());
        response.put("token", auth);

        return response;
    }

    @PreAuthorize("hasAnyRole('Moderator','Admin')")
    @PutMapping("/profile/password")
    public Map<String, Object> updatePassword(@RequestBody Map<String, Object> mapData, @RequestHeader("Authorization") String auth) {

        String token = auth.replace("Bearer ", "");
        String userEmail = jwtTokenUtil.getUsername(token);

        String newPassword = (String) mapData.get("newPassword");
        String currentPassword = (String) mapData.get("currentPassword");

        accountService.updatePassword(userEmail, currentPassword, newPassword);

        Account account = accountService.getUserByEmail(userEmail);

        return account.toJsonMap();
    }


    @ExceptionHandler
    public ResponseEntity<Map<String, String>> exceptionHandler(Exception exception) {
        Map<String, String> map = new HashMap();
        if (exception instanceof HibernateException) {
            map.put("message", "Aquest usuari ja existeix");
        } else if (exception instanceof AuthenticationException) {
            map.put("message", "Usuari o contrassenya incorrecta");
        } else if (exception instanceof DataIntegrityViolationException) {
            map.put("message", "Aquest usuari ja existeix");
        }else {
            System.out.println(exception);
            map.put("message", exception.getMessage());
        }

        return ResponseEntity.status(401).body(map);
    }

}
