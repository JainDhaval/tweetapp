package com.tweetapp.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.tweetapp.model.Reply;

@Repository
public interface ReplyRepository extends MongoRepository<Reply, String> {
	
	@Query("{id:{ $in: ?0 }}")
	public List<Reply> getAllReplyForPost(List<String> id);

}
