package cn.xanderye.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * Created on 2020/3/12.
 *
 * @author XanderYe
 */
public class FileUtil {

    /**
     * 分割字符串
     *
     * @param str
     * @param open
     * @param close
     * @return java.lang.String
     * @author XanderYe
     * @date 2020/3/9
     */
    public static String substringBetween(String str, String open, String close) {
        if (str != null && open != null && close != null) {
            int start = str.indexOf(open);
            if (start != -1) {
                int end = str.indexOf(close, start + open.length());
                if (end != -1) {
                    return str.substring(start + open.length(), end);
                }
            }
            return null;
        } else {
            return null;
        }
    }


    /**
     * 检查路径
     *
     * @param path
     * @return java.lang.String
     * @author XanderYe
     * @date 2020/3/9
     */
    public static String checkPath(String path) {
        path = path.replace("\"", "");
        if (!path.endsWith(File.separator)) {
            path += File.separator;
        }
        return path;
    }

    /**
     * 复制单个文件
     *
     * @param oldPath 原文件路径 如：c:/test.txt
     * @param newPath 复制后路径 如：f:/test.txt
     * @return void
     * @author XanderYe
     * @date 2020/3/9
     */
    public static void copyFile(String oldPath, String newPath) {
        File oldFile = new File(oldPath);
        if (oldFile.exists()) {
            try (InputStream inStream = new FileInputStream(oldPath);
                 FileOutputStream fs = new FileOutputStream(newPath)) {

                String newFolderPath = newPath.substring(0, newPath.lastIndexOf(File.separator));
                // 目标路径不存在时自动创建文件夹
                new File(newFolderPath).mkdirs();
                // 文件存在时读入原文件
                byte[] buffer = new byte[1024];
                int byteSum = 0;
                int byteRead;
                while ((byteRead = inStream.read(buffer)) != -1) {
                    // 字节数(文件大小)
                    byteSum += byteRead;
                    fs.write(buffer, 0, byteRead);
                }
            } catch (Exception e) {
                System.out.println("复制单个文件出错");
                e.printStackTrace();
            }
        }
    }

    /**
     * 复制整个文件夹内容
     *
     * @param oldPath 原文件路径 如：c:/test
     * @param newPath 复制后路径 如：f:/test
     * @return void
     * @author XanderYe
     * @date 2020/3/9
     */
    public static void copyFolder(String oldPath, String newPath) {
        try {
            //如果文件夹不存在 则建立新文件夹
            (new File(newPath)).mkdirs();
            File a = new File(oldPath);
            String[] file = a.list();
            File temp;
            for (String s : file) {
                if (oldPath.endsWith(File.separator)) {
                    temp = new File(oldPath + s);
                } else {
                    temp = new File(oldPath + File.separator + s);
                }
                if (temp.isFile()) {
                    FileInputStream input = new FileInputStream(temp);
                    FileOutputStream output = new FileOutputStream(newPath + "/" +
                            (temp.getName()).toString());
                    byte[] b = new byte[1024 * 5];
                    int len;
                    while ((len = input.read(b)) != -1) {
                        output.write(b, 0, len);
                    }
                    output.flush();
                    output.close();
                    input.close();
                }
                if (temp.isDirectory()) {
                    //如果是子文件夹
                    copyFolder(oldPath + "/" + s, newPath + "/" + s);
                }
            }
        } catch (Exception e) {
            System.out.println("复制文件夹出错");
            e.printStackTrace();
        }
    }

    /**
     * 删除包文件夹
     *
     * @param path  文件或文件夹所在目录，如d:/test
     * @param names 要删除的文件或文件夹数组
     * @return void
     * @author XanderYe
     * @date 2020/3/9
     */
    public static void deletePackages(String path, String[] names) {
        for (String name : names) {
            String deleteFilePath = path + name + File.separator;
            File file = new File(deleteFilePath);
            if (file.exists()) {
                try {
                    deleteFile(deleteFilePath);
                    System.out.println("删除：" + deleteFilePath);
                } catch (Exception e) {
                    System.out.println("删除：" + deleteFilePath + "失败");
                }
            }
        }
    }

    /**
     * 删除文件或文件夹
     *
     * @param path
     * @return void
     * @author XanderYe
     * @date 2020/3/9
     */
    public static void deleteFile(String path) {
        File file = new File(path);
        if (file.isFile()) {
            file.delete();
        } else {
            File[] files = file.listFiles();
            if (files == null) {
                file.delete();
            } else {
                for (File value : files) {
                    deleteFile(value.getAbsolutePath());
                }
                file.delete();
            }
        }
    }
}
