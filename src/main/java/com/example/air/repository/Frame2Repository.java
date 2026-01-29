package com.example.air.repository;

import com.example.air.entity.Frame2;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
;@Repository
public interface Frame2Repository extends MongoRepository<Frame2, String> {
    Frame2 findTopByOrderByDateDesc();
}
