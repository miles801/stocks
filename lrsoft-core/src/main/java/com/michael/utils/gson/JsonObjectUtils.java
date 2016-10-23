package com.michael.utils.gson;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.commons.lang3.StringUtils;

/**
 * @author miles
 * @datetime 2014/6/30 13:35
 */
public class JsonObjectUtils {
    /**
     * 从JsonObject中取出指定名称的字符串属性的值
     */
    public static String getStringProperty(JsonObject object, String property) {
        JsonElement element = object.get(property);
        if (element == null) {
            return null;
        }
        return element.getAsString();
    }

    /**
     * 实例化一个JsonObject，并添加一个键值对
     *
     * @param key   不能为空，否则抛出异常
     * @param value key的字符串值
     */
    public static JsonObject addStringProperty(String key, String value) {
        if (StringUtils.isEmpty(key)) {
            throw new IllegalArgumentException("给JsonObject添加属性时,key不能为空!");
        }
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(key, value);
        return jsonObject;
    }
}
