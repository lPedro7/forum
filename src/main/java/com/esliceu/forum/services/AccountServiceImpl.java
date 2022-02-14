package com.esliceu.forum.services;

import com.esliceu.forum.models.Account;
import com.esliceu.forum.repos.AccountRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    AccountRepo accountRepo;

    @Autowired
    PasswordEncoder passwordEncoder;

    public UserDetails findByUsername(String email) {

        try {
            Account account = accountRepo.findByEmail(email);
            return new User(account.getEmail(), passwordEncoder.encode(account.getPassword()), new ArrayList<>());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }

    }


}
