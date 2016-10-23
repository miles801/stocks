package com.michael.base.msg.domain;

import com.michael.base.attachment.AttachmentSymbol;
import com.michael.common.CommonDomain;
import com.michael.docs.annotations.ApiField;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 消息
 *
 * @author Michael
 */
@Entity
@Table(name = "msg_message")
public class Message extends CommonDomain implements AttachmentSymbol {


    @ApiField(value = "标题", required = true)
    @NotNull
    @Column(length = 40, nullable = false)
    private String title;

    @ApiField(value = "内容", required = true)
    @Column(columnDefinition = "text")
    private String content;

    @ApiField(value = "消息类型", required = true)
    @Column(length = 40)
    private String type;

    @ApiField(value = "消息类型名称", required = false)
    @Column(length = 40)
    private String typeName;
    // 接收人
    @ApiField(value = "接收人ID", required = true)
    @NotNull
    @Column(length = 40, nullable = false)
    private String receiverId;

    @ApiField(value = "接收人名称", required = true)
    @NotNull
    @Column(length = 40, nullable = false)
    private String receiverName;

    // 发送人
    @ApiField(value = "发送人ID", required = true)
    @NotNull
    @Column(length = 40, nullable = false)
    private String senderId;
    @ApiField(value = "发送人名称", required = true)
    @NotNull
    @Column(length = 40, nullable = false)
    private String senderName;

    // 发送时间
    @ApiField(value = "发送时间")
    @Column
    private Date sendTime;

    // 图标地址
    @ApiField(value = "缩略图url")
    @Column(length = 200)
    private String icon;

    // 跳转链接
    @ApiField(value = "消息正文跳转URL")
    @Column(length = 200)
    private String url;
    // 是否已读
    @Column
    @ApiField(value = "是否已读")
    private Boolean hasRead;

    // 阅读时间
    @ApiField(value = "阅读时间")
    @Column
    private Date readTime;

    // 收藏/关注
    @ApiField(value = "是否加入收藏")
    @Column
    private Boolean mark;

    @Override
    public String businessId() {
        return getId();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Date getReadTime() {
        return readTime;
    }

    public void setReadTime(Date readTime) {
        this.readTime = readTime;
    }

    public Boolean getMark() {
        return mark;
    }

    public void setMark(Boolean mark) {
        this.mark = mark;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public Boolean getHasRead() {
        return hasRead;
    }

    public void setHasRead(Boolean hasRead) {
        this.hasRead = hasRead;
    }
}
