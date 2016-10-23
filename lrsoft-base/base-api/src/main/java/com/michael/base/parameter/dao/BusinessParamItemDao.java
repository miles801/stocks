package com.michael.base.parameter.dao;

import com.michael.base.parameter.bo.BusinessParamItemBo;
import com.michael.base.parameter.domain.BusinessParamItem;

import java.util.List;
import java.util.Set;

/**
 * @author miles
 * @datetime 2014-07-02
 */
public interface BusinessParamItemDao {

    String save(BusinessParamItem businessParamItem);

    void update(BusinessParamItem businessParamItem);

    List<BusinessParamItem> query(BusinessParamItemBo bo);

    Long getTotal(BusinessParamItemBo bo);

    BusinessParamItem findById(String id);

    void delete(BusinessParamItem item);

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
     * @param type  业务参数类型
     * @param value 业务参数的值
     * @return 业务参数值的名称
     */
    String queryName(String type, String value);

    /**
     * <p>查询所有级联了指定业务参数的所有业务参数集合</p>
     * <p>即，所有的级联类型为指定编号，且级联的值为指定的值的参数集合</p>
     * <p>例如:</p>
     * <p>业务参数a级联了编号为'N',值为'1'的业务参数，</p>
     * <p>业务参数b也级联了编号'N’,值为'1'的业务参数，</p>
     * <p>那么使用fetchCascade('N','1')查询出来的集合就是[a,b]</p>
     * 任意参数为空，则返回null
     *
     * @param typeCode 目标类型编号
     * @param value    目标选项的值
     * @return 业务参数集合
     */
    List<BusinessParamItem> fetchCascade(String typeCode, String value);

    /**
     * 查询指定业务参数类型中，指定的值被级联的参数的id
     *
     * @param types 业务参数类型集合
     * @param codes 业务参数值的集合（编号集合）
     * @return 被级联的业务参数的id集合
     */
    List<String> hasBeenCascaded(Set<String> types, Set<String> codes);


    /**
     * 通过业务参数的Name找到对应的业务参数的Value
     *
     * @param name
     * @return
     */
    BusinessParamItem findValueByName(String type, String name);

    /**
     * 删除指定类型的所有参数
     *
     * @param type 业务参数的类型编号
     */
    void deleteByType(String type);
}
