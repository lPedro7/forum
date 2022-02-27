package com.esliceu.forum.repos;
import com.esliceu.forum.models.Reply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ReplyRepo extends JpaRepository<Reply,Integer> {
}
