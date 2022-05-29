package com.jy.distance;

import com.jy.plugin.ClusterArithmeticRegion;

import java.util.ArrayList;
import java.util.List;

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
    public List<Double> toDistance(List<Double> dataList, List<List<Double>> pointList) {
        return toManhattanDistance(dataList, pointList);
    }

    @Override
    public Double toClusterInstanceOrder(List<Double> dataList, List<Double> points) {
        return toManhattanDistanceOrder(dataList, points);
    }


    /**
     * 通过曼哈顿距离算法实现最小误差的计算
     *
     * @param dataList
     *        元数据
     * @param pointList
     *        中心点集合
     */
    private List<Double> toManhattanDistance(List<Double> dataList,  List<List<Double>> pointList){

        List<Double> distanceList = new ArrayList<>();
        for (List<Double> points : pointList) {
            List<Double> abs = new ArrayList<>();
            for (int i = 0; i < dataList.size(); i++) {
                Double point = points.get(i);
                Double data = dataList.get(i);
                abs.add(Math.abs(data-point));
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
     * @param dataList
     *        元数据
     */
    private Double toManhattanDistanceOrder(List<Double> dataList,List<Double> points){
        List<Double> abs = new ArrayList<>();
        for (int i = 0; i < dataList.size(); i++) {
            Double point = points.get(i);
            Double data = dataList.get(i);
            abs.add(Math.abs(data-point));
        }
        // 数据求和
        return listElementSum(abs);
    }

}
