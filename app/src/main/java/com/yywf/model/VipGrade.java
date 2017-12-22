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

	private int grade;
	private boolean isDefault;
	private boolean isHot;

	public int getGrade() {
		return grade;
	}

	public void setGrade(int grade) {
		this.grade = grade;
	}

	public boolean isDefault() {
		return isDefault;
	}

	public void setDefault(boolean aDefault) {
		isDefault = aDefault;
	}

	public boolean isHot() {
		return isHot;
	}

	public void setHot(boolean hot) {
		isHot = hot;
	}
}
