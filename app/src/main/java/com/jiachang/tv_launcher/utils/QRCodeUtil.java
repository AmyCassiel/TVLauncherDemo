package com.jiachang.tv_launcher.utils;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.text.TextUtils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;

/**
 * @author Mickey.Ma
 * @date 2020-04-01
 * @description 二维码工具类
 */
public class QRCodeUtil {
    /**
     * 生成一个二维码图像
     *
     * @param content
     * @return
     */
    public static Bitmap createQRCode(String content) {
        try {
            if (TextUtils.isEmpty(content) || TextUtils.equals("null", content) || "".equals(content)) {
                return null;
            }
            BitMatrix matrix = new MultiFormatWriter().encode(new String(content.getBytes("UTF-8"), "iso-8859-1"), BarcodeFormat.QR_CODE, 200, 200);
            return generateQRBitmap(matrix);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 绘制二维码
     *
     * @param matrix
     * @return
     */
    public static Bitmap generateQRBitmap(BitMatrix matrix) {
        int w = matrix.getWidth();
        int h = matrix.getHeight();
        int[] rawData = new int[w * h];
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                int color = Color.WHITE;
                if (matrix.get(i, j)) {
                    color = Color.BLACK;
                }
                rawData[i + (j * w)] = color;
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.RGB_565);
        bitmap.setPixels(rawData, 0, w, 0, 0, w, h);
        return bitmap;
    }
}
