package com.jy.enums;

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

}
