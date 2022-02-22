package com.esliceu.forum.services;

import com.esliceu.forum.configuration.Config;
import com.esliceu.forum.dto.RegisterRequest;
import com.esliceu.forum.models.Account;
import com.esliceu.forum.repos.AccountRepo;
import com.esliceu.forum.utils.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    AccountRepo accountRepo;

    public UserDetails findByUsername(String email) {


        try {
            Account account = accountRepo.findByEmail(email);
            List<GrantedAuthority> roles = new ArrayList<>();
            roles.add(new SimpleGrantedAuthority(account.getRole()));
            return new User(account.getEmail(), Config.passwordEncoder().encode(account.getPassword()), roles);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public void newAccount(RegisterRequest request) {
            Account account = new Account();
            account.setName(request.getName());
            account.setPassword(Config.passwordEncoder().encode(request.getPassword()));
            account.setEmail(request.getEmail());
            account.setRole(request.getRole());
            account.setAvatar(new byte[]{});
            accountRepo.save(account);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepo.findByEmail(username);
        return new User(account.getEmail(),account.getPassword(),new ArrayList<>());
    }

    public Account getUserByEmail(String email) {
        return accountRepo.findByEmail(email);
    }

    public void updateAccount(Account account) {
        accountRepo.save(account);
    }

    public void updatePassword(String email,String currentPassword,String newPassword) {

        Account account = accountRepo.findByEmail(email);
        newPassword = Config.passwordEncoder().encode(newPassword);

        if (Config.passwordEncoder().matches(currentPassword,account.getPassword())){
            account.setPassword(newPassword);
            accountRepo.save(account);
        }else{
            throw new RuntimeException();
        }

    }


}
