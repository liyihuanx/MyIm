package liyihuan.app.android.lib_camera;

import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * @ClassName: BuildProperties
 * @Description: java类作用描述
 * @Author: liyihuan
 * @Date: 2021/5/27 21:38
 */
public class BuildProperties {
    private final Properties properties;

    private BuildProperties() throws IOException {
        properties = new Properties();

        InputStream is = null;
        try {
            is = new FileInputStream(new File(Environment.getRootDirectory(), "build.prop"));
            properties.load(is);
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    public boolean containsKey(final Object key) {
        return properties.containsKey(key);
    }

    public boolean containsValue(final Object value) {
        return properties.containsValue(value);
    }

    public Set<Map.Entry<Object, Object>> entrySet() {
        return properties.entrySet();
    }

    public String getProperty(final String name) {
        return properties.getProperty(name);
    }

    public String getProperty(final String name, final String defaultValue) {
        return properties.getProperty(name, defaultValue);
    }

    public boolean isEmpty() {
        return properties.isEmpty();
    }

    public Enumeration<Object> keys() {
        return properties.keys();
    }

    public Set<Object> keySet() {
        return properties.keySet();
    }

    public int size() {
        return properties.size();
    }

    public Collection<Object> values() {
        return properties.values();
    }

    public static BuildProperties newInstance() throws IOException {
        return new BuildProperties();
    }
}
