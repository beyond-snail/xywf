package com.yywf.model;

import java.io.Serializable;

public class QbQrResult implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int qrcodeId;
	private int qrcodeOrderId;
	private String qrcodeKey;
	private long qrcodeCreateDate;
	private String qrcodeUserId;
	private String qrcodeGroupId;
	private String qrcodeQrcode;
	private String qrcodeOrgOrderNum;
	private String qrcodeTranAmt;
	private String qrcodeStatus;

	public int getQrcodeId() {
		return qrcodeId;
	}

	public void setQrcodeId(int qrcodeId) {
		this.qrcodeId = qrcodeId;
	}

	public int getQrcodeOrderId() {
		return qrcodeOrderId;
	}

	public void setQrcodeOrderId(int qrcodeOrderId) {
		this.qrcodeOrderId = qrcodeOrderId;
	}

	public String getQrcodeKey() {
		return qrcodeKey;
	}

	public void setQrcodeKey(String qrcodeKey) {
		this.qrcodeKey = qrcodeKey;
	}

	public long getQrcodeCreateDate() {
		return qrcodeCreateDate;
	}

	public void setQrcodeCreateDate(long qrcodeCreateDate) {
		this.qrcodeCreateDate = qrcodeCreateDate;
	}

	public String getQrcodeUserId() {
		return qrcodeUserId;
	}

	public void setQrcodeUserId(String qrcodeUserId) {
		this.qrcodeUserId = qrcodeUserId;
	}

	public String getQrcodeGroupId() {
		return qrcodeGroupId;
	}

	public void setQrcodeGroupId(String qrcodeGroupId) {
		this.qrcodeGroupId = qrcodeGroupId;
	}

	public String getQrcodeQrcode() {
		return qrcodeQrcode;
	}

	public void setQrcodeQrcode(String qrcodeQrcode) {
		this.qrcodeQrcode = qrcodeQrcode;
	}

	public String getQrcodeOrgOrderNum() {
		return qrcodeOrgOrderNum;
	}

	public void setQrcodeOrgOrderNum(String qrcodeOrgOrderNum) {
		this.qrcodeOrgOrderNum = qrcodeOrgOrderNum;
	}

	public String getQrcodeTranAmt() {
		return qrcodeTranAmt;
	}

	public void setQrcodeTranAmt(String qrcodeTranAmt) {
		this.qrcodeTranAmt = qrcodeTranAmt;
	}

	public String getQrcodeStatus() {
		return qrcodeStatus;
	}

	public void setQrcodeStatus(String qrcodeStatus) {
		this.qrcodeStatus = qrcodeStatus;
	}

}
