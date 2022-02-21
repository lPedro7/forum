package com.esliceu.forum.services;

import com.esliceu.forum.models.Topic;
import com.esliceu.forum.repos.TopicRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TopicServiceImpl implements TopicService{

    @Autowired
    TopicRepo topicRepo;

    @Override
    public void newTopic(Topic topic) {
        topicRepo.save(topic);
    }

    @Override
    public List<Topic> findAll() {

        List<Topic> topics = topicRepo.findAll();

        return topics;
    }

    public List<Topic> getAllByCategoryId(int categoryId){
        return topicRepo.findAllByCategoryId(categoryId);
    }

    @Override
    public Topic getById(int idtopic) {
        return topicRepo.getById(idtopic);
    }

}
