package com.esliceu.forum.repos;

import com.esliceu.forum.models.Reply;
import com.esliceu.forum.models.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReplyRepo extends JpaRepository<Reply,Integer> {

    List<Reply> findAllByTopic(Topic topic);

    List<Reply> findAll();

}
