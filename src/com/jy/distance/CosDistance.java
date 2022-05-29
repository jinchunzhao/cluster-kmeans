package com.jy.distance;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 余玄相似度算法执行器接口实现类
 *
 * @author jinchunzhao
 * @version 1.0
 * @date 2022-02-03 10:55
 */
public class CosDistance implements DistanceFunctionHandler {


    @Override
    public List<Double> toDistance(List<Double> dataList, List<List<Double>> pointList) {
        return toCosDistance(dataList, pointList);
    }

    @Override
    public Double toClusterInstanceOrder(List<Double> dataList, List<Double> points) {
        return toCosDistanceOrder(dataList, points);
    }


    /**
     * 通过余玄相似度算法实现最小误差的计算
     *
     * @param dataList
     *        元数据集合
     * @param pointList
     *        中心点集合
     */
    private List<Double> toCosDistance(List<Double> dataList, List<List<Double>> pointList){

        System.out.println(pointList);

        List<Double> distanceList = new ArrayList<>();
        for (List<Double> points : pointList) {
            if (Objects.isNull(points) || points.size() == 0){
                continue;
            }
            double sum1 = 0.00;
            double sum2 = 0.00;
            double sum3 = 0.00;
            for (int i = 0; i < dataList.size(); i++) {
                Double point = points.get(i);
                Double data = dataList.get(i);
                sum1 += point * data;
                sum2 += point * point;
                sum3 += data * data;
            }
            double result = sum1 / (Math.sqrt(sum2) + Math.sqrt(sum3));
            distanceList.add(cosDistancesSqrt(result));
        }

        return distanceList;
    }


    /**
     * 通过余玄相似度算法实现最小误差的计算
     *
     * @param dataList
     *        元数据集合
     */
    private Double toCosDistanceOrder(List<Double> dataList,List<Double> points){
        if (Objects.isNull(points) || points.size() == 0){
            return Double.NaN;
        }
        double sum1 = 0.00;
        double sum2 = 0.00;
        double sum3 = 0.00;
        for (int i = 0; i < dataList.size(); i++) {
            Double point = points.get(i);
            Double data = dataList.get(i);
            sum1 += point * data;
            sum2 += point * point;
            sum3 += data * data;
        }
        double result = sum1 / (Math.sqrt(sum2) + Math.sqrt(sum3));
        return cosDistancesSqrt(result);
    }

    /**
     * 余玄相似度：平方根
     *
     * @param reduce
     *        数据
     * @return
     *        计算的结果
     */
    private Double cosDistancesSqrt(Double reduce){
        return BigDecimal.valueOf(Math.sqrt(Math.abs(reduce))).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
    }


}
