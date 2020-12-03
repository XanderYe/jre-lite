package cn.xanderye;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        // 选择精简rt.jar还是jfxrt.jar
        boolean isJfx = false;
        if (args.length > 0) {
            if ("jfx".equals(args[0])) {
                isJfx = true;
            }
        }
        Scanner scanner = new Scanner(System.in);
        System.out.print("请输入classes.txt文件路径（支持拖拽）：");
        // class列表文件
        String classesFilePath = scanner.nextLine();
        if (isJfx) {
            System.out.print("请输入jfxrt.jar解压后的jfxrt文件夹路径（支持拖拽）：");
            // rt文件夹
            String rtPath = scanner.nextLine();
            Lite.liteJfxRt(classesFilePath, rtPath);
        } else {
            System.out.print("请输入rt.jar解压后的请输入rt文件夹路径（支持拖拽）：");
            // rt文件夹
            String rtPath = scanner.nextLine();
            Lite.liteRt(classesFilePath, rtPath);
        }
    }
}
