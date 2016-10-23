package com.michael.utils.gson;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.michael.core.context.SecurityContext;
import com.michael.utils.string.StringUtils;
import org.apache.commons.io.IOUtils;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.InvalidParameterException;
import java.util.*;

/**
 * gson的工具类
 *
 * @author miles
 */
public class GsonUtils {

    /**
     * 当从request.getInputStream()中读取了数据后，将其数据以字节数组的形式缓存到request的属性中
     */
    public static final String CACHED_INPUT_STREAM_DATA = "_CachedInputStreamData";

    /**
     * 将一个对象转成json字符串
     *
     * @param obj
     * @return
     */
    public static String toJson(Object obj) {
        return toJsonExclude(obj);
    }

    public static <T> T fromJson(String json, Class<T> clazz) {
        Gson gson = createGson(null, null);
        return gson.fromJson(json, clazz);
    }

    /**
     * 将一个对象转成json字符串并指定需要排除的属性名称列表
     * 如果没有指定属性名称集合，则将会全部转换
     * 默认时间会以时间戳的格式进行转换
     *
     * @param obj
     * @param exclusionFields
     * @return String
     */
    public static String toJsonExclude(Object obj, String... exclusionFields) {
        validateJsonObject(obj);
        //创建GsonBuilder
        Gson gson = createGson(null, exclusionFields);
        return gson.toJson(obj);
    }

    private static Gson createGson(String[] inclusionFields, String[] exclusionFields) {
        GsonBuilder builder = new GsonBuilder();

        //设置时间格式
        builder.registerTypeAdapter(Date.class, new DateConverter());
        builder.registerTypeAdapter(java.sql.Date.class, new DateConverter());
//        builder.registerTypeAdapterFactory(HibernateProxyNullAdapter.FACTORY);
        // 如果是手机端则转换空
        if (SecurityContext.get() != null && SecurityContext.get().isMobile()) {
            builder.serializeNulls();
        }
        //设置需要被排除的属性列表
        if (exclusionFields != null && exclusionFields.length > 0) {
            GsonExclusion gsonFilter = new GsonExclusion();
            gsonFilter.addExclusionField(exclusionFields);
            builder.setExclusionStrategies(gsonFilter);
        }

        //设置需要转换的属性名称
        if (inclusionFields != null && inclusionFields.length > 0) {
            GsonInclusion gsonFilter = new GsonInclusion();
            gsonFilter.addInclusionFields(inclusionFields);
            builder.setExclusionStrategies(gsonFilter);
        }
        //创建Gson并进行转换
        return builder.create();
    }

    /**
     * 将一个对象转成json字符串并指定需要需要转换的属性名称列表
     * 如果没有指定属性名称集合，则将会全部转换
     * 默认时间会以yyyy-MM-dd HH:mm:ss的格式进行转换
     *
     * @param obj
     * @param includeFields
     * @return
     */
    public static String toJsonInclude(Object obj, String... includeFields) {
        validateJsonObject(obj);

        //创建Gson并进行转换
        Gson gson = createGson(includeFields, null);
        return gson.toJson(obj);
    }


    /**
     * 通过response输出json数据
     *
     * @param response HttpServletResponse对象
     * @param json     json字符串
     */
    public static void printJson(HttpServletResponse response, String json) {
        PrintWriter writer = null;
        try {
            response.setContentType("application/json");
            writer = response.getWriter();
            writer.print(json);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    public static Map<String, Object> convertJson2Map(String json) {
        if (json == null) return null;
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(json, HashMap.class);
    }

    public static void printJson(HttpServletResponse response, String key, String value) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(key, value);
        printJson(response, jsonObject.toString());
    }

    /**
     * 将一个集合转成{data:[]}形式的对象
     *
     * @param response
     * @param data
     */
    public static void printCollectionJson(HttpServletResponse response, List data, String... includes) {
        String json = toCollectionJson(data, includes);
        printJson(response, json);
    }

    /**
     * 将一个集合转成{data:[]}形式的字符串
     *
     * @param data
     * @return
     */
    public static String toCollectionJson(List data, String... includes) {
        Map<String, List> map = new HashMap<String, List>();
        map.put("data", data);
        return toJsonInclude(map, includes);
    }

    public static void printJson(HttpServletResponse response, String key, Integer value) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(key, value);
        printJson(response, jsonObject.toString());
    }

