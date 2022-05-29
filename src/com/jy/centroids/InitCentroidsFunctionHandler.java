package com.jy.centroids;

import java.util.List;
import java.util.Map;

/**
 * 初始化质心方法执行器
 *
 * @author jinchunzhao
 * @version 1.0
 * @date 2022-02-03 10:50
 */
public interface InitCentroidsFunctionHandler {

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
     * @return
     *        初始化质心集合
     */
    List<List<Double>> initCentroids(List<List<Double>> dataList, int seed, int numClusters, Map<Integer, List<List<Double>>> clusterDataMap);
}