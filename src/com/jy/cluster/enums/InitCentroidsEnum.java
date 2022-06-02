package com.jy.cluster.enums;

import java.util.Objects;

/**
 * 初始化质心算法枚举
 *
 * @author jinchunzhao
 * @version 1.0
 * @date 2022-01-31 10:58
 */
public enum InitCentroidsEnum {

    /**
     * 初始化质心算法枚举
     */
    RANDOM("random"), KMEANS_PLUS("kmeans_plus"), FARTHEST_FIRST("farthest_first");

    private final String code;

    InitCentroidsEnum(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static InitCentroidsEnum machCode(String code){
        InitCentroidsEnum[] centroidsEnums = InitCentroidsEnum.values();
        for (InitCentroidsEnum centroidsEnum : centroidsEnums) {
            if (Objects.equals(centroidsEnum.getCode(),code)){
                return centroidsEnum;
            }
        }
        return null;
    }


}
