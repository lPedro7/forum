package com.esliceu.forum.controllers;

import com.esliceu.forum.dto.AuthRequest;
import com.esliceu.forum.utils.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class LoginController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @PostMapping("/login")
    public ResponseEntity<String> getLogin(@RequestBody AuthRequest request){
        try{
            User user = authenticate(request);
            return ResponseEntity.ok()
                    .header(
                            HttpHeaders.AUTHORIZATION,
                            jwtTokenUtil.generateAccessToken(user)
                    )
                    .body("OK");
        }catch (BadCredentialsException ex){
            return new ResponseEntity<>("Unauth",HttpStatus.UNAUTHORIZED);
            //throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }

    private User authenticate(AuthRequest request) {
        Authentication autenticate = authenticationManager
                .authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.getEmail(),request.getPassword()
                        )
                );
        return (User) autenticate.getPrincipal();
    }

}
