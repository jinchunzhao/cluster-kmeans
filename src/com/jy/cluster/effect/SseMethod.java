package com.jy.cluster.effect;


import java.util.List;
import java.util.Objects;

/**
 * SSE算法
 *
 * @author jinchunzhao
 * @version 1.0
 * @date 2022-02-03 14:21
 */
public class SseMethod {

    /**
     * 获取肘算法实例
     *
     * @return ElbowMethod
     */
    public static SseMethod getInstance(){
        return new SseMethod();
    }

    /**
     * 根据SSE算法计算误差的平方和
     *
     * @param dataList
     *        数据
     * @param points
     *        质心
     * @return
     *        误差平方和
     */
    public Double getSquaredSumError(List<Double> dataList,List<Double> points){
        if (Objects.isNull(points) || points.size() == 0){
            return Double.NaN;
        }
        double sum = 0.00;
        for (int i = 0; i < dataList.size(); i++) {
            Double data = dataList.get(i);
            Double point = points.get(i);
            sum += Math.pow(data-point, 2);
        }
        return sum;
    }


    /**
     * 求得集合元素之和
     *
     * @param squaredSum
     *        平方和
     *
     * @return
     *        集合元素之和
     */
    public Double listElementSum(List<Double> squaredSum){
        return squaredSum.stream().reduce(0.00, Double::sum);
    }
}
