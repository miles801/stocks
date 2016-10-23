package com.michael.base.attachment.domain;

import com.michael.docs.annotations.ApiField;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * @author miles
 * @datetime 14-2-28 下午10:40
 * 附件
 */

@Entity
@Table(name = "sys_attachment")
public class Attachment {

    public static final String STATUS_TEMP = "TEMP";
    public static final String STATUS_ACTIVE = "ACTIVE";
    @ApiField(value = "ID")
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "assigned")
    @GeneratedValue(generator = "idGenerator")
    @Column(name = "id", unique = true, nullable = false)
    private String id;

    /**
     * 文件的真实名称
     */
    @ApiField(value = "文件的真实名称", required = true)
    @Column(name = "file_name", length = 200, updatable = false)
    private String fileName;

    @ApiField(value = "文件类型")
    @Column(name = "file_type", length = 200, updatable = false)
    private String fileType;

    @ApiField(value = "内容类型")
    @Column(name = "content_type", length = 200, updatable = false)
    private String contentType;

    @ApiField(value = "文件大小")
    @Column(name = "file_size")
    private Long size;

    @ApiField(value = "上传时间")
    @Column(name = "upload_time")
    private Long uploadTime;
    /**
     * 业务id
     */
    @ApiField(value = "业务ID")
    @Column(name = "business_id", length = 40)
    private String businessId;

    /**
     * 业务类型
     */
    @ApiField(value = "业务类型")
    @Column(name = "business_type", length = 40)
    private String businessType;

    /**
     * 业务类
     */
    @ApiField(value = "业务类")
    @Column(name = "business_class", length = 100)
    private String businessClass;

    /**
     * 访问路径
     */
    @ApiField(value = "文件的访问地址", desc = "上传成功后自动生成")
    @Column(name = "url", length = 400)
    private String path;
    /**
     * 状态
     */
    @ApiField(value = "文件状态", desc = "临时：上传后没有永久保存的，定时会被清除;永久：一直存在")
    private String status;

    @ApiField(value = "描述")
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public Long getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(Long uploadTime) {
        this.uploadTime = uploadTime;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getBusinessClass() {
        return businessClass;
    }

    public void setBusinessClass(String businessClass) {
        this.businessClass = businessClass;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
