package com.michael.base.position.dao;

import com.michael.base.position.bo.PositionBo;
import com.michael.base.position.domain.Position;

import java.util.List;

/**
 * @author Michael
 */
public interface PositionDao {

    String save(Position position);

    void update(Position position);

    /**
     * 高级查询接口
     */
    List<Position> query(PositionBo bo);

    /**
     * 查询总记录数
     */
    Long getTotal(PositionBo bo);

    Position findById(String id);

    void deleteById(String id);

    /**
     * 根据实体对象删除
     * 必须保证该实体是存在的（一般是get或者load得到的对象）
     */
    void delete(Position position);

    /**
     * 判断同一级下是否具有相同的名称
     *
     * @param name     名称
     * @param parentId 上级ID
     * @param id       需要排除自身（一般是在更新时）
     * @return true存在
     */
    boolean hasName(String name, String parentId, String id);

    /**
     * 是否具有相同的编号
     *
     * @param code 编号
     * @param id   需要排除自身（一般是在更新时）
     * @return true存在
     */
    boolean hasCode(String code, String id);

    /**
     * 查询指定岗位下的所有子岗位（包括隔代岗位）
     *
     * @param id 岗位ID
     * @return 岗位集合
     */
    List<Position> children(String id);
}
