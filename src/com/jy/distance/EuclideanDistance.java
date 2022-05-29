package com.jy.distance;

import com.jy.plugin.ClusterArithmeticRegion;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 欧氏距离算法执行器接口实现类
 *
 * @author jinchunzhao
 * @version 1.0
 * @date 2022-02-03 10:55
 */
@ClusterArithmeticRegion(factionType = "euclideanDistance")
public class EuclideanDistance implements DistanceFunctionHandler {


    @Override
    public List<Double> toDistance(List<Double> dataList, List<List<Double>> pointList) {
        return toEuclideanDistance(dataList, pointList);
    }

    @Override
    public Double toClusterInstanceOrder(List<Double> dataList, List<Double> points) {
        return toEuclideanDistanceOrder(dataList, points);
    }


    /**
     * 通过欧式距离算法实现最小误差的计算
     *
     * @param dataList
     *        元数据集合
     * @param pointList
     *        中心点集合
     */
    private List<Double> toEuclideanDistance(List<Double> dataList, List<List<Double>> pointList){

        List<Double> distanceList = new ArrayList<>();

        for (List<Double> points : pointList) {
            List<Double> distancePows = new ArrayList<>();
            for (int i = 0; i < dataList.size(); i++) {
                Double point = points.get(i);
                Double data = dataList.get(i);
                distancePows.add(euclideanDistancePow(data, point));
            }
            // 数据求和
            Double reduce = listElementSum(distancePows);
            distanceList.add(euclideanDistancesSqrt(reduce));
        }
        return distanceList;
    }


    /**
     * 通过欧式距离算法实现最小误差的计算
     *
     * @param dataList
     *        元数据集合
     */
    private Double toEuclideanDistanceOrder(List<Double> dataList,List<Double> points){
        List<Double> distancePows = new ArrayList<>();
        for (int i = 0; i < dataList.size(); i++) {
            Double point = points.get(i);
            Double data = dataList.get(i);
            distancePows.add(euclideanDistancePow(data, point));
        }
        // 数据求和
        Double aDouble = listElementSum(distancePows);
        return euclideanDistancesSqrt(aDouble);
    }

    /**
     * 欧式距离：(x-y)的平方算法
     *
     * @param data
     *        数据
     * @param point
     *        中心点
     * @return
     *        计算的结果
     */
    private Double euclideanDistancePow(Double data,Double point){
        double result = Math.pow(data, 2) - 2 * data * point + Math.pow(point, 2);
        return new BigDecimal(result).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 欧式距离：平方根
     *
     * @param reduce
     *        数据
     * @return
     *        计算的结果
     */
    private Double euclideanDistancesSqrt(Double reduce){
        return BigDecimal.valueOf(Math.sqrt(Math.abs(reduce))).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
    }


}
