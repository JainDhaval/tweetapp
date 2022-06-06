package com.tweetapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tweetapp.dto.DeleteTweetResponseDTO;
import com.tweetapp.dto.ReplyTweetRequestDTO;
import com.tweetapp.dto.TweetPostRequestDTO;
import com.tweetapp.dto.TweetPostResponseDTO;
import com.tweetapp.dto.UpdateTweetRequestDTO;
import com.tweetapp.exception.TweetNotFoundException;
import com.tweetapp.exception.UserNotFoundException;
import com.tweetapp.model.Tweet;
import com.tweetapp.service.TweetService;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping(value = "/api/v1.0/tweets")
@Slf4j
@CrossOrigin(origins = "*")
public class TweetController {
	
	@Autowired
	TweetService tweetService;

	@PostMapping("/{username}/add")
	private ResponseEntity<TweetPostResponseDTO> postTweet(@PathVariable String username, @RequestBody TweetPostRequestDTO tweetPostRequestDTO) throws UserNotFoundException{
		log.info("Inside Tweet Controller || postTweet Method || Start");
		log.info("Inside Tweet Controller || postTweet Method || End");
		return tweetService.postTweets(username, tweetPostRequestDTO);
	}
	
	@PostMapping("/{username}/reply/{id}")
	private ResponseEntity<?> replyTweet(@PathVariable String username,@PathVariable String id,@RequestBody ReplyTweetRequestDTO replyTweetRequestDTO){
		log.info("Inside Tweet Controller || replyTweet Method || Start");
		log.info("Inside Tweet Controller || replyTweet Method || End");
		return tweetService.replyTweet(username, id, replyTweetRequestDTO);
	}
	
	@GetMapping("/all")
	private ResponseEntity<?> getAllTweets() throws TweetNotFoundException{
		log.info("Inside Tweet Controller || getAllTweets Method || Start");
		log.info("Inside Tweet Controller || getAllTweets Method || End");
		return tweetService.getAlltweets();
	}
	
	@GetMapping("/{username}")
	private ResponseEntity<?> getAllTweetsByUsername(@PathVariable String username) throws TweetNotFoundException{
		log.info("Inside Tweet Controller || getAllTweetsByUsername Method || Start");
		log.info("Inside Tweet Controller || getAllTweetsByUsername Method || End");
		return tweetService.getAllTweetsByUsername(username);
	}
	
	@PutMapping("/{username}/update/{id}")
	private ResponseEntity<Tweet> updateTweet(@PathVariable String username, @PathVariable String id, @RequestBody UpdateTweetRequestDTO updateTweetRequestDTO) throws TweetNotFoundException{
		log.info("Inside Tweet Controller || updateTweet Method || Start");
		log.info("Inside Tweet Controller || updateTweet Method || ",updateTweetRequestDTO);
		log.info("Inside Tweet Controller || updateTweet Method || End");
		return tweetService.updateTweet(username, id, updateTweetRequestDTO);
	}
	
	@DeleteMapping("/{username}/delete/{id}")
	private ResponseEntity<DeleteTweetResponseDTO> deleteTweet(@PathVariable String username, @PathVariable String id) throws TweetNotFoundException{
		log.info("Inside Tweet Controller || deleteTweet Method || Start");
		log.info("Inside Tweet Controller || deleteTweet Method || End");
		return tweetService.deleteTweet(username, id);
	}
	
	@PutMapping("/{username}/like/{id}")
	private ResponseEntity<?> likeTweet(@PathVariable String username, @PathVariable String id){
		log.info("Inside Tweet Controller || likeTweet Method || Start");
		log.info("Inside Tweet Controller || likeTweet Method || End");
		return tweetService.likeTweet(username, id);
	}
}
