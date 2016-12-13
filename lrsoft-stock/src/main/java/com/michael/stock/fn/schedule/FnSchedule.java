package com.michael.stock.fn.schedule;

import com.michael.core.SystemContainer;
import com.michael.core.pool.ThreadPool;
import com.michael.stock.fn.service.Fn3Service;
import com.michael.stock.fn.service.Fn4Service;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.Date;

/**
 * @author Michael
 */
@Component
public class FnSchedule {

    private Date lastTime;

    // 每天凌晨4点同步
    @Scheduled(cron = "0 0 4 * * ?")
    public void resetFn() {
        final SystemContainer instance = SystemContainer.getInstance();
        if (lastTime != null) {
            Assert.isTrue((new Date().getTime() - lastTime.getTime()) / (1000 * 60L) > 15, "15分钟内只能执行一次，请等待！");
        }
        lastTime = new Date();
        // 重置3元运算
        ThreadPool.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                instance.getBean(Fn3Service.class).reset();
            }
        });

        // 重置4元运算
        ThreadPool.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                instance.getBean(Fn4Service.class).reset();
            }
        });
    }

}
