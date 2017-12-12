package com.tool.utils.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.TypedValue;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;


/**
 * 转换工具
 *
 * @author yang
 * @since 2016-8-8 上午11:43:16
 */
public class ConvertUtils {

    /**
     * dp转px
     */
    public static float dpToPx(Context context, float dp) {
        // 方式一
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context
                .getResources().getDisplayMetrics());
        // 方式二
        //float px = dp * DeviceInfo.SCREEN_DENSITY;
        return px;
    }

    /**
     * Bitmap转 byte[]
     */
    public static byte[] BitmapToBytes(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    /**
     * 保留小数位
     *
     * @param val   原始值
     * @param scale 要保留小数点左边的多少位
     * @return
     */
    public static double rounding(double val, int scale) {
        val = val + 0.0000000001D;
        BigDecimal bigDecimal = new BigDecimal(val);
        return bigDecimal.setScale(scale, BigDecimal.ROUND_DOWN).doubleValue();
    }

    /**
     * 保留小数位
     *
     * @param val   原始值
     * @param scale 要保留小数点左边的多少位
     * @return
     */
    public static float rounding(float val, int scale) {
        val = val + 0.0000000001F;
        BigDecimal bigDecimal = new BigDecimal(val);
        return bigDecimal.setScale(scale, BigDecimal.ROUND_DOWN).floatValue();
    }


}
