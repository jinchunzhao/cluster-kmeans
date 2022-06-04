package com.jy.cluster.effect;

import com.jy.cluster.distance.DistanceFunctionHandler;
import com.jy.cluster.utils.ListUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * 轮廓系数：确定聚类效果好坏
 *
 * 轮廓系数接近 1，则说明样本 i 聚类合理;
 * 轮廓系数接近-1，则说明样本 i 更应该分类到另外的簇；
 * 轮廓系数近似为 0 ， 则说明样本 i 在两个簇的边界上
 *
 * @author jinchunzhao
 * @version 1.0
 * @date 2022-06-02 19:37
 */
public class SilhouetteCoefficient {

    /**
     * 获取轮廓系数算法实例
     *
     * @return SilhouetteCoefficient
     */
    public static SilhouetteCoefficient getInstance() {
        return new SilhouetteCoefficient();
    }


    /**
     * 获取轮廓系数
     *
     * @param distanceHandler
     *        距离算法执行器
     * @param clusterDataMap
     *        分簇数据Map
     * @return
     *        轮廓系数
     */
    public Double getSilhouetteScore(DistanceFunctionHandler distanceHandler, Map<Integer, List<List<Double>>> clusterDataMap) {
        List<Double> silhouetteScores = new ArrayList<>();
        for (Map.Entry<Integer, List<List<Double>>> entry : clusterDataMap.entrySet()) {
            Integer pointIndex = entry.getKey();
            List<List<Double>> dataList = entry.getValue();
            List<Double> oneClusterSilhouettes = new ArrayList<>();
            for (int i = 0; i < dataList.size(); i++) {
                List<Double> items1 = dataList.get(i);
                double clusterIn = getClusterInAvgDistance(i, dataList, items1, distanceHandler);
                double clusterOut = getClusterOutAvgDistance(pointIndex, clusterDataMap, items1, distanceHandler);
                double silhouette = (clusterOut - clusterIn) / Double.max(clusterOut, clusterIn);
                if (Objects.equals(silhouette, Double.NaN) || Objects.equals(silhouette, Double.NEGATIVE_INFINITY) || Objects.equals(silhouette, Double.POSITIVE_INFINITY)) {
                    silhouette = 0.0;
                }
                oneClusterSilhouettes.add(silhouette);
            }
            Double sum = oneClusterSilhouettes.stream().reduce(0.00, Double::sum);
            BigDecimal average = new BigDecimal(sum / oneClusterSilhouettes.size());
            double clusterAverage = average.setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
            silhouetteScores.add(clusterAverage);
        }
        Double sum = silhouetteScores.stream().reduce(0.00, Double::sum);
        BigDecimal average = new BigDecimal(sum / silhouetteScores.size());
        double silhouettesScore = average.setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
        return silhouettesScore;
    }

    /**
     * 获取样本到同簇其它样本的平均距离
     *
     * @param dataIndex       样本数据下标
     * @param dataList        同簇数据
     * @param items1          样本
     * @param distanceHandler 距离算法执行器
     * @return 样本到同簇其它样本的平均距离
     */
    public Double getClusterInAvgDistance(int dataIndex, List<List<Double>> dataList, List<Double> items1, DistanceFunctionHandler distanceHandler) {
        List<Double> clusterIns = new ArrayList<>();
        List<List<Double>> dataList2 = removeOwnDataList(dataIndex, dataList);

        dataList2.forEach(items2 -> {
            Double silhouetteScore = distanceHandler.getAmongSampleDistance(items1, items2);
            clusterIns.add(silhouetteScore);
        });

//                List<Double> list1 = clusterIns.stream().map(item ->{
//                    BigDecimal clusterOutAverage = new BigDecimal(item / clusterIns.size());
//                    double v = clusterOutAverage.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
//                    return v;
//                }).collect(Collectors.toList());
//                double clusterIn = list1.stream().mapToDouble(item->item).min().getAsDouble();
        if (clusterIns.size() <= 0){
            return 0.0;
        }
        Double sumIn = clusterIns.stream().reduce(0.00, Double::sum);
        BigDecimal clusterInAverage = new BigDecimal(sumIn / clusterIns.size());
        double clusterIn = clusterInAverage.setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
        return clusterIn;
    }

    /**
     * 获取样本到其它某簇的所有样本的平均距离
     *
     * @param pointIndex      中心点（簇）下标
     * @param clusterDataMap  分簇数据Map
     * @param items1          样本
     * @param distanceHandler 距离算法执行器
     * @return 样本到其它某簇的所有样本的平均距离
     */
    private Double getClusterOutAvgDistance(Integer pointIndex, Map<Integer, List<List<Double>>> clusterDataMap, List<Double> items1, DistanceFunctionHandler distanceHandler) {
        List<Double> clusterOuts = new ArrayList<>();
        List<Integer> indexSet = removeOwnClusterDataMap(pointIndex,clusterDataMap);
        for (int j = 0; j < indexSet.size(); j++) {
            Integer index = indexSet.get(j);
            List<List<Double>> lists = clusterDataMap.get(index);
            lists.forEach(item2s -> {
                Double silhouetteScore = distanceHandler.getAmongSampleDistance(items1, item2s);
                clusterOuts.add(silhouetteScore);
            });
        }
//                List<Double> list2 = clusterOuts.stream().map(item ->{
//                    BigDecimal clusterOutAverage = new BigDecimal(item / clusterOuts.size());
//                    double v = clusterOutAverage.setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
//                    return v;
//                }).collect(Collectors.toList());
//                double clusterOut = list2.stream().mapToDouble(item->item).min().getAsDouble();
        Double sumOut = clusterOuts.stream().reduce(0.00, Double::sum);
        BigDecimal clusterOutAverage = new BigDecimal(sumOut / clusterOuts.size());
        double clusterOut = clusterOutAverage.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        return clusterOut;
    }


    /**
     * 移除自身的数据
     *
     * @param ownIndex 自身数据下标
     * @param dataList 数据
     * @return 移除自身后的数据
     */
    private List<List<Double>> removeOwnDataList(int ownIndex, List<List<Double>> dataList) {
        List<List<Double>> lists = ListUtil.deepCopy(dataList);
        lists.remove(ownIndex);
        return lists;
    }

    /**
     * 移除自身簇中的数据
     *
     * @param ownIndex 自身簇map-key
     * @return 移除自身簇map-key后的map-key
     */
    private List<Integer> removeOwnClusterDataMap(Integer ownIndex, Map<Integer, List<List<Double>>> clusterDataMap) {
        Set<Integer> set = clusterDataMap.keySet();
        List<Integer> list = new ArrayList<>(set);
        list.remove(ownIndex);
        return list;
    }

}
