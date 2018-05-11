package cn.financial.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class Config {
    private Properties config;
    private String path;

    public Config() {
    }

    public Config(String path) {
        this.config = new Properties();
        setPath(path);
    }

    private void checkExist() {
        File file = new File(this.path);
        File parent = file.getParentFile();

        if (!parent.exists()) {
            parent.mkdirs();
        }

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getPath() {
        return this.path;
    }

    public String getProperty(String key) {
        return this.config.getProperty(key);
    }

    public String getProperty(String key, String defaultValue) {
        String value = getProperty(key);
        value = value == null ? defaultValue : value;
        return value;
    }

    private void loadConfig() {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(this.path);
            this.config.clear();
            this.config.load(fis);
        } catch (Exception e) {
            e.printStackTrace();

            if (fis != null) {
                try {
                    fis.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void remove(Object key) {
        this.config.remove(key);
    }

    public void removeAndSave(Object key) {
        remove(key);
        saveConfig();
    }

    public void saveConfig() {
        saveConfig(null);
    }

    public void saveConfig(String comments) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(this.path);
            this.config.store(fos, comments);
        } catch (Exception e) {
            e.printStackTrace();

            if (fos != null) {
                try {
                    fos.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void savePropertie(String key, String value) {
        savePropertie(key, value, null);
    }

    public void savePropertie(String key, String value, String comments) {
        setPropertie(key, value);
        saveConfig(comments);
    }

    public void setPath(String path) {
        this.path = path;
        checkExist();
        loadConfig();
    }

    public void setPropertie(String key, String value) {
        this.config.setProperty(key, value);
    }
}
