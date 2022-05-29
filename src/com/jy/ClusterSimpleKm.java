package com.jy;

import com.jy.centroids.InitCentroidsFactory;
import com.jy.centroids.InitCentroidsFunctionHandler;
import com.jy.distance.DistanceFunctionFactory;
import com.jy.distance.DistanceFunctionHandler;
import com.jy.elbow.SseMethod;
import com.jy.enums.DistanceFunctionEnum;
import com.jy.enums.InitCentroidsEnum;
import com.jy.utils.ListUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.OptionalInt;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


/**
 * 聚类算法-简单的k均值算法
 * 可以控制距离算法类型：欧式距离算法、曼哈顿距离算法
 *
 * @author jinchunzhao
 * @version 1.0
 * @date 2022-01-31 17:03
 */
public class ClusterSimpleKm {
    /**
     * 聚类个数
     */
    private Integer numClusters;

    /**
     * 最大迭代次数
     */
    private Integer maxIterations = 500;

    /**
     * 距离计算类型
     */
    private String distanceCalcType = DistanceFunctionEnum.EUCLIDEAN_DISTANCE.getCode();

    /**
     * 初始化质心算法类型
     */
    private String initCentroidsType = InitCentroidsEnum.RANDOM.getCode();

    /**
     * 随机数种子.种子固定后，即使是随机算法也可以确定性地运行。默认为：10
     */
    private Integer seed = 10;

    /**
     * 初始化中心点方法类型。默认为：随机
     */
    private Integer initializationPointType = 0;

    /**
     * 是否对簇中实例进行排序（按距离质心的远近）
     */
    private boolean preserveInstancesOrder = false;

    /**
     * 平方和误差
     */
    private Double squaredSumError;

    public Integer getNumClusters() {
        return numClusters;
    }

    public void setNumClusters(Integer numClusters) {
        this.numClusters = numClusters;
    }

    public Integer getMaxIterations() {
        return maxIterations;
    }

    public void setMaxIterations(Integer maxIterations) {
        this.maxIterations = maxIterations;
    }

    public String getDistanceCalcType() {
        return distanceCalcType;
    }

    public void setDistanceCalcType(String distanceCalcType) {
        this.distanceCalcType = distanceCalcType;
    }

    public String getInitCentroidsType() {
        return initCentroidsType;
    }

    public void setInitCentroidsType(String initCentroidsType) {
        this.initCentroidsType = initCentroidsType;
    }

    public Integer getInitializationPointType() {
        return initializationPointType;
    }

    public void setInitializationPointType(Integer initializationPointType) {
        this.initializationPointType = initializationPointType;
    }

    public Integer getSeed() {
        return seed;
    }

    public void setSeed(Integer seed) {
        this.seed = seed;
    }

    public boolean isPreserveInstancesOrder() {
        return preserveInstancesOrder;
    }

    public void setPreserveInstancesOrder(boolean preserveInstancesOrder) {
        this.preserveInstancesOrder = preserveInstancesOrder;
    }

    /**
     * 私有化无参构造函数
     */
    private ClusterSimpleKm() {

    }

    /**
     * 构建ClusterSimpleKm实例
     *
     * @return ClusterSimpleKm实例
     */
    public static ClusterSimpleKm getInstance() {
        return new ClusterSimpleKm();
    }

    /**
     * 初始化簇中心点
     */
    private static final List<List<Double>> INIT_POINTS = new ArrayList<>();

    /**
     * 簇中心点缓存
     */
    private static final List<List<Double>> POINTS_CACHE = new ArrayList<>();

    /**
     * 历史簇中心点缓存
     */
    private static final List<List<Double>> POINTS_HISTORY_CACHE = new ArrayList<>();

    /**
     * 聚类数据
     * key:中心点下标，value:该中心点的聚类数据
     */
    private static final Map<Integer, List<List<Double>>> CLUSTER_DATA_MAP = new HashMap<>();


