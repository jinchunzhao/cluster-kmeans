package com.jy.cluster.distance;

import com.jy.cluster.plugin.ClusterArithmeticRegion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 切比雪夫距离
 *
 * @author jinchunzhao
 * @version 1.0
 * @date 2022-06-01 20:07
 */
@ClusterArithmeticRegion(factionType = "chebyshevDistance")
public class ChebyshevDistance implements DistanceFunctionHandler {
    @Override
    public List<Double> getDataToPointsDistance(List<Double> dataList, List<List<Double>> pointList) {
        return toChebyshevDistance(dataList, pointList);
    }

    @Override
    public Double getClusterInstanceOrderDistance(List<Double> dataList, List<Double> points) {
        return toManhattanDistanceOrder(dataList, points);
    }

    @Override
    public Double getAmongSampleDistance(List<Double> dataList1, List<Double> dataList2) {
        List<Double> list = new ArrayList<Double>();
        int size = dataList2.size();
        for (int i = 0; i < size; i++) {
            Double data1 = dataList1.get(i);
            Double data2 = dataList2.get(i);
            double v = Math.abs(data1 - data2);
            list.add(v);
        }
        // 获取最大值
        return list.stream().mapToDouble(item -> item).max().getAsDouble();
    }

    @Override
    public Map<Integer, Double> getInitCentroidsDistance(List<Double> firstPoints, List<List<Double>> dataList) {
        Map<Integer,Double> distanceMap = new HashMap<>();
        for (int i = 0; i < dataList.size(); i++) {

            List<Double> list = new ArrayList<>();

            List<Double> dataItems = dataList.get(i);
            for (int j = 0; j < dataItems.size(); j++) {
                Double firstPoint = firstPoints.get(j);
                Double data = dataItems.get(j);
                double v = Math.abs(firstPoint - data);
                list.add(v);
            }
            double max = list.stream().mapToDouble(item -> item).max().getAsDouble();
            distanceMap.put(i,max);
        }
        return distanceMap;
    }


    /**
     * 通过切比雪夫距离算法实现最小误差的计算
     *
     * @param dataList
     *        元数据集合
     * @param pointList
     *        中心点集合
     */
    private List<Double> toChebyshevDistance(List<Double> dataList, List<List<Double>> pointList){
        List<Double> distanceList = new ArrayList<>();
        for (List<Double> points : pointList) {
            List<Double> distances = new ArrayList<>();
            for (int i = 0; i < dataList.size(); i++) {
                Double point = points.get(i);
                Double data = dataList.get(i);
                distances.add(Math.abs(data-point));
            }
            // 获取最大值
            Double max = distances.stream().mapToDouble(item -> item).max().getAsDouble();
            distanceList.add(max);
        }
        return distanceList;
    }


    /**
     * 通过切比雪夫距离算法实现最小误差的计算
     *
     * @param dataList
     *        元数据
     */
    private Double toManhattanDistanceOrder(List<Double> dataList,List<Double> points){
        List<Double> distances = new ArrayList<>();
        for (int i = 0; i < dataList.size(); i++) {
            Double point = points.get(i);
            Double data = dataList.get(i);
            distances.add(Math.abs(data-point));
        }
        Double max = distances.stream().mapToDouble(item -> item).max().getAsDouble();
        return max;
    }
}