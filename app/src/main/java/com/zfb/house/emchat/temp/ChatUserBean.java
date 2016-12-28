package com.zfb.house.emchat.temp;

import java.io.Serializable;

public class ChatUserBean implements Serializable {
	    private int userType;
		private String userName;//昵称
		private String imageUrl;

	public int getUserType() {
		return userType;
	}

	public void setUserType(int userType) {
		this.userType = userType;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
}