    /**
     * 初始化中心点
     *
     * @param dataList 元数据
     */
    private void initCenterPoint(List<List<Double>> dataList) {

        int dataSize = dataList.size();
        if (dataSize <= 0) {
            dataSize = 1;
        }
        if (Objects.isNull(this.numClusters) || this.numClusters == 0) {
            throw new RuntimeException("Param ClusterNum must be positive integer");
        }

        if (this.numClusters > dataSize) {
            throw new RuntimeException("簇数K的值不能多于数据集的元素数量!");
        }
        InitCentroidsFactory centroidsFactory = InitCentroidsFactory.getInstance();
        InitCentroidsFunctionHandler invokeHandler = centroidsFactory.getHandler(this.getInitCentroidsType());
        List<List<Double>> initCentroids = invokeHandler.initCentroids(dataList, this.seed, this.numClusters, CLUSTER_DATA_MAP);

        POINTS_CACHE.addAll(initCentroids);
        INIT_POINTS.addAll(initCentroids);
    }

    /**
     * 执行K-means聚类算法
     *
     * @param dataList 元数据
     */
    private void processKm(List<List<Double>> dataList) {

        int iterationCount = 0;
        DistanceFunctionFactory instance = DistanceFunctionFactory.getInstance();
        DistanceFunctionHandler invokeHandler = instance.getHandler(this.distanceCalcType);

        SseMethod sesMethod = SseMethod.getInstance();
        // 自旋迭代
        Boolean isContinueToCluster = true;
        while (isContinueToCluster) {
            iterationCount++;
            clearClusterDataMap();

            List<Double> squaredSumErrors = new ArrayList<>();
            for (List<Double> Doubles : dataList) {
                squaredSumErrors.add(dataClassify(Doubles, invokeHandler, sesMethod));
            }
            if (iterationCount >= this.maxIterations) {
                this.squaredSumError = sesMethod.listElementSum(squaredSumErrors);

                break;
            }
            //计算新的中心点。
            getNewPoint();
            isContinueToCluster = continueToCluster();
            if (!isContinueToCluster) {
                this.squaredSumError = sesMethod.listElementSum(squaredSumErrors);
            }
        }
        orderClusterInstances(invokeHandler);

        this.maxIterations = iterationCount;
    }

    /**
     * 对每一簇中的实例进行排序
     *
     * @param invokeHandler 距离算法执行器
     */
    private void orderClusterInstances(DistanceFunctionHandler invokeHandler) {
        if (!this.preserveInstancesOrder) {
            return;
        }
        Map<Integer, List<List<Double>>> clusterDataMap = new HashMap<>();
        for (int i = 0; i < POINTS_CACHE.size(); i++) {
            List<Double> points = POINTS_CACHE.get(i);
            List<List<Double>> clusterDataList = CLUSTER_DATA_MAP.get(i);
            Map<Integer, Double> orderItemMap = new HashMap<>();
            // 根据不同类型的距离算法计算最小误差
            for (int j = 0; j < clusterDataList.size(); j++) {
                List<Double> dataList = clusterDataList.get(j);
                Double f = invokeHandler.toClusterInstanceOrder(dataList, points);
                orderItemMap.put(j, f);
            }
            List<Map.Entry<Integer, Double>> orderList = orderItemMap.entrySet().stream()
                    .sorted((entry1, entry2) -> entry1.getValue().compareTo(entry2.getValue()))
                    .collect(Collectors.toList());

            List<List<Double>> clusters = new ArrayList<>();
            for (Map.Entry<Integer, Double> entry : orderList) {
                List<Double> Doubles = clusterDataList.get(entry.getKey());
                clusters.add(Doubles);
            }
            clusterDataMap.put(i, clusters);
        }
        clearClusterDataMap();
        CLUSTER_DATA_MAP.putAll(clusterDataMap);
    }

    /**
     * 对数据进行分类处理
     *
     * @param dataList  元数据
     * @param handler   最小误差距离算法
     * @param sseMethod SSE算法实例
     */
    private Double dataClassify(List<Double> dataList, DistanceFunctionHandler handler, SseMethod sseMethod) {

        List<Double> distanceList = handler.toDistance(dataList, POINTS_CACHE);

        // 获取集合中最新值的下标
        OptionalInt optionalInt = IntStream.range(0, distanceList.size())
                .reduce((i, j) -> distanceList.get(i) > distanceList.get(j) ? j : i);
        Double squaredSumError = null;
        if (optionalInt.isPresent()) {
            // 进行数据分类处理
            int asInt = optionalInt.getAsInt();
            List<List<Double>> clusters = CLUSTER_DATA_MAP.get(optionalInt.getAsInt());
            clusters.add(dataList);
            squaredSumError = sseMethod.getSquaredSumError(dataList, POINTS_CACHE.get(asInt));
        }
        return squaredSumError;
    }

