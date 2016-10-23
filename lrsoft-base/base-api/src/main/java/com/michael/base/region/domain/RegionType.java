package com.michael.base.region.domain;


/**
 * 行政区域的类型
 * Created by miles on 2014/3/26.
 */
public interface RegionType {
    /**
     * 国家
     */
    public static final int NATION = 0;
    /**
     * 省份/直辖市
     */
    public static final int PROVINCE = 1;
    /**
     * 城市
     */
    public static final int CITY = 2;
    /**
     * 县区
     */
    public static final int DISTRICT = 3;
    /**
     * 城镇
     */
    public static final int TOWN = 4;
    /**
     * 街道
     */
    public static final int STREET = 5;
}
