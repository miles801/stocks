package com.michael.tree;

/**
 * @author Michael
 */
public interface Tree {
    String getId();

    void setId(String id);

    String getParentId();

    void setParentId(String parentId);

    String getPath();

    void setPath(String path);

    Integer getLevel();

    void setLevel(Integer level);

}