    /**
     * 清理聚类数据
     */
    private void clearClusterDataMap() {
        for (Map.Entry<Integer, List<List<Double>>> entry : CLUSTER_DATA_MAP.entrySet()) {
            entry.getValue().clear();
        }

    }

    /**
     * 是否继续迭代聚类
     *
     * @return true/false
     */
    private Boolean continueToCluster() {
        for (int i = 0; i < POINTS_HISTORY_CACHE.size(); i++) {
            List<Double> lastPoints = POINTS_HISTORY_CACHE.get(i);
            if (Objects.isNull(lastPoints) || lastPoints.size() == 0) {
                continue;
            }
            List<Double> lastButOnePoints = POINTS_CACHE.get(i);
            for (int j = 0; j < lastButOnePoints.size(); j++) {
                Double lastButOnePoint = lastButOnePoints.get(j);
                Double lastPoint = lastPoints.get(j);
                if (!Objects.equals(lastButOnePoint, lastPoint)) {
                    POINTS_HISTORY_CACHE.clear();
                    return Boolean.TRUE;
                }
            }
        }
        POINTS_HISTORY_CACHE.clear();
        return Boolean.FALSE;
    }

    /**
     * 计算新的中心点
     * 求数据的平均值
     */
    private void getNewPoint() {

        //收集历史中心点信息
        POINTS_HISTORY_CACHE.addAll(ListUtil.deepCopy(POINTS_CACHE));
        POINTS_CACHE.clear();

        for (Map.Entry<Integer, List<List<Double>>> entry : CLUSTER_DATA_MAP.entrySet()) {
            List<List<Double>> dataList = entry.getValue();
            List<Double> newPoints = new ArrayList<>();

            for (List<Double> valueList : dataList) {
                if (Objects.isNull(valueList) || valueList.size() == 0) {
                    continue;
                }

                initNewPoint(newPoints, valueList);
                for (int k = 0; k < valueList.size(); k++) {
                    newPoints.set(k, newPoints.get(k) + valueList.get(k));
                }
            }
            // 求平均值得到新的中心点
            for (int k = 0; k < newPoints.size(); k++) {
                double v = newPoints.get(k) / dataList.size();
                Double v1 = new BigDecimal(v).setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
                newPoints.set(k, v1);
            }
//            if (CollectionUtils.isEmpty(newPoints)) {
//                continue;
//            }
            POINTS_CACHE.add(newPoints);
        }
    }

    /**
     * 初始化新中心点数据
     *
     * @param newPoints 新中心点集合
     * @param dataList  数据集合
     */
    private void initNewPoint(List<Double> newPoints, List<Double> dataList) {

        if (newPoints.size() > 0) {
            return;
        }
        int size = dataList.size();
        for (int i = 0; i < size; i++) {
            newPoints.add(0.00);
        }
    }


    /**
     * 简单K-means聚类算法运用
     *
     * @param dataList 元数据
     * @return 聚簇的数据集合
     */
    public Map<String, Object> callSimpleKmeansCluster(List<List<Double>> dataList) {
        initCenterPoint(dataList);
        processKm(dataList);

        Map<String, Object> resultMap = new LinkedHashMap<>();
        resultMap.put("numClusters", this.numClusters);
        resultMap.put("numIterations", this.maxIterations);
        resultMap.put("squaredSumError", this.squaredSumError);
        resultMap.put("initCentroids", this.initCentroidsType);
        resultMap.put("distanceCalcType",this.distanceCalcType);
        resultMap.put("initialStartPoints", INIT_POINTS);
        resultMap.put("clusterCentroids", POINTS_CACHE);
        resultMap.put("clusterData", CLUSTER_DATA_MAP);

        return resultMap;
    }
}
