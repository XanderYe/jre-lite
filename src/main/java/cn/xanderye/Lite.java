package cn.xanderye;

import cn.xanderye.util.FileUtil;
import cn.xanderye.util.SystemUtil;

import java.io.*;
import java.nio.charset.Charset;

public class Lite {

    public static void liteRt(String classesFilePath, String rtPath) {
        // 工作目录
        String newRtPath = System.getProperty("user.dir");
        // 检查目录
        rtPath = FileUtil.checkPath(rtPath);
        newRtPath = FileUtil.checkPath(newRtPath);
        System.out.println("当前工作目录：" + newRtPath);
        try {
            System.out.println("开始复制class");
            int count = copyClasses(classesFilePath, rtPath, newRtPath, false);
            System.out.println("已复制：" + count);
            String[] jarPackage = {"jar", "cvfm", "rt.jar", "META-INF/MANIFEST.MF"};
            String[] packages = {"com", "java", "javax", "jdk", "META-INF", "org", "sun"};
            // 合并数组
            String[] command = new String[packages.length + jarPackage.length];
            System.arraycopy(jarPackage, 0, command, 0, jarPackage.length);
            System.arraycopy(packages, 0, command, jarPackage.length, packages.length);
            System.out.println("打包rt.jar");
            SystemUtil.execStr(Charset.defaultCharset(), command);
            System.out.println("删除临时文件");
            deletePackages();
            System.out.println("精简完成");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 精简方法
     * @param classesFilePath
     * @param rtPath
     * @return void
     * @author XanderYe
     * @date 2020/3/12
     */
    public static void liteJfxRt(String classesFilePath, String rtPath) {
        // 工作目录
        String newRtPath = System.getProperty("user.dir");
        // 检查目录
        rtPath = FileUtil.checkPath(rtPath);
        newRtPath = FileUtil.checkPath(newRtPath);
        System.out.println("当前工作目录：" + newRtPath);
        try {
            System.out.println("开始复制class");
            int count = copyClasses(classesFilePath, rtPath, newRtPath, true);
            System.out.println("已复制：" + count);
            String[] jarPackage = {"jar", "cvfm", "jfxrt.jar", "META-INF/MANIFEST.MF"};
            String[] packages = {"com", "javafx", "META-INF", "netscape", "version.rc"};
            // 合并数组
            String[] command = new String[packages.length + jarPackage.length];
            System.arraycopy(jarPackage, 0, command, 0, jarPackage.length);
            System.arraycopy(packages, 0, command, jarPackage.length, packages.length);
            System.out.println("打包jfxrt.jar");
            SystemUtil.execStr(Charset.defaultCharset(), command);
            System.out.println("删除临时文件");
            deletePackages();
            System.out.println("精简完成");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取class文件并复制
     *
     * @param classesFilePath
     * @param rtPath
     * @param targetPath
     * @return int
     * @author XanderYe
     * @date 2020/3/9
     */
    private static int copyClasses(String classesFilePath, String rtPath, String targetPath, boolean isJfx) {
        File file = new File(classesFilePath);
        int count = 0;
        if (file.exists()) {
            try (FileReader fileReader = new FileReader(file);
                 BufferedReader bufferedReader = new BufferedReader(fileReader)) {
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    line = formatClassPath(line, isJfx);
                    if (line != null) {
                        String oldPath = rtPath + line;
                        String newPath = targetPath + line;
                        System.out.println("复制：" + newPath);
                        // 复制class
                        FileUtil.copyFile(oldPath, newPath);
                        count++;
                    }
                }
                if (count > 0) {
                    // 复制META-INF
                    FileUtil.copyFolder(rtPath + "META-INF", targetPath + "META-INF");
                    if (isJfx) {
                        FileUtil.copyFile(rtPath + "version.rc", targetPath + "version.rc");
                    }
                }
            } catch (Exception e) {
                System.out.println("class列表文件读取错误");
            }
        } else {
            System.out.println("class列表文件不存在");
        }
        return count;
    }

    /**
     * 格式化类路径
     *
     * @param s
     * @return java.lang.String
     * @author XanderYe
     * @date 2020/3/9
     */
    private static String formatClassPath(String s, boolean isJfx) {
        String jar = isJfx ? "jfxrt.jar" : "rt.jar";
        if (s.contains("[Loaded ") && (s.contains(jar))) {
            s = FileUtil.substringBetween(s, "[Loaded ", " ");
            if (s != null) {
                return s.replace(".", File.separator) + ".class";
            }
        }
        return null;
    }

    private static void deletePackages() {
        String[] packages = {"com", "java", "javax", "jdk", "META-INF", "org", "sun", "javafx", "netscape", "version.rc"};
        String path = System.getProperty("user.dir") + File.separator;
        // 删除包
        FileUtil.deletePackages(path, packages);
    }
}
