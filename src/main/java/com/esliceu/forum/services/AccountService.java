package com.esliceu.forum.services;

import com.esliceu.forum.dto.RegisterRequest;
import com.esliceu.forum.models.Account;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AccountService extends UserDetailsService{


    UserDetails findByUsername(String username);

    void newAccount(RegisterRequest request);

    UserDetails loadUserByUsername(String username);

    Account getUserByEmail(String email);

    void updateAccount(Account account);

    void updatePassword(String email,String currentPassword,String newPassword);
}
