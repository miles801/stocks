package com.michael.base.attachment.bo;

import com.michael.core.hibernate.criteria.BO;
import com.michael.core.hibernate.criteria.Condition;
import com.michael.core.hibernate.criteria.LikeModel;
import com.michael.core.hibernate.criteria.MatchModel;

/**
 * 附件的高级查询对象
 *
 * @author Michael
 */
public class AttachmentBo implements BO {
    /**
     * 文件名称
     */
    @Condition(matchMode = MatchModel.LIKE, likeMode = LikeModel.ANYWHERE)
    private String fileName;

    /**
     * 文件大小：大与等于指定值
     */
    @Condition(matchMode = MatchModel.GE, target = "size")
    private Long lowSize;

    /**
     * 文件大小：小于指定值
     */
    @Condition(matchMode = MatchModel.LT, target = "size")
    private Long highSize;


    /**
     * 上传时间：大于等于这个这个值
     */
    @Condition(matchMode = MatchModel.GE, target = "uploadTime")
    private Long uploadTimeMin;

    /**
     * 上传时间：小于这个值
     */
    @Condition(matchMode = MatchModel.LT, target = "uploadTime")
    private Long uploadTimeMax;

    /**
     * 状态：临时/永久
     */
    @Condition
    private String status;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Long getLowSize() {
        return lowSize;
    }

    public void setLowSize(Long lowSize) {
        this.lowSize = lowSize;
    }

    public Long getHighSize() {
        return highSize;
    }

    public void setHighSize(Long highSize) {
        this.highSize = highSize;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getUploadTimeMin() {
        return uploadTimeMin;
    }

    public void setUploadTimeMin(Long uploadTimeMin) {
        this.uploadTimeMin = uploadTimeMin;
    }

    public Long getUploadTimeMax() {
        return uploadTimeMax;
    }

    public void setUploadTimeMax(Long uploadTimeMax) {
        this.uploadTimeMax = uploadTimeMax;
    }
}
