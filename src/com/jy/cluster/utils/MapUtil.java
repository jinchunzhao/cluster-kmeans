package com.jy.cluster.utils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Map类型数据工具类
 *
 * @author jinchunzhao
 * @version 1.0
 * @date 2022-06-03 16:48
 */
public class MapUtil {


    /**
     * 对Map类型数据的值就行排序
     *
     * @param map
     *        元数据
     * @return
     *        排序后的数据
     */
    public static List<Map.Entry<Integer, Double>> orderMapValue(Map<Integer, Double> map){
        List<Map.Entry<Integer, Double>> orderList = map.entrySet().stream()
                .sorted((entry1, entry2) -> entry1.getValue().compareTo(entry2.getValue()))
                .collect(Collectors.toList());
        return orderList;
    }

}
