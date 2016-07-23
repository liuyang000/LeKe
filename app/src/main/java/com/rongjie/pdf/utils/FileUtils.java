package com.rongjie.pdf.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * Created by Administrator on 2016/7/13.
 * 文件操作
 */
public class FileUtils {

    public static final String APP_CACHE_DIR_NAME = "RONG_JIE_BOOK";
    public static final String TEXT_BOOK = "text_book";
    public  static  final  String TEXT_BOOK_ICON = "text_book_icon";



    /** 判断是否有存储卡，有返回TRUE，否则FALSE */
    public static boolean isSDcardExist() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }
    /**
     * 判断文件是否存在
     * @param pathName 文件全路径名称
     * @return 有返回TRUE，否则FALSE
     */
    public static boolean exists(String pathName) {
        try {
            return !isEmpty(pathName) && new File(pathName).exists();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 创建目录
     * @param dirPath 目录全路径名
     * @return 是否创建成功
     */
    public static boolean createDir(String dirPath) {
        File file = new File(dirPath);
        if (!file.exists() || !file.isDirectory()) {
            return file.mkdir();
        }
        return true;
    }


    /**
     * 创建多级目录，如果上级目录不存在，会先创建上级目录
     * @param dirPath 目录全路径名
     * @return 是否创建成功
     */
    public static boolean createDirs(String dirPath) {
        File file = new File(dirPath);
        if (!file.exists() || !file.isDirectory()) {
            return file.mkdirs();
        }
        return true;
    }


    /**
     * 删除文件
     * @param path 被删除的文件路径
     * @return 是否删除成功
     */
    public static boolean deleteFile(String path) {
        return new File(path).delete();
    }

    /** 获取安装在用户手机上的sdcard/下的chianandroid目录 */
    public static String getSDCardPath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath() + "/";
    }

    /** 获取应用的路径，如果没有SD卡，则返回data/data目录下的应用目录，如果有，则返回SD卡上的应用目录 */
    public static String getAppFilesDir(Context context) {
        if (isSDcardExist()) {
            return getSDCardPath() + APP_CACHE_DIR_NAME + "/";
        } else {
            return getAppFilesDirByData(context);
        }
    }


    /**获取课本的目录*/
    public  static String  getTextBookFilesDir(Context context) {
        return getAppFilesDir(context)+TEXT_BOOK+"/";
    }


    /**获取课本图片目录*/
    public  static String  getTextBookIconFilesDir(Context context) {
        return getAppFilesDir(context)+TEXT_BOOK_ICON + "/";
    }


    /** 获取文件data/data目录 */
    public static String getAppFilesDirByData(Context context) {
        return context.getFilesDir().getAbsolutePath() + "/";
    }

    /**判断字符串是否为空*/
    private static boolean isEmpty(String value) {
        return !(value != null && !"".equalsIgnoreCase(value.trim()) && !"null".equalsIgnoreCase(value.trim()));
    }

    public static boolean makeParentsDir(String filePath){
        if(isEmpty(filePath)){
            return false;
        }
        File targetFile = new File(filePath);
        File targetParent = targetFile.getParentFile();
        if(!targetParent.exists()){
            makeParentsDir(targetParent.getAbsolutePath());
            targetParent.mkdir();
        }else{
            if(targetParent.isDirectory()){
                return true;
            }else{
                return false;
            }
        }
        return true;
    }
}
