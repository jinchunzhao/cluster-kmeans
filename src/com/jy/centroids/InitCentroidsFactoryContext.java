package com.jy.centroids;


import com.jy.enums.InitCentroidsEnum;

import java.util.HashMap;
import java.util.Map;

/**
 * 执行器上下文
 *
 * @author jinchunzhao
 * @version 1.0
 * @date 2022-02-03 10:54
 */
public class InitCentroidsFactoryContext {

    /**
     * 初始质心算法执行器实例缓存
     */
    private static final Map<String, InitCentroidsFunctionHandler> CENTROIDS_FACTORY_MAP = new HashMap<>();

    /**
     * 初始化执行器实例
     */
    private static void initHandlerInstance() {
        CENTROIDS_FACTORY_MAP.put(InitCentroidsEnum.RANDOM.getCode(), new RandomCentroids());
        CENTROIDS_FACTORY_MAP.put(InitCentroidsEnum.KMEANS_PLUS.getCode(), null);
        CENTROIDS_FACTORY_MAP.put(InitCentroidsEnum.FARTHEST_FIRST.getCode(), null);
    }

    /**
     * 根据类型获取对应的执行器实现类
     *
     * @param type
     *        执行器类型
     * @return
     *        执行器实现类
     */
    public static InitCentroidsFunctionHandler getInvokeHandler(String type){
        initHandlerInstance();
        return CENTROIDS_FACTORY_MAP.get(type);
    }

}
