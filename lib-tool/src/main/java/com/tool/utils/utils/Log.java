package com.tool.utils.utils;

public class Log {

	/**
	 * 控制日志的开关
	 */
	private static boolean LogSwitch = true;
	public static String TAG = "global";

	/**
	 * 打印verbose级别的日志
	 * 
	 * @param tag
	 *            标记
	 * @param text
	 *            日志内容
	 */
	public static void verbose(String tag, String text) {
		if (LogSwitch) {
			android.util.Log.w(tag, text);
		}
	}

	/**
	 * 
	 * 打印debug级别的日志 [功能详细描述]
	 * 
	 * @param obj
	 *            tag标记，传入当前调用的类对象即可，方法会转化为该对象对应的类名
	 * @param text
	 *            日志内容
	 */
	public static void debug(Object obj, String text) {
		if (LogSwitch) {
			if (obj != null) {
				debug(obj.getClass().getSimpleName(), text);
			}
		}
	}

	/**
	 * 打印debug级别的日志
	 * 
	 * @param tag
	 *            标记
	 * @param text
	 *            日志内容
	 */
	public static void debug(String tag, String text) {
		if (LogSwitch) {
			android.util.Log.d(tag, text);
		}
	}

	/**
	 * 打印info级别的日志
	 * 
	 * @param tag
	 *            标记
	 * @param text
	 *            日志内容
	 */
	public static void info(String tag, String text) {
		if (LogSwitch) {
			android.util.Log.i(tag, text);
		}

	}

	/**
	 * 打印warn级别的日志
	 * 
	 * @param tag
	 *            标记
	 * @param text
	 *            日志内容
	 */
	public static void warn(String tag, String text) {
		if (LogSwitch) {
			android.util.Log.w(tag, text);
		}
	}

	/**
	 * 打印warn级别的日志
	 * 
	 * @param tag
	 *            标记
	 * @param text
	 *            日志内容
	 * @param throwable
	 *            异常信息
	 */
	public static void warn(String tag, String text, Throwable throwable) {
		if (LogSwitch) {
			android.util.Log.w(tag, text, throwable);
		}
	}

	/**
	 * 打印error级别的日志
	 * 
	 * @param tag
	 *            标记
	 * @param text
	 *            日志内容
	 */
	public static void error(String tag, String text) {
		if (LogSwitch) {
			android.util.Log.e(tag, text);
		}
	}

	/**
	 * 打印error级别的日志
	 * 
	 * @param tag
	 *            标记
	 * @param text
	 *            日志内容
	 * @param throwable
	 *            异常信息
	 */
	public static void error(String tag, String text, Throwable throwable) {
		if (LogSwitch) {
			android.util.Log.e(tag, text, throwable);
		}
	}

}
