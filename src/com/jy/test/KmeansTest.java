package com.jy.test;

import com.jy.ClusterSimpleKm;
import com.jy.enums.DistanceFunctionEnum;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 测试类
 * 
 * @author jinchunzhao
 * @version 1.0
 * @date 2022-05-29 11:08
 */
public class KmeansTest {

    private static final String FILE_PATH = System.getProperty("user.dir") + "\\src\\com\\jy\\test\\data.csv";
    /**
     * 读取文件中的数据存入map中(  位数不够的化，补0)
     */
    private static List<List<Double>> loadData() {

        List<List<Double>> dataList = new ArrayList<>();

        BufferedReader br = null;
        String line;
        String csvSplitBy = ",";
        try {
            br = new BufferedReader(new FileReader(FILE_PATH));
            while ((line = br.readLine()) != null) {
                // 分割点为
                List<String> post = Arrays.asList(line.split(csvSplitBy));

                if (Objects.nonNull(post) || post.size() > 0) {
                    List<Double> list = new ArrayList<>();
                    for (int i = 1; i < post.size(); i++) {
                        list.add(Double.valueOf(post.get(i)));
                    }

                    dataList.add(list);

                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return dataList;
        }
    }

    public static void main(String[] args) {
        String distanceType = DistanceFunctionEnum.COS_DISTANCE.getCode();
        Integer maxIterations = 100;
        int k = 5;


        List<List<Double>> dataList = loadData();
        ClusterSimpleKm clusterSimpleKm = ClusterSimpleKm.getInstance();

        clusterSimpleKm.setNumClusters(k);
        clusterSimpleKm.setMaxIterations(maxIterations);

        clusterSimpleKm.setDistanceCalcType(distanceType);
        clusterSimpleKm.setPreserveInstancesOrder(true);
//        clusterSimpleKm.setInitializationPointType(RANDOM);
//        clusterSimpleKm.setSeed(100);


        Map<String, Object> resultMap = clusterSimpleKm.callSimpleKmeansCluster(dataList);

        System.out.println("聚类后的数据：" + resultMap.toString());
    }

}
