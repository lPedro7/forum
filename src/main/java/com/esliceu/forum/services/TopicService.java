package com.esliceu.forum.services;

import com.esliceu.forum.models.Topic;

import java.util.List;

public interface TopicService {
    void newTopic(Topic topic);

    List<Topic> findAll();
    List<Topic> getAllByCategoryId(int categoryId);

    Topic getById(int idtopic);
}
