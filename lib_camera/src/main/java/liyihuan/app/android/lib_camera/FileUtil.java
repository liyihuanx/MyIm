package liyihuan.app.android.lib_camera;

import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import liyihuan.app.android.lib_camera.DeviceUtil;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @ClassName: FileUtil
 * @Description: java类作用描述
 * @Author: liyihuan
 * @Date: 2021/5/27 22:07
 */
class FileUtil {

    /**
     * 创建文件
     *
     * @param path
     * @return
     */
    @Nullable
    public static File createFile(@NonNull String path) {
        File file = null;
        if (DeviceUtil.isCanUseSD() && !TextUtils.isEmpty(path)) {
            try {
                file = new File(path);
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }
                if (file.exists()) {
                    file.delete();
                }
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    /**
     * 获取文件大小
     *
     * @param filePath
     * @return
     */
    public static long getFileSize(@NonNull String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return 0;
        }
        return getFileSize(new File(filePath));
    }

    /**
     * 获取文件大小
     *
     * @param file
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    public static long getFileSize(@Nullable File file) {
        if (file == null || !file.exists()) {
            Log.e("FileUtil", "文件不存在!");
            return 0;
        }
        if (!file.canRead()) {
            file.setReadable(true);
        }
        long size = 0;
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            size = fis.available();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close(fis);
        }
        return size;
    }


    /**
     * 关闭流
     *
     * @param closeables Closeable
     */
    @SuppressWarnings("WeakerAccess")
    public static void close(Closeable... closeables) {
        if (closeables == null || closeables.length == 0) {
            return;
        }
        for (Closeable closeable : closeables) {
            if (closeable != null) {
                try {
                    closeable.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
