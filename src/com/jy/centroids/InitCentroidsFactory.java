package com.jy.centroids;

import com.jy.enums.InitCentroidsEnum;
import com.jy.plugin.ClusterArithmeticRegion;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 初始化质心工厂
 *
 * @author jinchunzhao
 * @version 1.0
 * @date 2022-05-29 12:58
 */
public class InitCentroidsFactory {

    /**
     * 要扫描的策略类的包
     */
    public static final String SCAN_PACKAGE = InitCentroidsFactory.class.getPackage().getName();

    /**
     * 初始化质心方法执行器实例缓存Map
     */
    private static final Map<String, Class<? extends InitCentroidsFunctionHandler>> HANDLER_MAP = new HashMap<>();

    /**
     * 构造函数，初始化质心方法执行器实例缓存Map
     */
    public InitCentroidsFactory() {
        try {
            initHandlerMap();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据code获取质心方法执行器实例
     *
     * @param initFactionType 初始化方法类型
     * @return 执行器实例
     */
    public InitCentroidsFunctionHandler getHandler(String initFactionType) {

        Class<? extends InitCentroidsFunctionHandler> clazz = HANDLER_MAP.get(initFactionType);
        if (Objects.isNull(clazz)) {
            return null;
        }
        ClusterArithmeticRegion annotation = clazz.getAnnotation(ClusterArithmeticRegion.class);
        InitCentroidsEnum initCentroidsEnum = InitCentroidsEnum.machCode(annotation.factionType());
        if (Objects.nonNull(initCentroidsEnum)) {
            try {
                return clazz.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;

    }


    /**
     * 初始化质心方法执行器实例缓存Map
     *
     * @throws ClassNotFoundException 未找到类
     */
    private void initHandlerMap() throws ClassNotFoundException {

        String path = System.getProperty("user.dir") + File.separator + "src" + File.separator + SCAN_PACKAGE.replace(".", File.separator);
        File file = new File(path);
        String[] files = file.list();
        for (String str : files) {
            String forName = SCAN_PACKAGE + "." + str.replace(".java", "");
            Class<? extends InitCentroidsFunctionHandler> clazz = (Class<? extends InitCentroidsFunctionHandler>) Class.forName(forName);
            if (clazz.isAnnotationPresent(ClusterArithmeticRegion.class)) {
                ClusterArithmeticRegion annotation = clazz.getAnnotation(ClusterArithmeticRegion.class);
                HANDLER_MAP.put(annotation.factionType(), clazz);
            }
        }
    }

    /**
     * 获取初始质心工厂实例
     *
     * @return 初始质心工厂实例
     */
    public static InitCentroidsFactory getInstance() {
        return new InitCentroidsFactory();
    }
}
