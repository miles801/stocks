package com.michael.base.resource.web;

import java.util.List;

/**
 * @author Michael
 */
public class PositionResourceTemp {
    private String positionId;
    private List<String> resourceIds;

    public String getPositionId() {
        return positionId;
    }

    public void setPositionId(String positionId) {
        this.positionId = positionId;
    }

    public List<String> getResourceIds() {
        return resourceIds;
    }

    public void setResourceIds(List<String> resourceIds) {
        this.resourceIds = resourceIds;
    }
}
