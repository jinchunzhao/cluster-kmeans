package com.jy.cluster.utils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * 文件工具类
 *
 * @author jinchunzhao
 * @version 1.0
 * @date 2022-06-08 20:44
 */
public class FileUtil {

    /**
     * 获取包下指定的类名称
     *
     * @param packagePath
     *        包路径
     * @throws IOException
     *         IOException
     */
    public static List<String> getFileClassNames(String packagePath) throws IOException {

        List<String> classNames = new ArrayList<>();
        Enumeration<URL> resources = Thread.currentThread().getContextClassLoader().getResources(packagePath.replace(".", "/"));
        while (resources.hasMoreElements()) {
            URL nextElement = resources.nextElement();
            String filePath = nextElement.getFile();
            File file = new File(filePath);
            String[] list = file.list();
            assert list != null;
            String className = null;

            for (String fileName : list) {
                boolean endsWith = fileName.endsWith(".class");
                if (endsWith) {
                    className = fileName.substring(0, fileName.length() - 6);
                    classNames.add(className);
//                        try {
//                            //装载class
//                            Class<? extends InitCentroidsFunctionHandler> clazz = (Class<? extends InitCentroidsFunctionHandler>) Thread.currentThread().getContextClassLoader().loadClass(packagePath + "." + fileName);
//
//                            if (clazz.isAnnotationPresent(ClusterArithmeticRegion.class)){
//                                ClusterArithmeticRegion annotation = clazz.getAnnotation(ClusterArithmeticRegion.class);
//                                map.put(annotation.factionType(), clazz);
//                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
                }
            }
        }
        return classNames;
    }
}