package com.tweetapp.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.tweetapp.dto.DeleteTweetResponseDTO;
import com.tweetapp.dto.ReplyTweetRequestDTO;
import com.tweetapp.dto.TweetPostRequestDTO;
import com.tweetapp.dto.TweetPostResponseDTO;
import com.tweetapp.dto.TweetsResponseDTO;
import com.tweetapp.dto.UpdateTweetRequestDTO;
import com.tweetapp.exception.TweetNotFoundException;
import com.tweetapp.exception.UserNotFoundException;
import com.tweetapp.model.Reply;
import com.tweetapp.model.Tweet;
import com.tweetapp.repository.ReplyRepository;
import com.tweetapp.repository.TweetRepository;
import com.tweetapp.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TweetServiceImpl implements TweetService {
	
	@Autowired
	TweetRepository tweetRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	ReplyRepository replyRepository;

	@Override
	public ResponseEntity<TweetPostResponseDTO> postTweets(String username, TweetPostRequestDTO tweetPostRequestDTO) throws UserNotFoundException {
		log.info("Inside TweetServiceImpl || postTweets Method || Start");
		TweetPostResponseDTO tweetPostResponseDTO = new TweetPostResponseDTO();
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
				log.info("Inside TweetServiceImpl || postTweets Method || Tweet Posted",username);
				tweetPostResponseDTO.setMessage("Tweet Posted");
				log.info("Inside TweetServiceImpl || postTweets Method || End");
				return new ResponseEntity<TweetPostResponseDTO>(tweetPostResponseDTO,HttpStatus.CREATED);
			}
			log.info("Inside TweetServiceImpl || postTweets Method || Some error occur while posting",username);
			tweetPostResponseDTO.setMessage("Something gone wrong. Please try again");
			log.info("Inside TweetServiceImpl || postTweets Method || End");
			return new ResponseEntity<TweetPostResponseDTO>(tweetPostResponseDTO,HttpStatus.BAD_REQUEST);
		}
		else {
			log.info("Inside TweetServiceImpl || postTweets Method || User not found",username);
			tweetPostResponseDTO.setMessage("Something gone wrong. Please try again");
			log.info("Inside TweetServiceImpl || postTweets Method || End");
			throw new UserNotFoundException("User Not Found");
		}
	}

	@Override
	public ResponseEntity<?> replyTweet(String username, String tweetId, ReplyTweetRequestDTO replyTweetRequestDTO) {
		log.info("Inside TweetServiceImpl || replyTweet Method || Start");
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
					log.debug("Inside TweetServiceImpl || replyTweet Method || Reply done successfully",tweetId);
					log.info("Inside TweetServiceImpl || replyTweet Method || End");
					return new ResponseEntity<Tweet>(tweet, HttpStatus.OK);
				}
				log.debug("Inside TweetServiceImpl || replyTweet Method || Can not find the tweet",tweetId);
				log.info("Inside TweetServiceImpl || replyTweet Method || End");
				return new ResponseEntity<String>("Could not find the tweet", HttpStatus.NOT_FOUND);
			}
			log.debug("Inside TweetServiceImpl || replyTweet Method || Some error occur while replying the tweet",tweetId);
			log.info("Inside TweetServiceImpl || replyTweet Method || End");
			return new ResponseEntity<String>("Something went wrong", HttpStatus.BAD_REQUEST);
		}
		log.debug("Inside TweetServiceImpl || replyTweet Method || User not found",username);
		log.info("Inside TweetServiceImpl || replyTweet Method || End");
		return new ResponseEntity<String>("User not found", HttpStatus.NOT_FOUND);
	}

	@Override
	public ResponseEntity<?> getAlltweets() throws TweetNotFoundException {
		log.info("Inside TweetServiceImpl || getAlltweets Method || Start");
		List<TweetsResponseDTO> tweetResponse = new ArrayList<>();
		List<Tweet> tweets = tweetRepository.findAll();
		if(tweets.size() > 0) {
			for (Tweet tweet : tweets) {
				TweetsResponseDTO tweetsResponseDTO = new TweetsResponseDTO();
				tweetsResponseDTO.setTweetId(tweet.getTweetId());
				tweetsResponseDTO.setLoginId(tweet.getLoginId());
				tweetsResponseDTO.setTweetDescription(tweet.getTweetDescription());
				tweetsResponseDTO.setPostDate(tweet.getPostDate());
				tweetsResponseDTO.setTweetTag(tweet.getTweetTag());
				tweetsResponseDTO.setTweetLikes(tweet.getTweetLikes());
				List<Reply> replys = (List<Reply>) replyRepository.findAllById(tweet.getReplyId());
				tweetsResponseDTO.setReplys(replys);
				tweetsResponseDTO.setLikeIds(tweet.getLikeId());
				tweetResponse.add(tweetsResponseDTO);
			}
			log.debug("Inside TweetServiceImpl || getAlltweets Method || Tweets Found", tweets.size());
			log.info("Inside TweetServiceImpl || getAlltweets Method || End");
			return new ResponseEntity<List<TweetsResponseDTO>>(tweetResponse,HttpStatus.OK);
		}
		else if(tweets.size() == 0) {
			log.debug("Inside TweetServiceImpl || getAlltweets Method || No Tweets Found", tweets.size());
			log.info("Inside TweetServiceImpl || getAlltweets Method || End");
			throw new TweetNotFoundException("No Tweet Found");
		}
		log.debug("Inside TweetServiceImpl || getAlltweets Method || Error occur while fetching tweets", tweets.size());
		log.info("Inside TweetServiceImpl || getAlltweets Method || End");
		return new ResponseEntity<String>("Something went wrong", HttpStatus.BAD_REQUEST);
	}

	@Override
	public ResponseEntity<?> getAllTweetsByUsername(String username) throws TweetNotFoundException {
		log.info("Inside TweetServiceImpl || getAllTweetsByUsername Method || Start");
		List<TweetsResponseDTO> tweetResponse = new ArrayList<>();
		Optional<List<Tweet>> optional = tweetRepository.getAllTweetsByUsername(username);
		if(optional.isPresent()) {
			List<Tweet> tweets = optional.get();
			if(tweets.size() > 0) {
				for (Tweet tweet : tweets) {
					TweetsResponseDTO tweetsResponseDTO = new TweetsResponseDTO();
					tweetsResponseDTO.setTweetId(tweet.getTweetId());
					tweetsResponseDTO.setLoginId(tweet.getLoginId());
					tweetsResponseDTO.setTweetDescription(tweet.getTweetDescription());
					tweetsResponseDTO.setPostDate(tweet.getPostDate());
					tweetsResponseDTO.setTweetTag(tweet.getTweetTag());
					tweetsResponseDTO.setTweetLikes(tweet.getTweetLikes());
					List<Reply> replys = (List<Reply>) replyRepository.findAllById(tweet.getReplyId());
					tweetsResponseDTO.setReplys(replys);
					tweetsResponseDTO.setLikeIds(tweet.getLikeId());
					tweetResponse.add(tweetsResponseDTO);
				}
				log.debug("Inside TweetServiceImpl || getAllTweetsByUsername Method || Tweets Found", tweets.size());
				log.info("Inside TweetServiceImpl || getAllTweetsByUsername Method || End");
				return new ResponseEntity<List<TweetsResponseDTO>>(tweetResponse,HttpStatus.OK);
			}
			else if(tweets.size() == 0) {
				log.debug("Inside TweetServiceImpl || getAllTweetsByUsername Method || No Tweets Found", tweets.size());
				log.info("Inside TweetServiceImpl || getAllTweetsByUsername Method || End");
				throw new TweetNotFoundException("No Tweet's Posted by you. If you want to post click on Post Tweet");
			}
		}
		log.debug("Inside TweetServiceImpl || getAllTweetsByUsername Method || Error occur while fetching tweets");
		log.info("Inside TweetServiceImpl || getAllTweetsByUsername Method || End");
		return new ResponseEntity<String>("Something went wrong", HttpStatus.BAD_REQUEST);
	}

	@Override
	public ResponseEntity<Tweet> updateTweet(String username, String id, UpdateTweetRequestDTO updateTweetRequestDTO) throws TweetNotFoundException {
		log.info("Inside TweetServiceImpl || updateTweet Method || Start");
		Tweet tweet = tweetRepository.findById(id).get();
		if(!tweet.getTweetId().isEmpty()) {
			tweet.setTweetDescription(updateTweetRequestDTO.getTweetDescriptions());
			tweet.setTweetTag(updateTweetRequestDTO.getTweetTag());
			tweet = tweetRepository.save(tweet);
			log.debug("Inside TweetServiceImpl || updateTweet Method || Tweet Updated", id);
			log.info("Inside TweetServiceImpl || updateTweet Method || End");
			return new ResponseEntity<Tweet>(tweet,HttpStatus.OK);
		}
		log.debug("Inside TweetServiceImpl || updateTweet Method || Tweet Not Found", id);
		log.info("Inside TweetServiceImpl || updateTweet Method || End");
		throw new TweetNotFoundException("No tweet found");
	}

	@Override
	public ResponseEntity<DeleteTweetResponseDTO> deleteTweet(String username, String id) throws TweetNotFoundException {
		log.info("Inside TweetServiceImpl || deleteTweet Method || Start");
		Optional<Tweet> optional = tweetRepository.findById(id);
		DeleteTweetResponseDTO deleteTweetResponseDTO = new DeleteTweetResponseDTO();
		if(optional.isPresent()) {
			Tweet tweet = optional.get();
			if(tweet.getLoginId().equalsIgnoreCase(username)) {
				log.info("Inside TweetServiceImpl || deleteTweet Method || Tweet Deleted", id);
				tweetRepository.deleteById(id);
				if(tweet.getReplyId().size() > 0) {
					log.info("Inside TweetServiceImpl || deleteTweet Method || Tweet Replys Deleted", tweet.getReplyId().size());
					replyRepository.deleteAllById(tweet.getReplyId());
				}
				deleteTweetResponseDTO.setMessage("Tweet Deleted");
				log.info("Inside TweetServiceImpl || deleteTweet Method || End");
				return new ResponseEntity<DeleteTweetResponseDTO>(deleteTweetResponseDTO, HttpStatus.OK);
			}
			log.info("Inside TweetServiceImpl || deleteTweet Method || User not authorized to delete", username);
			deleteTweetResponseDTO.setMessage("You are not authorized to delete the Tweet");
			log.info("Inside TweetServiceImpl || deleteTweet Method || End");
			return new ResponseEntity<DeleteTweetResponseDTO>(deleteTweetResponseDTO, HttpStatus.FORBIDDEN);
		}
		log.debug("Inside TweetServiceImpl || deleteTweet Method || No tweet Found", id);
		deleteTweetResponseDTO.setMessage("You are not authorized to delete the Tweet");
		log.info("Inside TweetServiceImpl || deleteTweet Method || End");
		throw new TweetNotFoundException("Could not find the tweet");
	}

	@Override
	public ResponseEntity<?> likeTweet(String username, String id) {
		log.info("Inside TweetServiceImpl || likeTweet Method || Start");
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
					log.debug("Inside TweetServiceImpl || likeTweet Method || Liked tweet", likes);
					log.info("Inside TweetServiceImpl || likeTweet Method || End");
					return new ResponseEntity<Tweet>(tweet,HttpStatus.OK);
				}
				else {
					int likes = tweet.getTweetLikes() - 1;
					tweet.setTweetLikes(likes);
					List<String> likesId = tweet.getLikeId();
					likesId.remove(index);
					tweet.setLikeId(likesId);
					tweet = tweetRepository.save(tweet);
					log.debug("Inside TweetServiceImpl || likeTweet Method || Disliked tweet", likes);
					log.info("Inside TweetServiceImpl || likeTweet Method || End");
					return new ResponseEntity<Tweet>(tweet,HttpStatus.OK);
				}
			}
			log.debug("Inside TweetServiceImpl || likeTweet Method || No Tweet Found", id);
			log.info("Inside TweetServiceImpl || likeTweet Method || End");
			return new ResponseEntity<String>("Could not find the tweet", HttpStatus.NOT_FOUND);
		}
		log.debug("Inside TweetServiceImpl || likeTweet Method || User not found", username);
		log.info("Inside TweetServiceImpl || likeTweet Method || End");
		return new ResponseEntity<String>("User not found", HttpStatus.NOT_FOUND);
	}

}
