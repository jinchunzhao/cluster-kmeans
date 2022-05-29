package com.jy.distance;

import java.util.List;

/**
 * 距离方法执行器接口
 *
 * @author jinchunzhao
 * @version 1.0
 * @date 2022-02-03 10:59
 */
public interface DistanceFunctionHandler {


    /**
     * List集合中元素之和
     *
     * @param Doubles
     *        集合
     * @return
     *        计算的结果
     */
    default Double listElementSum(List<Double> Doubles){
        return Doubles.stream().reduce(0.00, Double::sum);
    }
    /**
     * 通过曼哈顿距离算法实现最小误差的计算
     *
     * @param dataList
     *        元数据
     * @param pointList
     *        中心点集合
     */
    List<Double> toDistance(List<Double> dataList, List<List<Double>> pointList);

    /**
     * 根据距离算法将根据数据离质心的距离远近进行排序。距离近的排到前面
     *
     * @param dataList
     *        每一条数据
     * @param points
     *        质心点
     * @return
     *        最小误差
     */
    Double toClusterInstanceOrder(List<Double> dataList,List<Double> points);
}
