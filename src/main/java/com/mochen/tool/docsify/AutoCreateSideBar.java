package com.mochen.tool.docsify;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AutoCreateSideBar {

    private static final Logger log = LogManager.getLogger();

    public static void main(String[] args) {
//        String path = "E:/workspace/LearnProject/docsify/learning-notes";
        String path = "F:/WorkSpace/learn_code/docsify/learning-notes";
        List<FilesMap> files = getFiles(path, "");

        outFiles(files, "", 0, null);
//        createReadMeMd(path, "", "_sidebar.md");
//        removeReadMeMd(path, "");
    }

    private static int start = 3;
    private static int end = 10;

    public static void outFiles(List<FilesMap> files, String front, int hierarchy, String upper) {
        if (upper != null && start > 0 && start == hierarchy){
            System.out.println(String.format("* [< 返回首页](/)"));
        }

        for (FilesMap fm : files) {
            if (hierarchy >= start){
                // 输出内容
                if (hierarchy == end-1){
                    if (fm.getType() == '0'){
                        System.out.println(String.format(front+"* [%s](%s)", fm.getName(), fm.getPath().replaceAll(" ", "%20")));
                    }else{
                        System.out.println(String.format(front+"* [**%s**](%s)", fm.getName(), fm.getPath().replaceAll(" ", "%20")));
                    }
                }else{
                    if (fm.getType() == '0'){
                        System.out.println(String.format(front+"* [%s](%s)", fm.getName(), fm.getPath().replaceAll(" ", "%20")));
                    }else{
                        System.out.println(String.format(front+"* **%s**", fm.getName()));
                    }
                }
            }

            hierarchy++;
            if (hierarchy >= end) {
                hierarchy --;
                continue;
            }
            if (fm.getChilds() != null) {
                outFiles(fm.getChilds(), front+"  ", hierarchy, fm.getPath());
                hierarchy --;
            }
        }

    }

    public static List<FilesMap> getFiles(String path, String absPath) {
        File file = new File(path);
        File[] tempList = file.listFiles();

        List<FilesMap> filesMaps = new ArrayList<>();

        for (int i = 0; i < Objects.requireNonNull(tempList).length; i++) {
            FilesMap filesMap = new FilesMap();
            String name = tempList[i].getName();
            if (tempList[i].isFile()) {
                // 排除后缀为md且没有卵用的文件
                if (name.equals("_sidebar.md") || name.equals("README.md")) continue;
                // 获取到文件后缀名，但是有的文件没有后缀
                String[] split = name.split("\\.");
                // 有后缀且文件名为md的留下
                if (split.length != 2 || !split[1].equals("md")) continue;
                filesMap.setType('0');
                filesMap.setName(split[0]); // 名字不要后缀
                filesMap.setPath(absPath + "/" + name);
                filesMap.setChilds(null);
            } else if (tempList[i].isDirectory()) {
                if(name.equals("photo")) continue;
                // 当前的路径是一个目录，这个目录下有我们生成的README.md，将在此文件夹中生成一个侧边栏文件_sidebar.md
                List<FilesMap> files = getFiles(path + "/" + name, absPath + "/" + name);
                filesMap.setType('1');
                filesMap.setName(name);
                filesMap.setPath(absPath + "/" + name + "/");
                filesMap.setChilds(files);
            }
            filesMaps.add(filesMap);
        }
        return filesMaps;
    }

    /**
     * 在每一个文件夹下都创建一个指定文件名的文件文件
     *
     * @param path
     * @param absPath
     */
    public static void createReadMeMd(String path, String absPath, String fileName) {
        ArrayList<String> files = new ArrayList<>();
        File file = new File(path);
        File[] tempList = file.listFiles();

        try {
            File rmdFile = new File(path + "/" + fileName);
            if (rmdFile.createNewFile())
                log.info("文件创建成功！");
            else
                log.info("该文件已经存在。path=" + path);
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < Objects.requireNonNull(tempList).length; i++) {
            if (tempList[i].isDirectory()) {
                String dirName = tempList[i].getName();
                log.info(absPath + "文件夹：" + dirName);
                if (dirName.equals("photo")) continue;
                // 深入下一级
                createReadMeMd(path + "/" + dirName, absPath + "/" + dirName, fileName);
            }
        }
    }

    /**
     * 因为第一次写的创建文件的方法有问题，重新吧photo目录的README.md文件删除
     * 若没有这个问题直接执行上一下方法即可
     *
     * @param path
     * @param absPath
     */
    public static void removeReadMeMd(String path, String absPath) {
        ArrayList<String> files = new ArrayList<>();
        File file = new File(path);
        File[] tempList = file.listFiles();

        for (int i = 0; i < Objects.requireNonNull(tempList).length; i++) {
            if (tempList[i].isDirectory()) {
                String dirName = tempList[i].getName();
                if (dirName.equals("photo")) {
                    File rmdFile = new File(path + "/photo/README.md");
                    if (rmdFile.delete())
                        log.info("文件删除成功！");
                    else
                        log.info("删除失败 path= " + path + "/photo/README.md");
                }
                // 深入下一级
                removeReadMeMd(path + "/" + dirName, absPath + "/" + dirName);
            }
        }
    }

}
