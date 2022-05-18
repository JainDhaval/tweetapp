package com.tweetapp.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.tweetapp.dto.ReplyTweetRequestDTO;
import com.tweetapp.dto.TweetPostRequestDTO;
import com.tweetapp.dto.UpdateTweetRequestDTO;

@Service
public interface TweetService {

	public ResponseEntity<String> postTweets(String username, TweetPostRequestDTO tweetPostRequestDTO);
	public ResponseEntity<?> replyTweet(String username, String tweetId, ReplyTweetRequestDTO replyTweetRequestDTO);
	public ResponseEntity<?> getAlltweets();
	public ResponseEntity<?> getAllTweetsByUsername(String username);
	public ResponseEntity<?> updateTweet(String username,String id,UpdateTweetRequestDTO updateTweetRequestDTO);
	public ResponseEntity<?> deleteTweet(String username,String id);
	public ResponseEntity<?> likeTweet(String username,String id);
}
