package com.esliceu.forum.services;

import org.springframework.security.core.userdetails.UserDetails;

public interface AccountService {


    UserDetails findByUsername(String username);


}
