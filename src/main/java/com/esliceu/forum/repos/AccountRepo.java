package com.esliceu.forum.repos;

import com.esliceu.forum.models.Account;
import net.bytebuddy.TypeCache;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepo extends JpaRepository<Account,Integer> {


    Account findByEmail(String email);
}
