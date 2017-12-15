package com.yywf.model;

import java.io.Serializable;

public class Message implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int id;
	private int messageId;
	private String gmtCreate;
	private boolean hasRead;
	private String memberName;
	private String memberPhone;
	private String memo;
	private String sendFrom;
	private String title;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getMessageId() {
		return messageId;
	}

	public void setMessageId(int messageId) {
		this.messageId = messageId;
	}

	public String getGmtCreate() {
		return gmtCreate;
	}

	public void setGmtCreate(String gmtCreate) {
		this.gmtCreate = gmtCreate;
	}

	public boolean isHasRead() {
		return hasRead;
	}

	public void setHasRead(boolean hasRead) {
		this.hasRead = hasRead;
	}

	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	public String getMemberPhone() {
		return memberPhone;
	}

	public void setMemberPhone(String memberPhone) {
		this.memberPhone = memberPhone;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getSendFrom() {
		return sendFrom;
	}

	public void setSendFrom(String sendFrom) {
		this.sendFrom = sendFrom;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	// private long gmt_create;
	// private int send_user_id;
	// private int level;
	// private int recevice_user_id;
	// private String memo;
	// private int message_id;
	// private int message_type;
	// private String member_customer_name;
	// private long gmt_modified;
	// private String title;
	// private String send_from;
	// private String member_mobile_phone;
	// private int id;
	// private int has_read;

	// public long getGmt_create() {
	// return gmt_create;
	// }
	//
	// public void setGmt_create(long gmt_create) {
	// this.gmt_create = gmt_create;
	// }
	//
	// public int getSend_user_id() {
	// return send_user_id;
	// }
	//
	// public void setSend_user_id(int send_user_id) {
	// this.send_user_id = send_user_id;
	// }
	//
	// public int getLevel() {
	// return level;
	// }
	//
	// public void setLevel(int level) {
	// this.level = level;
	// }
	//
	// public int getRecevice_user_id() {
	// return recevice_user_id;
	// }
	//
	// public void setRecevice_user_id(int recevice_user_id) {
	// this.recevice_user_id = recevice_user_id;
	// }
	//
	// public String getMemo() {
	// return memo;
	// }
	//
	// public void setMemo(String memo) {
	// this.memo = memo;
	// }
	//
	// public int getMessage_id() {
	// return message_id;
	// }
	//
	// public void setMessage_id(int message_id) {
	// this.message_id = message_id;
	// }
	//
	// public int getMessage_type() {
	// return message_type;
	// }
	//
	// public void setMessage_type(int message_type) {
	// this.message_type = message_type;
	// }
	//
	// public String getMember_customer_name() {
	// return member_customer_name;
	// }
	//
	// public void setMember_customer_name(String member_customer_name) {
	// this.member_customer_name = member_customer_name;
	// }
	//
	// public long getGmt_modified() {
	// return gmt_modified;
	// }
	//
	// public void setGmt_modified(long gmt_modified) {
	// this.gmt_modified = gmt_modified;
	// }
	//
	// public String getTitle() {
	// return title;
	// }
	//
	// public void setTitle(String title) {
	// this.title = title;
	// }
	//
	// public String getSend_from() {
	// return send_from;
	// }
	//
	// public void setSend_from(String send_from) {
	// this.send_from = send_from;
	// }
	//
	// public String getMember_mobile_phone() {
	// return member_mobile_phone;
	// }
	//
	// public void setMember_mobile_phone(String member_mobile_phone) {
	// this.member_mobile_phone = member_mobile_phone;
	// }
	//
	// public int getId() {
	// return id;
	// }
	//
	// public void setId(int id) {
	// this.id = id;
	// }
	//
	// public int getHas_read() {
	// return has_read;
	// }
	//
	// public void setHas_read(int has_read) {
	// this.has_read = has_read;
	// }

}
