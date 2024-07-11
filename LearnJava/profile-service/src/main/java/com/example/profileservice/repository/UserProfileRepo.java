package com.example.profileservice.repository;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserProfileRepo extends Neo4jRepository<UserProfileRepo, String> {
}
