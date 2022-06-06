package com.tweetapp.exception;

public class TweetNotFoundException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 981223106459677076L;
	
	public TweetNotFoundException(String message) {
		super(message);
	}

}
