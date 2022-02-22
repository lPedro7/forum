package com.esliceu.forum.services;

import com.esliceu.forum.models.Account;
import com.esliceu.forum.models.Reply;
import com.esliceu.forum.models.Topic;
import com.esliceu.forum.repos.ReplyRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReplyServiceImpl implements ReplyService{

    @Autowired
    ReplyRepo replyRepo;

    @Autowired
    TopicServiceImpl topicService;

    @Override
    public void newReply(Reply reply) {
        replyRepo.save(reply);
    }

    @Override
    public Reply getById(int idreply) {
        return replyRepo.getById(idreply);
    }

    @Override
    public void deleteReply(Reply reply) {
        replyRepo.delete(reply);
    }
}
