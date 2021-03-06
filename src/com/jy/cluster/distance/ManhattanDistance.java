package com.jy.cluster.distance;

import com.jy.cluster.plugin.ClusterArithmeticRegion;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 曼哈顿距离算法执行器接口实现类
 *
 * @author jinchunzhao
 * @version 1.0
 * @date 2022-02-03 10:45
 */
@ClusterArithmeticRegion(factionType = "manhattanDistance")
public class ManhattanDistance implements DistanceFunctionHandler {

    @Override
    public List<Double> getDataToPointsDistance(List<Double> dataList, List<List<Double>> pointList) {
        return toManhattanDistance(dataList, pointList);
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

        Double sum = listElementSum(list);
        BigDecimal distanceAverage = new BigDecimal(sum / list.size());
        double distance = distanceAverage.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        return distance;
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
            // 数据求和
            distanceMap.put(i,listElementSum(list));
        }
        return distanceMap;
    }


    /**
     * 通过曼哈顿距离算法实现最小误差的计算
     *
     * @param dataList  元数据
     * @param pointList 中心点集合
     */
    private List<Double> toManhattanDistance(List<Double> dataList, List<List<Double>> pointList) {

        List<Double> distanceList = new ArrayList<>();
        for (List<Double> points : pointList) {
            List<Double> abs = new ArrayList<>();
            for (int i = 0; i < dataList.size(); i++) {
                Double point = points.get(i);
                Double data = dataList.get(i);
                abs.add(Math.abs(data - point));
            }
            // 数据求和
            Double reduce = listElementSum(abs);
            distanceList.add(reduce);
        }
        return distanceList;
    }

    /**
     * 通过曼哈顿距离算法实现最小误差的计算
     *
     * @param dataList 元数据
     */
    private Double toManhattanDistanceOrder(List<Double> dataList, List<Double> points) {
        List<Double> abs = new ArrayList<>();
        for (int i = 0; i < dataList.size(); i++) {
            Double point = points.get(i);
            Double data = dataList.get(i);
            abs.add(Math.abs(data - point));
        }
        // 数据求和
        return listElementSum(abs);
    }

}
