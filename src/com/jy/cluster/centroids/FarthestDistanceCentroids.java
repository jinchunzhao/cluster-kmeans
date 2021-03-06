package com.jy.cluster.centroids;

import com.jy.cluster.distance.DistanceFunctionFactory;
import com.jy.cluster.distance.DistanceFunctionHandler;
import com.jy.cluster.plugin.ClusterArithmeticRegion;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * 最远距离算法
 *
 * @author jinchunzhao
 * @version 1.0
 * @date 2022-06-03 16:27
 */
@ClusterArithmeticRegion(factionType = "farthest_first")
public class FarthestDistanceCentroids implements InitCentroidsFunctionHandler {
    @Override
    public List<List<Double>> initCentroids(List<List<Double>> dataList, int seed, int numClusters, Map<Integer, List<List<Double>>> clusterDataMap, String distanceCalcType) {

        List<List<Double>> points = new ArrayList<>();
        int dataSize = dataList.size();
        Random random = new Random();
        int r = random.nextInt(dataSize - 1);
        List<Double> firstPoint = dataList.get(r);
        points.add(firstPoint);

        DistanceFunctionFactory functionFactory = DistanceFunctionFactory.getInstance();
        DistanceFunctionHandler distanceHandler = functionFactory.getHandler(distanceCalcType);
        numClusters--;
        clusterDataMap.put(numClusters, new ArrayList<>());
        List<Double> currentPoints = new ArrayList<>();
        currentPoints.addAll(firstPoint);
        do {
            numClusters--;
            clusterDataMap.put(numClusters, new ArrayList<>());
            Map<Integer, Double> distanceMap = distanceHandler.getInitCentroidsDistance(currentPoints, dataList);
            List<Map.Entry<Integer, Double>> entries = orderMapValue(distanceMap);
            // 最远的距离
            Map.Entry<Integer, Double> entry = entries.get(entries.size() - 1);
            Integer index = entry.getKey();
            List<Double> nextPoints = dataList.get(index);
            currentPoints.clear();
            currentPoints.addAll(nextPoints);
            points.add(nextPoints);
        } while (numClusters > 0);
        return points;
    }
}
