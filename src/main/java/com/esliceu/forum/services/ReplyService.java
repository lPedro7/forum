package com.esliceu.forum.services;

import com.esliceu.forum.models.Account;
import com.esliceu.forum.models.Reply;
import com.esliceu.forum.models.Topic;

import java.util.List;

public interface ReplyService {
    void newReply(Reply reply);

    Reply getById(int idreply);

    void deleteReply(Reply reply);
}
