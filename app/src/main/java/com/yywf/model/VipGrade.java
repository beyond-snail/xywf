/******************************************************************
 *
 *    Java Lib For Android, Powered By GuangZhou WLB.
 *
 *    Package:     cn.myapp.mobile.owner.model
 *
 *    Filename:    OilCardVO.java
 *
 *    Description: TODO(用一句话描述该文件做什么)
 *
 *    Copyright:   Copyright (c) 2015-2018
 *
 *    Company:     Digital Telemedia Co.,Ltd
 *
 *    @author:     WLB
 *
 *    @version:    1.0.0
 *
 *    Create at:   2017年5月23日 下午5:26:26
 *
 *    Revision:
 *
 *    2017年5月23日 下午5:26:26
 *        - first revision
 *
 *****************************************************************/
package com.yywf.model;

import java.io.Serializable;

/**
 * @ClassName OilCardVO
 * @author WLB
 * @category 所属功能分类
 * @Date 2017年5月23日 下午5:26:26
 * @Description what
 */
public class VipGrade implements Serializable {

	private static final long serialVersionUID = 1L;

	private int id;
	private String gradename;
	private int purchasePrice;
	private int gradegive;
	private int gradedemand;
	private int profitratio;
	private int cashback;
	private int earnestMoney;
	private boolean isDefault;
	private int ishot;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getGradename() {
		return gradename;
	}

	public void setGradename(String gradename) {
		this.gradename = gradename;
	}

	public int getPurchasePrice() {
		return purchasePrice;
	}

	public void setPurchasePrice(int purchasePrice) {
		this.purchasePrice = purchasePrice;
	}

	public int getGradegive() {
		return gradegive;
	}

	public void setGradegive(int gradegive) {
		this.gradegive = gradegive;
	}

	public int getGradedemand() {
		return gradedemand;
	}

	public void setGradedemand(int gradedemand) {
		this.gradedemand = gradedemand;
	}

	public int getProfitratio() {
		return profitratio;
	}

	public void setProfitratio(int profitratio) {
		this.profitratio = profitratio;
	}

	public int getCashback() {
		return cashback;
	}

	public void setCashback(int cashback) {
		this.cashback = cashback;
	}

	public boolean isDefault() {
		return isDefault;
	}

	public void setDefault(boolean aDefault) {
		isDefault = aDefault;
	}

	public int getEarnestMoney() {
		return earnestMoney;
	}

	public void setEarnestMoney(int earnestMoney) {
		this.earnestMoney = earnestMoney;
	}

	public int getIshot() {
		return ishot;
	}

	public void setIshot(int ishot) {
		this.ishot = ishot;
	}
}
