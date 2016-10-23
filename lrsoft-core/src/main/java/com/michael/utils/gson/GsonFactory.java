package com.michael.utils.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Date;

/**
 * Created by miles on 13-12-10.
 * gson工厂对象，负责产生各种Gson对象
 */
public class GsonFactory {
    /**
     * 构建一个普通的gson对象（包含对时间的转换）
     *
     * @return
     */
    public static Gson build() {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Date.class, new DateConverter());//时间转换器
        return builder.create();
    }

}
