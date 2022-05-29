package com.jy.centroids;

import com.jy.plugin.ClusterArithmeticRegion;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * 随机算法
 *
 * @author jinchunzhao
 * @version 1.0
 * @date 2022-02-03 10:54
 */
@ClusterArithmeticRegion(factionType = "random")
public class RandomCentroids implements InitCentroidsFunctionHandler {


    @Override
    public List<List<Double>> initCentroids(List<List<Double>> dataList, int seed, int numClusters, Map<Integer, List<List<Double>>> clusterDataMap) {
        return randomCenterPoint(dataList, seed, numClusters, clusterDataMap);
    }


    /**
     * 随机产生k个中心点
     *
     * @param dataList 元数据
     */
    private List<List<Double>> randomCenterPoint(List<List<Double>> dataList, int seed, int numClusters, Map<Integer, List<List<Double>>> clusterDataMap) {

        List<List<Double>> initPoints = new ArrayList<>();
        int dataSize = dataList.size();

        List<Integer> randoms = new ArrayList<>();

        Random random = new Random();
        random.setSeed(seed);

        for (int i = 0; i < numClusters; i++) {
            spinRandom(dataSize, randoms, random);
            List<Double> point = dataList.get(randoms.get(randoms.size() - 1));
            initPoints.add(point);
            clusterDataMap.put(i, new ArrayList<>());
        }

        return initPoints;
    }


    /**
     * 自旋获取不重复的簇中心坐标
     *
     * @param dataSize 数据集的元素数量
     * @param randoms  随机数集合
     * @param random   Random实例
     */
    private void spinRandom(int dataSize, List<Integer> randoms, Random random) {
        while (true) {
            int r = random.nextInt(dataSize - 1);
            if (!randoms.contains(r)) {
                randoms.add(r);
                break;
            }
        }
    }


}
