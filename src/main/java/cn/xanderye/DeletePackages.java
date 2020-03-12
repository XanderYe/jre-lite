package cn.xanderye;

import java.io.File;

/**
 * Created on 2020/3/9.
 *
 * @author XanderYe
 */
public class DeletePackages {
    public static void main(String[] args) {
        String[] packages = {"com", "java", "javax", "jdk", "META-INF", "org", "sun", "javafx", "netscape", "version.rc"};
        String path = System.getProperty("user.dir") + File.separator;
        // 删除包
        Util.deletePackages(path, packages);
        System.out.println("删除完毕");
    }
}
