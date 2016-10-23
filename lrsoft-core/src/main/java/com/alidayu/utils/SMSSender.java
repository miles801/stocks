package com.alidayu.utils;

import com.alidayu.taobao.api.ApiException;
import com.alidayu.taobao.api.DefaultTaobaoClient;
import com.alidayu.taobao.api.TaobaoClient;
import com.alidayu.taobao.api.request.AlibabaAliqinFcSmsNumSendRequest;
import com.alidayu.taobao.api.response.AlibabaAliqinFcSmsNumSendResponse;
import com.google.gson.Gson;
import org.springframework.util.Assert;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.Map;
import java.util.Properties;

/**
 * @author Michael
 */
public class SMSSender {
    private static SMSSender ourInstance = new SMSSender();

    public static SMSSender getInstance() {
        return ourInstance;
    }

    private String sign;
    private TaobaoClient client;

    private Gson gson = new Gson();

    private SMSSender() {
        InputStream stream = SMSSender.class.getClassLoader().getResourceAsStream("config.properties");
        Assert.notNull(stream, "短信接口初始化失败!未获取到配置文件[config.properties]!");
        Properties properties = new Properties();
        try {
            properties.load(stream);
            final String sendSmsUrl = properties.getProperty("sms.url");
            Assert.hasText(sendSmsUrl, "短信接口初始化失败!未正确获取参数:sms.url");
            final String appKey = properties.getProperty("sms.appKey");
            Assert.hasText(appKey, "短信接口初始化失败!未正确获取参数:sms.appKey");
            final String appSecret = properties.getProperty("sms.appSecret");
            Assert.hasText(appSecret, "短信接口初始化失败!未正确获取参数:sms.appSecret");
            String s = properties.getProperty("sms.sign");
            Assert.hasText(s, "短信接口初始化失败!未正确获取参数:sms.sign");
            sign = URLDecoder.decode(s, "utf-8");
            client = new DefaultTaobaoClient(sendSmsUrl, appKey, appSecret);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送短信
     *
     * @param templateId 模板ID
     * @param mobiles    接收的电话号码
     * @param params     可选的参数
     * @return 返回结果
     */
    public SmsResponse send(String templateId, String mobiles, Map<String, String> params) {
        Assert.hasText(templateId, "模板不能为空！");
        Assert.hasText(mobiles, "电话号码不能为空!");
        AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();
        // 短信类型-短信通知
        req.setSmsType("normal");

        // 签名，该签名必须是在阿里大鱼中已经审核通过的
        req.setSmsFreeSignName(sign);

        // 参数，用于替换模板中的占位符
        if (params != null && !params.isEmpty()) {
            req.setSmsParamString(gson.toJson(params));
        }

        // 接收号码，如果有多个号码，可以使用逗号分隔，最多可以传入200个电话
        req.setRecNum(mobiles);

        // 短信模板ID，必须是已经通过审核的
        req.setSmsTemplateCode(templateId);

        try {
            AlibabaAliqinFcSmsNumSendResponse rsp = client.execute(req);
            String body = rsp.getBody();
            ResponseWrapper wrapper = gson.fromJson(body, ResponseWrapper.class);
            return wrapper.getResponse();
        } catch (ApiException e) {
            Assert.isTrue(false, "短信发送失败!" + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
