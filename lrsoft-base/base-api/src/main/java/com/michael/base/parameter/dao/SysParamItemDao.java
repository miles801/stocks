package com.michael.base.parameter.dao;

import com.michael.base.parameter.bo.SysParamItemBo;
import com.michael.base.parameter.domain.SysParamItem;

import java.util.List;

/**
 * @author miles
 * @datetime 2014-06-20
 */
public interface SysParamItemDao {

    String save(SysParamItem sysParamItem);

    void update(SysParamItem sysParamItem);

    List<SysParamItem> query(SysParamItemBo bo);

    long getTotal(SysParamItemBo bo);

    SysParamItem findById(String id);

    void delete(SysParamItem item);

    /**
     * 判断名称在指定父节点下是否存在
     *
     * @param typeCode 类型的编号
     * @param name     选项的名称
     * @return true/false
     */
    boolean hasName(String typeCode, String name);

    /**
     * 判断指定的code是否存在
     *
     * @param typeCode 类型的编号
     * @param value    选项的值
     * @return true/false
     */
    boolean hasValue(String typeCode, String value);

    /**
     * 获取指定类型的下指定值对应的名称
     *
     * @param type  系统参数类型
     * @param value 系统参数的值
     * @return 系统参数值的名称
     */
    String queryName(String type, String value);

    /**
     * 查询所有级联了指定基础参数的所有基础参数集合
     * 即，所有的级联类型为指定编号，且级联的值为指定的值的参数集合
     * 例如:
     * 基础参数a级联了编号为'N',值为'1'的基础参数，
     * 基础参数b也级联了编号'N’,值为'1'的基础参数，
     * 那么使用fetchCascade('N','1')查询出来的集合就是[a,b]
     * <p/>
     * 任意参数为空，则返回null
     *
     * @param typeCode 目标类型编号
     * @param value    目标选项的值
     * @return 基础参数集合
     */
    List<SysParamItem> fetchCascade(String typeCode, String value);

    /**
     * 查询参数是否被级联
     *
     * @param typeCode 类型
     * @param value    值
     * @return true：被级联
     */
    boolean hasCascade(String typeCode, String value);

    /**
     * 删除指定类型的所有参数
     *
     * @param type 系统参数的编号
     */
    void deleteByType(String type);
}
