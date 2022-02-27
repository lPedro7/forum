package com.esliceu.forum.repos;
import com.esliceu.forum.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepo extends JpaRepository<Account,Integer> {
    Account findByEmail(String email);
}
