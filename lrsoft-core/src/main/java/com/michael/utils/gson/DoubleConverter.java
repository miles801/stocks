package com.michael.utils.gson;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.text.DecimalFormat;

/**
 * Double类型的转化器
 * 为了防止将Double数据转成科学计数法，所以在将实体类解析成json时，会将Double数据已字符串的形式进行返回
 *
 * @author Michael
 */
public class DoubleConverter extends TypeAdapter<Double> {
    final DecimalFormat df = new DecimalFormat("##,###.##");

    @Override
    public void write(JsonWriter out, Double value) throws IOException {
        if (value == null) {
            out.value("0");
            return;
        }
        out.value(df.format(value));
    }

    @Override
    public Double read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        }
        return Double.parseDouble(in.toString());
    }
}