    public static void printJson(HttpServletResponse response, String key, Float value) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(key, value);
        printJson(response, jsonObject.toString());
    }

    public static void printJson(HttpServletResponse response, String key, Double value) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(key, value);
        printJson(response, jsonObject.toString());
    }

    public static void printJson(HttpServletResponse response, String key, Boolean value) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(key, value);
        printJson(response, jsonObject.toString());
    }

    /**
     * 通过response输出json数据
     *
     * @param response HttpServletResponse对象
     * @param obj      object
     */
    public static void printJsonObject(HttpServletResponse response, Object obj) {
        if (obj == null) return;
        if (obj instanceof String) {
            printJson(response, (String) obj);
            return;
        }
        String json = toJson(obj);
        printJson(response, json);
    }

    private static void validateJsonObject(Object obj) {
        if (obj == null) {
            throw new NullPointerException("要转成json的对象不能为空！");
        }
        if (obj instanceof String || obj instanceof Number || obj instanceof Boolean) {
            throw new RuntimeException("要转成json字符串的必须是复杂(引用)类型的对象！");
        }
    }

    /**
     * 从request body中取出数据流（json字符串），并进行utf-8转码后，使用Gson转换成指定类型的对象
     * 如果request、clazz为空，则抛出异常
     *
     * @param request                  包含请求体的request对象
     * @param clazz                    要转成的对象的类型
     * @param excludeFields（可选，要排除的字段）
     * @param <T>
     * @return clazz指定的类型
     */
    public static <T> T wrapDataToEntity(HttpServletRequest request, Class<T> clazz, String... excludeFields) {
        if (request == null || clazz == null) {
            throw new InvalidParameterException("参数不能为空！");
        }

        // 创建gson对象
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Date.class, new DateConverter());   // 注册时间转换器

        if (excludeFields != null && excludeFields.length > 0) {        // 要排除的字段
            GsonExclusion exclusions = new GsonExclusion();
            exclusions.addExclusionField(excludeFields);
            builder.setExclusionStrategies(exclusions);
        }
        Gson gson = builder.create();

        // 根据请求类型分别解析
        String method = request.getMethod();
        String data = null;
        try {
            // 从流中获取数据
            ServletInputStream inputStream = request.getInputStream();
            if (inputStream != null) {
                data = IOUtils.toString(inputStream, "utf-8");
            }
            if (inputStream == null || StringUtils.isEmpty(data)) {
                Enumeration<String> enumeration = request.getParameterNames();
                JsonObject jsonObject = new JsonObject();
                while (enumeration.hasMoreElements()) {
                    String key = enumeration.nextElement();
                    if (excludeFields != null && Arrays.binarySearch(excludeFields, key) != -1) {
                        continue;
                    }
                    String value = request.getParameter(key);
                    if (value != null) {
                        jsonObject.addProperty(key, StringUtils.decodeByUTF8(value));
                    }
                }
                return gson.fromJson(jsonObject, clazz);
            }
            // 如果流中已经没有数据，则从缓存中获取数据
            // 如果已经得到数据，则缓存到request中
            if (StringUtils.isEmpty(data)) {
                Object cachedData = request.getAttribute(CACHED_INPUT_STREAM_DATA);
                if (cachedData != null) {
                    data = IOUtils.toString((byte[]) cachedData, "utf-8");
                }
            } else {
                request.setAttribute(CACHED_INPUT_STREAM_DATA, data.getBytes("utf-8"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (data == null) {
            return null;
        }

        //空对象和空集合不进行转换
        data = data.replaceAll("(:\\[\\])|(:\\{\\})", ":null");
        return gson.fromJson(data, clazz);
    }

    public static void printSuccess(HttpServletResponse response) {
        ResponseData responseData = new ResponseData();
        responseData.setSuccess(true);
        responseData.setCode("1");
        responseData.setMessage("操作成功");
        printJsonObject(response, responseData);
    }

    public static void printSuccess(HttpServletResponse response, Object data) {
        ResponseData responseData = new ResponseData();
        responseData.setSuccess(true);
        responseData.setData(data);
        responseData.setMessage("操作成功");
        responseData.setCode("1");
        printJsonObject(response, responseData);
    }

    public static void printData(HttpServletResponse response, Object data) {
        ResponseData responseData = new ResponseData();
        responseData.setSuccess(true);
        responseData.setData(data);
        responseData.setCode("1");
        responseData.setMessage("操作成功");
        printJsonObject(response, responseData);
    }

    public static void printDataInclude(HttpServletResponse response, Object data, String... fields) {
        ResponseData responseData = new ResponseData();
        responseData.setSuccess(true);
        responseData.setData(data);
        responseData.setCode("1");
        responseData.setMessage("操作成功");
        String json = toJsonInclude(responseData, fields);
        printJson(response, json);
    }

    public static void printDataExclude(HttpServletResponse response, Object data, String... fields) {
        ResponseData responseData = new ResponseData();
        responseData.setData(data);
        responseData.setSuccess(true);
        responseData.setCode("1");
        responseData.setMessage("操作成功");
        String json = toJsonExclude(responseData, fields);
        printJson(response, json);
    }

    public static void printFail(HttpServletResponse response, String code, String message, Object data) {
        ResponseData responseData = new ResponseData();
        responseData.setFail(true);
        responseData.setData(data);
        responseData.setMessage(message);
        responseData.setCode("0");
        String json = toJson(responseData);
        printJson(response, json);
    }

    public static void printError(HttpServletResponse response, String code, String message, Object data) {
        ResponseData responseData = new ResponseData();
        responseData.setError(true);
        responseData.setMessage(message);
        responseData.setData(data);
        responseData.setCode("0");
        String json = toJson(responseData);
        printJson(response, json);
    }
}
