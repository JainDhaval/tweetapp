package com.tweetapp.dto;

import java.util.List;

import com.tweetapp.model.Reply;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TweetsResponseDTO {
	private String tweetId;
	private String loginId;
	private String tweetDescription;
	private String postDate;
	private String tweetTag;
	private int tweetLikes;
	private List<Reply> replys;
	private List<String> likeIds;
}
