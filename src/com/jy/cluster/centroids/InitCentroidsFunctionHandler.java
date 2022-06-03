package com.jy.cluster.centroids;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 初始化质心方法执行器
 *
 * @author jinchunzhao
 * @version 1.0
 * @date 2022-02-03 10:50
 */
public interface InitCentroidsFunctionHandler {


    /**
     * 对Map类型数据的值就行排序
     *
     * @param map
     *        元数据
     * @return
     *        排序后的数据
     */
    default List<Map.Entry<Integer, Double>> orderMapValue(Map<Integer, Double> map){
        List<Map.Entry<Integer, Double>> orderList = map.entrySet().stream()
                .sorted((entry1, entry2) -> entry1.getValue().compareTo(entry2.getValue()))
                .collect(Collectors.toList());
        return orderList;
    }

    /**
     * 初始化质心
     *
     * @param dataList
     *        元数据
     * @param seed
     *        随机种子
     * @param numClusters
     *        聚簇个数
     * @param clusterDataMap
     *        初始化聚类数据
     * @param distanceCalcType
     *        距离方法类型
     * @return
     *        初始化质心集合
     */
    List<List<Double>> initCentroids(List<List<Double>> dataList, int seed, int numClusters, Map<Integer, List<List<Double>>> clusterDataMap,String distanceCalcType);
}