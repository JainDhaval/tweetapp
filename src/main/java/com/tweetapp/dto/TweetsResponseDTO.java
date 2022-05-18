package com.tweetapp.dto;

import java.util.List;

import com.tweetapp.model.Tweet;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TweetsResponseDTO {

	private List<Tweet> tweets;
}
