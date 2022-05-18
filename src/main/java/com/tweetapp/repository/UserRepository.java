package com.tweetapp.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.tweetapp.model.User;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

	@Query("{loginId:?0}")
	Optional<User> getUserByLoginId(String loginId);
	
	@Query("{loginId:?0,password: ?1}")
	Optional<User> checkCredentials(String loginId, String password);
}
