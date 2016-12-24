package com.michael.stock.fn.service;

import java.util.Date;

/**
 * @author Michael
 */
public class Handle {
    // 处理时间
    private Date handleTime;
    // 开始时间
    private Date startTime;
    // 截止时间
    private Date endTime;

    public Handle() {
    }

    public Handle(Date handleTime, Date startTime, Date endTime) {
        this.handleTime = handleTime;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Date getHandleTime() {
        return handleTime;
    }

    public void setHandleTime(Date handleTime) {
        this.handleTime = handleTime;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
}
