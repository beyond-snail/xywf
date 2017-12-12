package com.tool.utils.utils;

import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.View.MeasureSpec;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class BMapUtil {
	
	/**
	 * 从view 得到图片
	 * @param view
	 * @return
	 */
	public static Bitmap getBitmapFromView(View view) {
        view.destroyDrawingCache();
        view.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.setDrawingCacheEnabled(true);
        Bitmap bitmap = view.getDrawingCache(true);
        return bitmap;
	}
	
	/**
	 * 图片压缩
	 * 解码图片和缩放减少内存消耗
	 * @param  file
	 * @return bitmap
	 * @throws NotFoundException, Exception
	 * @author WG
	 */
	public static Bitmap decodeFile(File f) throws NotFoundException, Exception {
		try {
			//解码图片尺寸
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(new FileInputStream(f), null, o);
			
			//新的尺寸，我们想要的数值范围
			final int REQUIRED_SIZE = 600;
			//查询正确的范围值，应该快进2
			int scale=1;
			while(o.outWidth/scale/2>=REQUIRED_SIZE && o.outHeight/scale/2>=REQUIRED_SIZE)
				scale*=2;
			
			//解码宽样式取大小
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
		} catch(FileNotFoundException e) {
			e.printStackTrace();
		}
		
		return null;
    } 
	
	/**
     * 图片压缩
     * 解码图片和缩放减少内存消耗
     * @param  file
     * @return bitmap
     * @throws NotFoundException, Exception
     * @author WG
     */
    public static Bitmap decodeFile2(File f) throws NotFoundException, Exception {
        try {
            //解码图片尺寸
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f), null, o);
            
            //新的尺寸，我们想要的数值范围
            final int REQUIRED_SIZE = 300;
            //查询正确的范围值，应该快进2
            int scale=1;
            while(o.outWidth/scale/2>=REQUIRED_SIZE && o.outHeight/scale/2>=REQUIRED_SIZE)
                scale*=2;
            
            //解码宽样式取大小
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        }
        
        return null;
    }  
	
	/**
	 * bitmap 转 file
	 * @param context
	 * @param bitmap
	 * @return
	 */
	public static String bitmapToFile(Context context, Bitmap bitmap) {
		File f = null;
		String filePath = null;
		
		try {
			//创建一个文件写到bitmap数据
			//新建文件（放在安卓缓存目录,  文件名）
			f = new File(context.getCacheDir(), UtilFile.getPhotoFileName());
			//创建文件
			f.createNewFile();
			filePath = f.getPath();
			
			//写入byte数组到文件里
			//先创建文件输出输入流
			FileOutputStream mFileOutputStream = new FileOutputStream(f);
			mFileOutputStream.write(bitmapToByteArray(bitmap));
			mFileOutputStream.close();
			mFileOutputStream.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return filePath ; 
	}
	
	/**
	 * bitmap转换成byte数组
	 */
	
	public static byte[] bitmapToByteArray(Bitmap bitmap) {
		ByteArrayOutputStream mByteArrayOutputStream = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 80, mByteArrayOutputStream);
		
		return mByteArrayOutputStream.toByteArray();
	}
	
	/**
	 * 图片横竖判断
	 * @return true  横
	 * @return false 竖
	 * @author WG
	 */
	public static boolean pictureDirction(String filePath) throws NotFoundException, Exception {
		Bitmap bitmap = BitmapFactory.decodeFile(filePath);
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		if(width > height) {  //如果宽大，说明图片是横向拍出来的
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * view 转 btimap
	 * @param view
	 * @return
	 */
	public static Bitmap convertViewToBitmap(View view){
		view.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
	        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
	        view.buildDrawingCache();
	        Bitmap bitmap = view.getDrawingCache();

	 return bitmap;
	}
	
	/**
     * view 转 btimap
     * @param view
     * @return
     */
    public static String fileTofile(Context context, String file){
        String path = "";
        Bitmap b =null;
        try {
            b = decodeFile(new File(file));
            path = bitmapToFile(context, b);
            
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            b =null;
        }
        return path;
    }
    
    /**
     * view 转 btimap  加大压缩
     * @param view
     * @return
     */
    public static String fileTofile2(Context context, String file){
        String path = "";
        Bitmap b =null;
        try {
            b = decodeFile2(new File(file));
            path = bitmapToFile(context, b);
            
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            b =null;
        }
        return path;
    }
}
