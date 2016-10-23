package com.michael.base.attachment.utils;

import com.michael.base.attachment.AttachmentConfig;
import com.michael.base.attachment.AttachmentConstant;
import com.michael.base.attachment.exception.AttachmentConfigurationException;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.util.Assert;

import java.io.File;
import java.io.IOException;

/**
 * 附件上传的工具类
 *
 * @author miles
 * @datetime 2014/4/25 2:54
 */
public class AttachmentHolder {
    private static AttachmentHolder holder = new AttachmentHolder();
    private String tempDir = null;
    private String activeDir = null;
    private int expireHour = 2;// 默认1小时清理一次
    private Logger logger = Logger.getLogger(AttachmentHolder.class);

    /**
     * 初始化附件的相关配置
     */
    private AttachmentHolder() {
        AttachmentConfig instance = AttachmentConfig.getInstance();
        // 获得清除历史的时间间隔
        String value = instance.getConfigValue(AttachmentConstant.ATTACHMENT_EXPIRED);
        if (value != null && value.matches("\\d+")) {
            expireHour = Integer.parseInt(value);
        }
        activeDir = instance.getConfigValue(AttachmentConstant.ATTACHMENT_ACTIVE);
        if (activeDir == null) {
            throw new AttachmentConfigurationException("附件上传配置文件中没有正确的配置附件保存目录[attachment.active]!");
        }
        if (!activeDir.endsWith("/") && !activeDir.endsWith("\\")) {
            activeDir += "/";
        }
        tempDir = activeDir + "temp/";
        try {
            FileUtils.forceMkdir(new File(activeDir));
            FileUtils.forceMkdir(new File(tempDir));
        } catch (IOException e) {
            logger.error("附件初始化:创建目录失败!");
            e.printStackTrace();
        }

        // 启动清空临时记录的方法
        logger.info("==========附件上传配置=========");
        logger.info("临时目录：" + tempDir);
        logger.info("正式目录：" + activeDir);
        logger.info("临时目录清空间隔：" + expireHour + "小时");
    }

    /**
     * 获得临时目录
     */
    public String getTempFolder() {
        return tempDir;
    }

    /**
     * 获得正式目录
     */
    public String getActiveFolder() {
        return activeDir;
    }

    public int getExpiredHour() {
        return expireHour;
    }

    /**
     * 获得临时目录下的指定名称的文件
     */
    public File getTempFile(String filename) {
        Assert.hasText(filename, "文件名不能为空!");
        // 如果临时目录不存在，则重新创建
        if (!new File(tempDir).exists()) {
            new File(tempDir).mkdirs();
        }
        return new File(tempDir + "/" + filename);
    }

    public static AttachmentHolder newInstance() {
        return holder;
    }

}
