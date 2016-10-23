package com.michael.base.log.bo;

import com.michael.core.hibernate.criteria.BO;
import com.michael.core.hibernate.criteria.Condition;
import com.michael.core.hibernate.criteria.LikeModel;
import com.michael.core.hibernate.criteria.MatchModel;

import java.util.Date;

/**
 * @author Michael
 */
public class LoginLogBo implements BO {
    // 登录IP
    @Condition
    private String ip;

    // 登录时间
    @Condition(matchMode = MatchModel.GE, target = "loginTime")
    private Date loginTimeGe;
    @Condition(matchMode = MatchModel.LT, target = "loginTime")
    private Date loginTimeLt;

    @Condition
    private String creatorId;
    // 退出时间
    @Condition
    private Date logoutTime;

    // 是否已经退出（true表示已经退出，false表示未退出）
    @Condition(matchMode = MatchModel.NOT_NULL, target = "logoutTime")
    private Boolean logout;
    // 退出方式
    @Condition
    private String type;

    @Condition(matchMode = MatchModel.LIKE, likeMode = LikeModel.ANYWHERE)
    private String description;

    public Boolean getLogout() {
        return logout;
    }

    public void setLogout(Boolean logout) {
        this.logout = logout;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getIp() {
        return this.ip;
    }

    public Date getLoginTimeGe() {
        return loginTimeGe;
    }

    public void setLoginTimeGe(Date loginTimeGe) {
        this.loginTimeGe = loginTimeGe;
    }

    public Date getLoginTimeLt() {
        return loginTimeLt;
    }

    public void setLoginTimeLt(Date loginTimeLt) {
        this.loginTimeLt = loginTimeLt;
    }

    public void setLogoutTime(Date logoutTime) {
        this.logoutTime = logoutTime;
    }

    public Date getLogoutTime() {
        return this.logoutTime;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
