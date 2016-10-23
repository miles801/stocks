package com.michael.stock.stock.dto;

import com.michael.poi.annotation.Col;
import com.michael.poi.annotation.ImportConfig;
import com.michael.poi.core.DTO;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Michael
 */
@ImportConfig(file = "", startRow = 1)
public class StockDTO implements DTO {
    // 编号
    @Col(index = 0)
    private String code;
    // 名称
    @Col(index = 1)
    private String name;
    // 类型
    @Col(index = 2)
    private Integer type;
    // 状态
    @Col(index = 3)
    private String status;

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getType() {
        return this.type;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return this.status;
    }


    public static void main(String[] args) {

        String url = "http://www.bestopview.com/stocklist.html";
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpget = new HttpGet(url);
        try {
            HttpResponse response = httpClient.execute(httpget);
            int status = response.getStatusLine().getStatusCode();
            if (status == 200) {
                InputStream input = response.getEntity().getContent();
                String content = IOUtils.toString(input, "gb2312");//新浪股票接口使用的是GBK编码
                input.close();
                Pattern pattern = Pattern.compile("<li><a href=.+>(.+)\\((\\d{6})\\)</a></li>");
                Matcher matcher = pattern.matcher(content);
                int i = 1;
                while (matcher.find()) {
                    String name = matcher.group(1);
                    String code = matcher.group(2);
                    System.out.println((i++) + "--" + name + "--->" + code);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        httpClient.getConnectionManager().shutdown();
    }
}
