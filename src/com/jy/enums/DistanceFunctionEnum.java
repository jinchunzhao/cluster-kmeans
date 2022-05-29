package com.jy.enums;

import java.util.Objects;

/**
 * 距离算法方法枚举类
 *
 * @author jinchunzhao
 * @version 1.0
 * @date 2022-01-31 10:51
 */
public enum DistanceFunctionEnum {


    /**
     * 聚类分析距离算法
     */
    EUCLIDEAN_DISTANCE("euclideanDistance"),
    MANHATTAN_DISTANCE("manhattanDistance"),
    COS_DISTANCE("cosDistance");

    private final String code;

    DistanceFunctionEnum(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    /**
     * 根据code匹配枚举
     *
     * @param code
     *        code
     * @return
     *        枚举
     */
    public static DistanceFunctionEnum machCode(String code){
        for (DistanceFunctionEnum distanceAlgorithmEnum : DistanceFunctionEnum.values()){
            if (Objects.equals(distanceAlgorithmEnum.getCode(), code)){
                return distanceAlgorithmEnum;
            }
        }
        return null;
    }
}
