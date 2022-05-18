package com.tweetapp.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.tweetapp.dto.ReplyTweetRequestDTO;
import com.tweetapp.dto.TweetPostRequestDTO;
import com.tweetapp.dto.TweetsResponseDTO;
import com.tweetapp.dto.UpdateTweetRequestDTO;
import com.tweetapp.model.Reply;
import com.tweetapp.model.Tweet;
import com.tweetapp.repository.ReplyRepository;
import com.tweetapp.repository.TweetRepository;
import com.tweetapp.repository.UserRepository;

@Service
public class TweetServiceImpl implements TweetService {
	
	@Autowired
	TweetRepository tweetRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	ReplyRepository replyRepository;

	@Override
	public ResponseEntity<String> postTweets(String username, TweetPostRequestDTO tweetPostRequestDTO) {
		if(userRepository.getUserByLoginId(username).isPresent()) {
			Tweet tweet = new Tweet();
			tweet.setLoginId(username);
			tweet.setPostDate(tweetPostRequestDTO.getDate());
			tweet.setTweetDescription(tweetPostRequestDTO.getTweetDescription());
			tweet.setTweetTag(tweetPostRequestDTO.getTweetTag());
			tweet.setTweetLikes(tweetPostRequestDTO.getTweetLikes());
			tweet.setReplyId(new ArrayList<>());
			tweet.setLikeId(new ArrayList<>());
			
			if(!tweetRepository.save(tweet).getLoginId().isEmpty()) {
				return new ResponseEntity<String>("Tweet Posted",HttpStatus.CREATED);
			}
			return new ResponseEntity<String>("Something gone wrong. Please try again",HttpStatus.BAD_REQUEST);
		}
		else {
			return new ResponseEntity<String>("User not found", HttpStatus.NOT_FOUND);
		}
	}

	@Override
	public ResponseEntity<?> replyTweet(String username, String tweetId, ReplyTweetRequestDTO replyTweetRequestDTO) {
		
		if(userRepository.getUserByLoginId(username).isPresent()) {
			Reply reply = new Reply();
			reply.setTweetId(tweetId);
			reply.setLoginId(username);
			reply.setReplyDescription(replyTweetRequestDTO.getReplyDescription());
			reply.setDate(replyTweetRequestDTO.getDate());
			
			reply = replyRepository.save(reply);
			if(!reply.getReplyId().isEmpty()) {
				Optional<Tweet> optional = tweetRepository.findById(tweetId);
				if(optional.isPresent()) {
					Tweet tweet = optional.get();
					List<String> replysId = tweet.getReplyId();
					replysId.add(reply.getReplyId());
					tweet.setReplyId(replysId);
					tweet = tweetRepository.save(tweet);
					return new ResponseEntity<Tweet>(tweet, HttpStatus.OK);
				}
				return new ResponseEntity<String>("Could not find the tweet", HttpStatus.NOT_FOUND);
			}
			return new ResponseEntity<String>("Something went wrong", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<String>("User not found", HttpStatus.NOT_FOUND);
	}

	@Override
	public ResponseEntity<?> getAlltweets() {
		TweetsResponseDTO tweetsResponseDTO = new TweetsResponseDTO();
		tweetsResponseDTO.setTweets(tweetRepository.findAll());
		if(tweetsResponseDTO.getTweets().size() > 0) {
			return new ResponseEntity<TweetsResponseDTO>(tweetsResponseDTO,HttpStatus.OK);
		}
		return new ResponseEntity<String>("Something went wrong", HttpStatus.BAD_REQUEST);
	}

	@Override
	public ResponseEntity<?> getAllTweetsByUsername(String username) {
		TweetsResponseDTO tweetsResponseDTO = new TweetsResponseDTO();
		tweetsResponseDTO.setTweets(tweetRepository.getAllTweetsByUsername(username).get());
		if(tweetsResponseDTO.getTweets().size() > 0) {
			return new ResponseEntity<TweetsResponseDTO>(tweetsResponseDTO,HttpStatus.OK);
		}
		else if(tweetsResponseDTO.getTweets().size() == 0) {
			return new ResponseEntity<String>("No Tweet from user", HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<String>("Something went wrong", HttpStatus.BAD_REQUEST);
	}

	@Override
	public ResponseEntity<?> updateTweet(String username, String id, UpdateTweetRequestDTO updateTweetRequestDTO) {
		Tweet tweet = tweetRepository.findById(id).get();
		tweet.setTweetDescription(updateTweetRequestDTO.getTweetDescriptions());
		tweet = tweetRepository.save(tweet);
		return new ResponseEntity<Tweet>(tweet,HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> deleteTweet(String username, String id) {
		Optional<Tweet> optional = tweetRepository.findById(id);
		if(optional.isPresent()) {
			Tweet tweet = optional.get();
			if(tweet.getLoginId().equalsIgnoreCase(username)) {
				tweetRepository.deleteById(id);
				if(tweet.getReplyId().size() > 0) {
					replyRepository.deleteAllById(tweet.getReplyId());
				}
				return new ResponseEntity<String>("Tweet Deleted", HttpStatus.OK);
			}
			return new ResponseEntity<String>("You are not authorized to delete the Tweet", HttpStatus.FORBIDDEN);
		}
		return new ResponseEntity<String>("Could not find the tweet", HttpStatus.NOT_FOUND);
	}

	@Override
	public ResponseEntity<?> likeTweet(String username, String id) {
		if(userRepository.getUserByLoginId(username).isPresent()) {
			Optional<Tweet> optional = tweetRepository.findById(id);
			if(optional.isPresent()) {
				Tweet tweet = optional.get();
				int index = tweet.getLikeId().indexOf(username);
				if(index == -1) {
					int likes = tweet.getTweetLikes() + 1;
					tweet.setTweetLikes(likes);
					List<String> likesId = tweet.getLikeId();
					likesId.add(username);
					tweet.setLikeId(likesId);
					tweet = tweetRepository.save(tweet);
					return new ResponseEntity<Tweet>(tweet,HttpStatus.OK);
				}
				else {
					int likes = tweet.getTweetLikes() - 1;
					tweet.setTweetLikes(likes);
					List<String> likesId = tweet.getLikeId();
					likesId.remove(index);
					tweet.setLikeId(likesId);
					tweet = tweetRepository.save(tweet);
					return new ResponseEntity<Tweet>(tweet,HttpStatus.OK);
				}
			}
			return new ResponseEntity<String>("Could not find the tweet", HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<String>("User not found", HttpStatus.NOT_FOUND);
	}

}
