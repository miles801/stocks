package com.michael.base.parameter.service;

import com.michael.base.parameter.bo.BusinessParamItemBo;
import com.michael.base.parameter.domain.BusinessParamItem;
import com.michael.base.parameter.vo.BusinessParamItemVo;
import com.michael.core.pager.PageVo;

import java.util.List;

/**
 * @author miles
 * @datetime 2014-07-02
 */
public interface BusinessParamItemService {

    String save(BusinessParamItem businessParamItem);

    void update(BusinessParamItem businessParamItem);

    PageVo query(BusinessParamItemBo bo);

    BusinessParamItemVo findById(String id);

    /**
     * 真的删除
     *
     * @param ids ID列表
     */
    void deleteByIds(String... ids);

    /**
     * 禁用
     *
     * @param ids ID列表
     */
    void disable(String... ids);

    /**
     * 启用
     *
     * @param ids ID列表
     */
    void enable(String... ids);

    /**
     * 根据类型查询有效的选项
     *
     * @param type 参数类型编号
     */
    List<BusinessParamItemVo> queryValid(String type);

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
    List<BusinessParamItemVo> fetchCascade(String typeCode, String value);

    /**
     * 查询指定业务参数类型下，状态为有效，且被其他参数级联的所有参数的集合
     *
     * @param type 业务参数类型
     * @return 被级联的参数的集合
     */
    List<BusinessParamItemVo> queryCascadeItem(String type);
}
