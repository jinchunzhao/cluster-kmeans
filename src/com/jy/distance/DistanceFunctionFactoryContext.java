package com.jy.distance;


import com.jy.enums.DistanceFunctionEnum;

import java.util.HashMap;
import java.util.Map;

/**
 * 执行器工厂上下文
 *
 * @author jinchunzhao
 * @version 1.0
 * @date 2022-02-03 10:40
 */
public class DistanceFunctionFactoryContext {

    /**
     * 距离方法执行器实例缓存
     */
    private static final Map<String, DistanceFunctionHandler> DISTANCE_FACTORY_MAP = new HashMap<>();

    /**
     * 初始化执行器实例
     */
    private static void initHandlerInstance() {
        DISTANCE_FACTORY_MAP.put(DistanceFunctionEnum.EUCLIDEAN_DISTANCE.getCode(), new EuclideanDistance());
        DISTANCE_FACTORY_MAP.put(DistanceFunctionEnum.MANHATTAN_DISTANCE.getCode(), new ManhattanDistance());
        DISTANCE_FACTORY_MAP.put(DistanceFunctionEnum.COS_DISTANCE.getCode(), new CosDistance());
    }

    /**
     * 根据类型获取对应的执行器实现类
     *
     * @param type
     *        执行器类型
     * @return
     *        执行器实现类
     */
    public static DistanceFunctionHandler getInvokeHandler(String type){
        initHandlerInstance();
        return DISTANCE_FACTORY_MAP.get(type);
    }

}
