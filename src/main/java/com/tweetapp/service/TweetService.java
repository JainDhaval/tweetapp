package com.tweetapp.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.tweetapp.dto.DeleteTweetResponseDTO;
import com.tweetapp.dto.ReplyTweetRequestDTO;
import com.tweetapp.dto.TweetPostRequestDTO;
import com.tweetapp.dto.TweetPostResponseDTO;
import com.tweetapp.dto.UpdateTweetRequestDTO;
import com.tweetapp.exception.TweetNotFoundException;
import com.tweetapp.exception.UserNotFoundException;
import com.tweetapp.model.Tweet;

@Service
public interface TweetService {

	public ResponseEntity<TweetPostResponseDTO> postTweets(String username, TweetPostRequestDTO tweetPostRequestDTO) throws UserNotFoundException;
	public ResponseEntity<?> replyTweet(String username, String tweetId, ReplyTweetRequestDTO replyTweetRequestDTO);
	public ResponseEntity<?> getAlltweets() throws TweetNotFoundException;
	public ResponseEntity<?> getAllTweetsByUsername(String username) throws TweetNotFoundException;
	public ResponseEntity<Tweet> updateTweet(String username,String id,UpdateTweetRequestDTO updateTweetRequestDTO) throws TweetNotFoundException;
	public ResponseEntity<DeleteTweetResponseDTO> deleteTweet(String username,String id) throws TweetNotFoundException;
	public ResponseEntity<?> likeTweet(String username,String id);
}
