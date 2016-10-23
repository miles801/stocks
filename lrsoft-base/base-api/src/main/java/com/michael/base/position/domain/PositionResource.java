package com.michael.base.position.domain;

import com.michael.common.CommonDomain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * 岗位资源（权限）
 *
 * @author Michael
 */
@Entity
@Table(name = "sys_position_resource")
public class PositionResource extends CommonDomain {

    @NotNull
    @Column(nullable = false, length = 40)
    private String positionId;
    @NotNull
    @Column(nullable = false, length = 40)
    private String resourceId;

    // 资源类型，同Resource的类型一致
    @Column(length = 40)
    private String resourceType;

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public String getPositionId() {
        return positionId;
    }

    public void setPositionId(String positionId) {
        this.positionId = positionId;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }
}
