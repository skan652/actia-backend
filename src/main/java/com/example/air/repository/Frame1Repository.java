package com.example.air.repository;

import com.example.air.entity.Frame1;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Frame1Repository extends MongoRepository<Frame1, String> {

    Frame1 findTopByOrderByDateDesc();
}
