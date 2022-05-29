package com.jy.distance;

import com.jy.centroids.InitCentroidsFactory;
import com.jy.centroids.InitCentroidsFunctionHandler;
import com.jy.enums.DistanceFunctionEnum;
import com.jy.enums.InitCentroidsEnum;
import com.jy.plugin.ClusterArithmeticRegion;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 最小误差距离算法工厂
 *
 * @author jinchunzhao
 * @version 1.0
 * @date 2022-05-29 15:26
 */
public class DistanceFunctionFactory {

    /**
     * 要扫描的策略类的包
     */
    private static final String SCAN_PACKAGE = DistanceFunctionFactory.class.getPackage().getName();

    /**
     * 最小误差距离算法执行器实例缓存Map
     */
    private static final Map<String, Class<? extends DistanceFunctionHandler>> HANDLER_MAP = new HashMap<>();

    /**
     * 私有化无参构造函数，最小误差距离算法执行器实例缓存Map
     */
    private DistanceFunctionFactory() {
        try {
            initHandlerMap();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据code获取最小误差距离算法执行器实例
     *
     * @param distanceFunctionType 最小误差距离算法类型
     * @return 执行器实例
     */
    public DistanceFunctionHandler getHandler(String distanceFunctionType) {

        Class<? extends DistanceFunctionHandler> clazz = HANDLER_MAP.get(distanceFunctionType);
        if (Objects.isNull(clazz)) {
            return null;
        }
        ClusterArithmeticRegion annotation = clazz.getAnnotation(ClusterArithmeticRegion.class);
        DistanceFunctionEnum functionEnum = DistanceFunctionEnum.machCode(annotation.factionType());
        if (Objects.nonNull(functionEnum)) {
            try {
                return clazz.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;

    }

    /**
     * 获取最小误差距离算法工厂实例
     *
     * @return 最小误差距离算法工厂实例
     */
    public static DistanceFunctionFactory getInstance() {
        return new DistanceFunctionFactory();
    }

    /**
     * 最小误差距离算法执行器实例缓存Map
     *
     * @throws ClassNotFoundException 未找到类
     */
    private void initHandlerMap() throws ClassNotFoundException {

        String path = System.getProperty("user.dir") + File.separator + "src" + File.separator + SCAN_PACKAGE.replace(".", File.separator);
        File file = new File(path);
        String[] files = file.list();
        for (String str : files) {
            String forName = SCAN_PACKAGE + "." + str.replace(".java", "");
            Class<? extends DistanceFunctionHandler> clazz = (Class<? extends DistanceFunctionHandler>) Class.forName(forName);
            if (clazz.isAnnotationPresent(ClusterArithmeticRegion.class)){
                ClusterArithmeticRegion annotation = clazz.getAnnotation(ClusterArithmeticRegion.class);
                HANDLER_MAP.put(annotation.factionType(), clazz);
            }
        }
    }

}
