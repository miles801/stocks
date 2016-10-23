package com.michael.base.attachment;

import com.michael.base.attachment.exception.AttachmentConfigurationException;
import com.michael.utils.string.StringUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Properties;

/**
 * 附件配置
 * Created by miles on 2014/8/20.
 */
public class AttachmentConfig {
    private static AttachmentConfig ourInstance = new AttachmentConfig();

    public static AttachmentConfig getInstance() {
        return ourInstance;
    }

    private Properties properties = null;

    private AttachmentConfig() {
        properties = new Properties();
        try {
            properties.load(AttachmentConfig.class.getClassLoader().getResourceAsStream("config.properties"));
        } catch (IOException e) {
            e.printStackTrace();
            throw new AttachmentConfigurationException("加载系统配置文件失败!");
        }
        String key = properties.getProperty("attachment.active");
        if (StringUtils.isEmpty(key)) {
            Logger logger = Logger.getLogger(AttachmentConfig.class);
            logger.error("读取附件配置信息失败,缺少变量值:attachment.active!");
        }
    }

    /**
     * 从配置文件中获取配置的属性的值
     *
     * @param key key为AttachmentConstant中的常量
     * @return 属性值
     */
    public String getConfigValue(String key) {
        return properties == null ? null : properties.getProperty(key);
    }


}
