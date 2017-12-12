package com.tool.utils.utils;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Environment;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class UtilFile {

	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@SuppressLint("NewApi")
	public static boolean hasSDCard() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			// require api level larger or equal to 9
			if (Build.VERSION.SDK_INT >= 9) {
				Environment.getExternalStorageDirectory().setReadable(true);
				Environment.getExternalStorageDirectory().setWritable(true);
			}
			return true;
		}
		return false;
	}

	/**
	 *
	 * @Title: copyFileToSDCard
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param @param ctx
	 * @param @param source
	 * @param @return
	 * @return String 返回目的地址
	 * @throws
	 */
	public static String copyFileFromRawToSDCard(Context ctx, int rawId) {
		String apkPath = Environment.getExternalStorageDirectory().getPath() + "/heiche/";
		String apkName = "qrcode.png";
		// 删除旧的魔指apk
		File tmp = new File(apkPath + apkName);
		if (tmp.exists() && tmp.isFile()) {
			tmp.delete();
		}
		File dir = new File(apkPath);
		// 创建目录
		if (!dir.exists()) {
			dir.mkdirs();
		}
		File file = new File(apkPath, apkName);
		try {
			// assets下对于超过2M 的文件，有所限制，建议改名为Jpg
			InputStream is = ctx.getAssets().open("qrcode.png");
			// InputStream is = ctx.getResources().openRawResource(rawId);
			FileOutputStream os = new FileOutputStream(file);
			byte[] bytes = new byte[512];
			int i = -1;
			while ((i = is.read(bytes)) > 0) {
				os.write(bytes, 0, i);
			}
			os.close();
			is.close();

			String permission = "666";
			try {
				String command = "chmod " + permission + " " + apkPath + "/" + apkName;
				Runtime runtime = Runtime.getRuntime();
				runtime.exec(command);
			} catch (IOException e) {
				e.printStackTrace();
			}

		} catch (Exception e) {
		}
		return apkPath + apkName;
	}

	/**
	 * @author chenzheng_Java 保存用户输入的内容到文件
	 */
	public static void save(Context context, String phonNum) {
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DATE);
		String content = year + "-" + month + "-" + day;

		File f = new File(savePath);
		if (!f.exists()) {
			try {

				// 按照指定的路径创建文件夹

				f.mkdirs();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		File dir = new File(savePath + phonNum);

		if (!dir.exists()) {

			try {

				// 在指定的文件夹中创建文件

				dir.createNewFile();

			} catch (Exception e) {

			}

		}

		try {

			/*
			 * 根据用户提供的文件名，以及文件的应用模式，打开一个输出流.文件不存系统会为你创建一个的，
			 * 至于为什么这个地方还有FileNotFoundException抛出，我也比较纳闷。在Context中是这样定义的 public
			 * abstract FileOutputStream openFileOutput(String name, int mode)
			 * throws FileNotFoundException; openFileOutput(String name, int
			 * mode); 第一个参数，代表文件名称，注意这里的文件名称不能包括任何的/或者/这种分隔符，只能是文件名
			 * 该文件会被保存在/data/data/应用名称/files/chenzheng_java.txt 第二个参数，代表文件的操作模式
			 * MODE_PRIVATE 私有（只能创建它的应用访问） 重复写入时会文件覆盖 MODE_APPEND 私有
			 * 重复写入时会在文件的末尾进行追加，而不是覆盖掉原来的文件 MODE_WORLD_READABLE 公用 可读
			 * MODE_WORLD_WRITEABLE 公用 可读写
			 */
			FileOutputStream outputStream = new FileOutputStream(savePath
					+ phonNum);
			outputStream.write(content.getBytes());
			outputStream.flush();
			outputStream.close();
			// Toast.makeText(FileTest.this, "保存成功", Toast.LENGTH_LONG).show();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * @author chenzheng_java 读取刚才用户保存的内容
	 */
	public static String read(Context context, String phonNum) {
		try {

			buildSavePath();
			File f = new File(savePath + phonNum);
			if (!f.exists()) {
				save(context, phonNum);

			}

			FileInputStream inputStream = new FileInputStream(savePath
					+ phonNum);
			byte[] bytes = new byte[1024];
			ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
			while (inputStream.read(bytes) != -1) {
				arrayOutputStream.write(bytes, 0, bytes.length);
			}
			inputStream.close();

			String content = new String(arrayOutputStream.toByteArray());
			// showTextView.setText(content);
			arrayOutputStream.close();
			return content;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return "";
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}

	}

	private static String savePath;

	/**
	 * 构建下载路径
	 */
	private static void buildSavePath() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			savePath = Environment.getExternalStorageDirectory().getPath()
					+ "/login/";
		} else {
			savePath = "/data/login/";
		}
	}

	/**
	 * <li>功能描述：时间相减得到天数
	 *
	 * @param beginDateStr
	 * @param endDateStr
	 * @return long
	 * @author Administrator
	 */
	public static long getDaySub(String beginDateStr, String endDateStr) {
		long day = 0;
		SimpleDateFormat format = new SimpleDateFormat(
				"yyyy-MM-dd");
		Date beginDate;
		Date endDate;
		try {
			beginDate = format.parse(beginDateStr);
			endDate = format.parse(endDateStr);
			day = (endDate.getTime() - beginDate.getTime())
					/ (24 * 60 * 60 * 1000);
			// System.out.println("相隔的天数="+day);
		} catch (ParseException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}
		return day;
	}

	/**
	 * 创建目录
	 * 
	 * @param filePath
	 * @return
	 */
	public static File createPath(String filePath) {
		File file = null;
		try {
			file = new File(filePath);
			if (!file.exists()) {
				file.mkdir();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return file;
	}

	/**
	 * 创建文件
	 * 
	 * @param filePath
	 * @param fileName
	 * @return
	 */
	public static File createFile(String filePath, String fileName) {
		File file = null;
		try {
			file = new File(filePath);
			if (!file.exists()) {
				file.mkdir();
			}
			file = new File(filePath + fileName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return file;
	}

	// 上传代码，第一个参数，为要使用的URL，第二个参数，为表单内容，第三个参数为要上传的文件，可以上传多个文件，这根据需要页定
	private static final String TAG = "uploadFile";
	private static final int TIME_OUT = 10 * 1000; // 超时时间
	private static final String CHARSET = "utf-8"; // 设置编码

	/**
	 * android上传文件到服务器
	 * 
	 * @param file
	 *            需要上传的文件
	 * @param RequestURL
	 *            请求的rul
	 * @return 返回响应的内容
	 */
	public static String uploadFile(File file, String RequestURL) {
		String result = null;
		String BOUNDARY = UUID.randomUUID().toString(); // 边界标识 随机生成
		String PREFIX = "--", LINE_END = "\r\n";
		String CONTENT_TYPE = "multipart/form-data"; // 内容类型

		try {
			URL url = new URL(RequestURL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(TIME_OUT);
			conn.setConnectTimeout(TIME_OUT);
			conn.setDoInput(true); // 允许输入流
			conn.setDoOutput(true); // 允许输出流
			conn.setUseCaches(false); // 不允许使用缓存
			conn.setRequestMethod("POST"); // 请求方式
			conn.setRequestProperty("Charset", CHARSET); // 设置编码
			conn.setRequestProperty("connection", "keep-alive");
			conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary="
					+ BOUNDARY);

			if (file != null) {
				/**
				 * 当文件不为空，把文件包装并且上传
				 */
				DataOutputStream dos = new DataOutputStream(
						conn.getOutputStream());
				StringBuffer sb = new StringBuffer();
				sb.append(PREFIX);
				sb.append(BOUNDARY);
				sb.append(LINE_END);
				/**
				 * 这里重点注意： name里面的值为服务器端需要key 只有这个key 才可以得到对应的文件
				 * filename是文件的名字，包含后缀名的 比如:abc.png
				 */
				sb.append("Content-Disposition: form-data; name=\"file\"; filename=\""
						+ file.getName() + "\"" + LINE_END);
				sb.append("Content-Type: image/pjpeg; charset=" + CHARSET
						+ LINE_END);
				sb.append(LINE_END);
				dos.write(sb.toString().getBytes());
				InputStream is = new FileInputStream(file);
				byte[] bytes = new byte[1024];
				int len = 0;
				while ((len = is.read(bytes)) != -1) {
					dos.write(bytes, 0, len);
				}
				is.close();
				dos.write(LINE_END.getBytes());
				byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END)
						.getBytes();
				dos.write(end_data);
				dos.flush();
				/**
				 * 获取响应码 200=成功 当响应成功，获取响应的流
				 */
				int res = conn.getResponseCode();
				Log.info(TAG, "response code:" + res);
				if (res == 200) {
					Log.info(TAG, "request success");
					InputStream input = conn.getInputStream();
					StringBuffer sb1 = new StringBuffer();
					int ss;
					while ((ss = input.read()) != -1) {
						sb1.append((char) ss);
					}
					result = sb1.toString();
					Log.info(TAG, "result : " + result);
				} else {
					Log.error(TAG, "request error");
				}
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 使用系统当前日期加以调整作为照片的名称
	 * 
	 * @return
	 */
	public static String getPhotoFileName() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"'IMG'_yyyyMMdd_HHmmssSSS", Locale.getDefault());
		return dateFormat.format(date) + "_"+(int)(Math.random()*100) + ".jpg";
	}

}
