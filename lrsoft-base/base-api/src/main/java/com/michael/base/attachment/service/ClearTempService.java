package com.michael.base.attachment.service;

import com.michael.base.attachment.utils.AttachmentHolder;
import com.michael.core.spring.SpringLoadListener;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author Michael
 */
public class ClearTempService implements SpringLoadListener {

    @Override
    public void execute(BeanFactory beanFactory) {
        final Logger logger = Logger.getLogger(ClearTempService.class);
        logger.info("初始化附件垃圾数据清理服务...");
        final AttachmentService attachmentService = beanFactory.getBean(AttachmentService.class);
        Assert.notNull(attachmentService, "定时执行清空临时目录时，没有获取附件服务!" + AttachmentService.class.getName());
        Timer timer = new Timer("定时器：清除附件临时文件...");

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                logger.info("------------------------准备清理附件临时目录和垃圾数据------------------------");
                attachmentService.clearTemp();
                logger.info("------------------------附件临时目录和垃圾数据清理成功------------------------");
            }
        }, new Date(), AttachmentHolder.newInstance().getExpiredHour() * 1000 * 60 * 60);
    }
}
