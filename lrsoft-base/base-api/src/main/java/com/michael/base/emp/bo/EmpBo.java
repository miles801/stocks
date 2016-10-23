package com.michael.base.emp.bo;

import com.michael.core.hibernate.criteria.BO;
import com.michael.core.hibernate.criteria.Condition;
import com.michael.core.hibernate.criteria.LikeModel;
import com.michael.core.hibernate.criteria.MatchModel;
import com.michael.docs.annotations.ApiField;

import java.util.List;

/**
 * @author Michael
 */
public class EmpBo implements BO {

    @Condition(matchMode = MatchModel.LIKE, likeMode = LikeModel.ANYWHERE)
    @ApiField(value = "姓名", desc = "like")
    private String name;

    @Condition
    @ApiField(value = "工号", desc = "=")
    private String code;

    @Condition
    @ApiField(value = "登录名查询")
    private String loginName;

    @Condition(matchMode = MatchModel.LIKE, likeMode = LikeModel.ANYWHERE)
    @ApiField(value = "姓名全拼", desc = "like")
    private String pinyin;

    @Condition
    @ApiField(value = "性别，来自业务参数BP_SEX", desc = "=")
    private String sex;
    @Condition
    @ApiField(value = "电话号码", desc = "=")
    private String phone;
    @Condition
    @ApiField(value = "账号状态", desc = "=")
    private Integer locked;
    @Condition
    @ApiField(value = "机构ID", desc = "=")
    private String orgId;
    @Condition(matchMode = MatchModel.LIKE, likeMode = LikeModel.ANYWHERE)
    @ApiField(value = " 机构名称", desc = "like")
    private String orgName;

    @Condition
    @ApiField(value = "岗位编号,来自业务参数:BP_DUTY", desc = "=")
    private String duty;

    @ApiField(value = "关键字匹配", desc = "会查询几乎所有字段中匹配的内容")
    private String keywords;

    @ApiField("查询由指定人创建以及从属创建的信息")
    private String creatorId;

    @Condition(matchMode = MatchModel.IN, target = "id")
    private List<String> ids;

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public List<String> getIds() {
        return ids;
    }

    public void setIds(List<String> ids) {
        this.ids = ids;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getLocked() {
        return locked;
    }

    public void setLocked(Integer locked) {
        this.locked = locked;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getDuty() {
        return duty;
    }

    public void setDuty(String duty) {
        this.duty = duty;
    }
}
