package com.tool.utils.utils;

import java.math.BigDecimal;
import java.text.NumberFormat;

/**
 * 进行BigDecimal对象的加减乘除，四舍五入等运算的工具类
 * 
 * @author ivan
 * 
 */
public class ArithUtil {

	// 默认除法运算精度
	private static final int DEF_DIV_SCALE = 10;

	// 这个类不能实例化
	private ArithUtil() {
	}

	/**
	 * 校验字符串，为空则返回0
	 * 
	 * @param s
	 * @return
	 */
	private static String verifyEmpty(String s) {
		if (s == null || s.trim().length() <= 0) {
			return "0";
		}
		return s;
	}

	/**
	 * double数值相加
	 * 
	 * @param v1
	 * @param v2
	 * @return
	 */
	public static double add(double v1, double v2) {
		return add(Double.toString(v1), Double.toString(v2));
	}

	/**
	 * double数值相加
	 * 
	 * @param v1
	 * @param v2
	 * @return
	 */
	public static double add(String v1, String v2) {
		BigDecimal b1 = new BigDecimal(verifyEmpty(v1));
		BigDecimal b2 = new BigDecimal(verifyEmpty(v2));
		return b1.add(b2).doubleValue();
	}

	/**
	 * double数值相减
	 * 
	 * @param v1
	 * @param v2
	 * @return
	 */
	public static double sub(double v1, double v2) {
		return sub(Double.toString(v1), Double.toString(v2));
	}

	/**
	 * double数值相减
	 * 
	 * @param v1
	 * @param v2
	 * @return
	 */
	public static double sub(String v1, String v2) {
		BigDecimal b1 = new BigDecimal(verifyEmpty(v1));
		BigDecimal b2 = new BigDecimal(verifyEmpty(v2));
		return b1.subtract(b2).doubleValue();
	}

	public static int formatIntSub(String v1, String v2) {
		BigDecimal b1 = new BigDecimal(verifyEmpty(v1));
		BigDecimal b2 = new BigDecimal(verifyEmpty(v2));
		return b1.subtract(b2).intValue();
	}

	/**
	 * double数值相乘
	 * 
	 * @param v1
	 * @param v2
	 * @return
	 */
	public static double mul(double v1, double v2) {
		return mul(Double.toString(v1), Double.toString(v2));
	}

	/**
	 * double数值相乘
	 * 
	 * @param v1
	 * @param v2
	 * @return
	 */
	public static double mul(String v1, String v2) {
		BigDecimal b1 = new BigDecimal(verifyEmpty(v1));
		BigDecimal b2 = new BigDecimal(verifyEmpty(v2));
		return b1.multiply(b2).doubleValue();
	}

	public static int formatIntMul(String v1, String v2) {
		BigDecimal b1 = new BigDecimal(verifyEmpty(v1));
		BigDecimal b2 = new BigDecimal(verifyEmpty(v2));
		return b1.multiply(b2).intValue();
	}

	/**
	 * double数值相除
	 * 
	 * @param v1
	 * @param v2
	 * @return
	 */
	public static double div(double v1, double v2) {
		return div(v1, v2, DEF_DIV_SCALE);
	}

	/**
	 * double数值相除
	 * 
	 * @param v1
	 * @param v2
	 * @return
	 */
	public static double div(String v1, String v2) {
		return div(v1, v2, DEF_DIV_SCALE);
	}

	public static int formatIntDiv(String v1, String v2) {
		return div2(v1, v2, DEF_DIV_SCALE);
	}

	public static double div(double v1, double v2, int scale) {
		return div(Double.toString(v1), Double.toString(v2), scale);
	}

	/**
	 * double数值相除
	 * 
	 * @param v1
	 * @param v2
	 * @param scale
	 *            保留小数位数
	 * @return
	 */
	public static double div(String v1, String v2, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException(
					"The scale must be a positive integer or zero");
		}
		BigDecimal b1 = new BigDecimal(verifyEmpty(v1));
		BigDecimal b2 = new BigDecimal(verifyEmpty(v2));
		return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	public static int div2(String v1, String v2, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException(
					"The scale must be a positive integer or zero");
		}
		BigDecimal b1 = new BigDecimal(verifyEmpty(v1));
		BigDecimal b2 = new BigDecimal(verifyEmpty(v2));
		return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).intValue();
	}

	/**
	 * 四舍五入保留scale位小数
	 * 
	 * @param v
	 * @param scale
	 *            保留的小数位数
	 * @return
	 */
	public static double round(String v, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException(
					"The scale must be a positive integer or zero");
		}
		BigDecimal b = new BigDecimal(v);
		BigDecimal one = new BigDecimal("1");
		return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	/**
	 * 四舍五入保留scale位小数
	 * 
	 * @param v
	 * @param scale
	 *            保留的小数位数
	 * @return
	 */
	public static double round(double v, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException(
					"The scale must be a positive integer or zero");
		}
		BigDecimal b = new BigDecimal(v);
		BigDecimal one = new BigDecimal(1);
		return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	/**
	 * 将double型数字格式化小数部分所指定的位数
	 * 
	 * @param v
	 * @param digits
	 *            小数部分位数
	 * @return
	 */
	public static String formatFractionDigits(double v, int digits) {
		NumberFormat numberFormat = NumberFormat.getInstance();
		// 设置数的小数部分所允许的最小位数
		numberFormat.setMinimumFractionDigits(digits);
		numberFormat.setMaximumFractionDigits(digits);
		return numberFormat.format(v);
	}

}
