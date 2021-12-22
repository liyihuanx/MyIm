package liyihuan.app.android.lib_camera;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.drawable.DrawableCompat;import liyihuan.app.android.lib_camera.FileUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: BitmapUtils
 * @Description: java类作用描述
 * @Author: liyihuan
 * @Date: 2021/5/27 22:03
 */
public class BitmapUtils {

    public static final int IMAGE_RESOLUTION_BIG_WIDTH = 720;// 输出图片大小
    public static final int IMAGE_RESOLUTION_BIG_HEIGHT = 720;// 输出图片大小

    public static final int IMAGE_RESOLUTION_SMALL_WIDTH = 120;// 输出图片大小
    public static final int IMAGE_RESOLUTION_SMALL_HEIGHT = 120;// 输出图片大小

    /**
     * Save bitmap to dest path.
     *
     * @param bitmap
     * @param destPath
     */
    public static File saveBitmap2JPG(Bitmap bitmap, String destPath) {
        return saveBitmap2JPG(bitmap, destPath, 100);
    }

    public static File saveBitmap2JPG(Bitmap bitmap, String destPath, int quality) {
        File newFile = FileUtil.createFile(destPath);
        if (newFile == null || bitmap == null) {
            return null;
        }
        FileOutputStream outStream = null;
        try {
            outStream = new FileOutputStream(newFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outStream);
            outStream.flush();
            Log.e("BitmapUtils", "image size: save - " + newFile.length() / Constants.KB + " kb");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (outStream != null) {
                try {
                    outStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return newFile;
    }


}
