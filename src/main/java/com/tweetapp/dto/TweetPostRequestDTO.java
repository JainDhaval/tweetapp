package com.tweetapp.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TweetPostRequestDTO {

	private String tweetDescription;
	private String date;
	private String tweetTag;
	private int tweetLikes;
}
