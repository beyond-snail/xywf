package com.yywf.model;

/**
 * 历史登录过账号的用户实体类
 * 
 * @author morton
 * 
 */
public class LoginHistoryVO {

	private String avatar;
	private String username;
	private String password;

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
