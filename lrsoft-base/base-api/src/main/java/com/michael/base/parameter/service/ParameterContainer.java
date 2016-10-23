package com.michael.base.parameter.service;

import com.michael.base.parameter.vo.BusinessParamItemVo;
import com.michael.base.parameter.vo.SysParamItemVo;
import com.michael.core.SystemContainer;
import com.michael.core.pager.Pager;
import com.michael.utils.string.StringUtils;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>参数容器:</p>
 * <p>提供从容器中读取/缓存指定类型的参数选项集合的功能</p>
 * <p>提供更新、移除、清空已缓存数据的功能</p>
 * Created by Michael on 2014/9/4.
 */
public class ParameterContainer {

    private static ParameterContainer ourInstance = new ParameterContainer();
    private Logger logger = Logger.getLogger(ParameterContainer.class);

    public static ParameterContainer getInstance() {
        return ourInstance;
    }


    private ParameterContainer() {
    }

    // 系统参数类型,key为基础参数类型的编号，value为对应的参数集合
    private final Map<String, List<SysParamItemVo>> systemContainer = new HashMap<String, List<SysParamItemVo>>();

    // 业务参数类型,key为业务参数类型的编号，value为对应的参数集合
    private final Map<String, List<BusinessParamItemVo>> businessContainer = new HashMap<String, List<BusinessParamItemVo>>();

    // 系统参数类型,key为编号，value为名称
    private Map<String, String> systemParamTypeMap = new HashMap<String, String>();


    // 业务参数类型,key为编号，value为名称
    private final Map<String, String> businessParamTypeMap = new HashMap<String, String>();


    /**
     * <p>从容器中查询指定基础类型的编号对应的所有选项</p>
     * <p>如果容器中还没有初始化该类型的数据，则加载了数据并放入容器中，然后返回</p>
     *
     * @param type 基础参数类型的编号，如果为空，则返回null
     * @return 基础参数集合
     */
    public List<SysParamItemVo> getSystemItems(String type) {
        if (StringUtils.isEmpty(type)) {
            return null;
        }
        List<SysParamItemVo> vos = systemContainer.get(type);
        if (vos == null) {
            vos = reloadSystem(type);
            systemContainer.put(type, vos);
            return vos;
        }
        return vos;
    }

    /**
     * <p>从容器中查询指定业务类型编号对应的所有选项</p>
     * <p>如果容器中还没有初始化该类型的数据，则加载了数据并放入容器中，然后返回</p>
     *
     * @param type 业务参数类型编号，如果为空，则返回null
     * @return 业务参数集合
     */
    public List<BusinessParamItemVo> getBusinessItems(String type) {
        if (StringUtils.isEmpty(type)) {
            return null;
        }
        Integer start = Pager.getStart();
        Integer limit = Pager.getLimit();
        Pager.setStart(null);
        Pager.setLimit(null);
        List<BusinessParamItemVo> vos = businessContainer.get(type);
        if (vos == null) {
            vos = reloadBusiness(type);
        }
        Pager.setStart(start);
        Pager.setLimit(limit);
        return vos;
    }


    /**
     * <p>加载业务参数类型编号为指定值的所有业务参数集合,并将结果缓存到容器中 </p>
     * <p>如果返回的数据为null，则创建一个空的集合缓存到容器中</p>
     *
     * @param type 业务参数编号，如果为空，则返回null
     * @return 业务参数选项集合
     */
    public List<BusinessParamItemVo> reloadBusiness(final String type) {
        // 清掉历史数据
//        synchronized (businessContainer) {
        businessContainer.remove(type);
        if (org.apache.commons.lang3.StringUtils.isEmpty(type)) return null;
        // 清除分页信息
        Pager.clear();
        // 查询新的数据
        List<BusinessParamItemVo> vos = SystemContainer.getInstance().getBean(BusinessParamItemService.class)
                .queryValid(type);
        if (vos == null) {
            vos = new ArrayList<BusinessParamItemVo>();
        }
        businessContainer.put(type, vos);
        return vos;
//        }
    }

    /**
     * <p>加载系统参数类型编号为指定值的所有系统参数集合,并将结果缓存到容器中 </p>
     * <p>如果返回的数据为null，则创建一个空的集合缓存到容器中</p>
     *
     * @param type 系统参数编号，如果为空，则返回null
     * @return 基础参数选项集合
     */
    public List<SysParamItemVo> reloadSystem(String type) {
//        synchronized (systemContainer) {
        if (org.apache.commons.lang3.StringUtils.isEmpty(type)) return null;
        logger.info("========== 重新加载系统参数[" + type + "]================");
        // 清除分页信息
        Pager.clear();
        List<SysParamItemVo> vos = SystemContainer.getInstance().getBean(SysParamItemService.class)
                .queryValid(type);
        if (vos == null) {
            vos = new ArrayList<SysParamItemVo>();
        }
        logger.info("========== 系统参数[" + type + "]加载完毕：" + vos.size() + "================");
        systemContainer.put(type, vos);
        return vos;
//        }
    }

    /**
     * <p>查询编号为指定值的系统参数类型对应的名称</p>
     * <p>如果容器中不存在该编号，则查询，并将结果缓存到容器中后返回</p>
     *
     * @param code 系统参数类型的编号，如果为空，则返回null
     * @return 系统参数类型的名称
     */
    public String getSystemTypeName(String code) {
        if (org.apache.commons.lang3.StringUtils.isEmpty(code)) return null;
        String name = systemParamTypeMap.get(code);
        if (StringUtils.isEmpty(name)) {
            name = SystemContainer.getInstance().getBean(SysParamTypeService.class)
                    .getName(code);
            systemParamTypeMap.put(code, name);
        }
        return name;
    }

    /**
     * <p>查询编号为指定值的业务参数类型对应的名称</p>
     * <p>如果容器中不存在该编号，则查询，并将结果缓存到容器中后返回</p>
     * <p>该缓存只会缓存30秒</p>
     *
     * @param code 业务参数类型的编号，如果为空，则返回null
     * @return 业务参数类型的名称
     */
    public String getBusinessTypeName(final String code) {
        if (org.apache.commons.lang3.StringUtils.isEmpty(code)) return null;
        String name = businessParamTypeMap.get(code);
        if (StringUtils.isEmpty(name)) {
            name = SystemContainer.getInstance().getBean(BusinessParamTypeService.class)
                    .getName(code);
            businessParamTypeMap.put(code, name);
        }
        return name;
    }

    /**
     * <p>查询系统参数的名称</p>
     * <p>如果容器中不存在指定类型编号的数据，则查询，并将结果缓存到容器中后返回</p>
     *
     * @param type  系统参数类型的编号，如果为空，则返回null
     * @param value 系统参数选项的值，如果为空，则返回null
     * @return 系统参数类型的名称
     */
    public String getSystemName(String type, String value) {
        if (org.apache.commons.lang3.StringUtils.isAnyEmpty(type, value)) {
            return null;
        }
        // 直接读取本地容器中的数据，如果没有则重新加载
        List<SysParamItemVo> vos = getSystemItems(type);
        for (SysParamItemVo vo : vos) {
            if (vo.getValue().equals(value)) {
                return vo.getName();
            }
        }
        return null;
    }

    /**
     * <p>查询系统参数的名称</p>
     * <p>如果容器中不存在指定类型编号的数据，则直接返回null</p>
     *
     * @param type  系统参数类型的编号，如果为空，则返回null
     * @param value 系统参数选项的值，如果为空，则返回null
     * @return 系统参数类型的名称
     */
    public String getSystemNameWithNoQuery(String type, String value) {
        if (org.apache.commons.lang3.StringUtils.isAnyEmpty(type, value)) {
            return null;
        }
        // 从本地容器中获取，没有则返回，否则遍历
        List<SysParamItemVo> vos = systemContainer.get(type);
        if (vos == null) {
            return SystemContainer.getInstance().getBean(SysParamItemService.class).findName(type, value);
        }
        for (SysParamItemVo vo : vos) {
            if (vo.getValue().equals(value)) {
                return vo.getName();
            }
        }
        return null;
    }

    /**
     * <p>查询业务参数的名称</p>
     * <p>如果容器中不存在指定类型编号的数据，则查询，并将结果缓存到容器中后返回</p>
     *
     * @param type  业务参数类型的编号，如果为空，则返回null
     * @param value 业务参数选项的值，如果为空，则返回null
     * @return 业务参数类型的名称
     */
    public String getBusinessName(String type, String value) {
        if (org.apache.commons.lang3.StringUtils.isAnyEmpty(type, value)) {
            return null;
        }
        // 从容器中获取指定编号缓存的数据，如果没有则重新加载
        List<BusinessParamItemVo> vos = getBusinessItems(type);
        for (BusinessParamItemVo vo : vos) {
            if (vo.getValue().equals(value)) {
                return vo.getName();
            }
        }
        return null;
    }

    /**
     * <p>查询业务参数的值</p>
     * <p>如果容器中不存在指定类型编号的数据，则查询，并将结果缓存到容器中后返回</p>
     *
     * @param type 业务参数类型的编号，如果为空，则返回null
     * @param name 业务参数选项的值，如果为空，则返回null
     * @return 业务参数类型的名称
     */
    public String getBusinessValue(String type, String name) {
        if (org.apache.commons.lang3.StringUtils.isAnyEmpty(type, name)) {
            return null;
        }
        // 从容器中获取指定编号缓存的数据，如果没有则重新加载
        List<BusinessParamItemVo> vos = getBusinessItems(type);
        for (BusinessParamItemVo vo : vos) {
            if (vo.getName().equals(name)) {
                return vo.getValue();
            }
        }
        return null;
    }

    public String getSysValue(String type, String name) {
        if (StringUtils.isEmpty(type) || StringUtils.isEmpty(name)) {
            return null;
        }
        // 从容器中获取指定编号缓存的数据，如果没有则重新加载
        List<SysParamItemVo> vos = getSystemItems(type);
        for (SysParamItemVo vo : vos) {
            if (vo.getName().equals(name)) {
                return vo.getValue();
            }
        }
        return null;
    }

    /**
     * <p>查询业务参数的名称</p>
     * <p>如果容器中不存在指定类型编号的数据，则直接返回null</p>
     *
     * @param type  业务参数类型的编号，如果为空，则返回null
     * @param value 业务参数选项的值，如果为空，则返回null
     * @return 业务参数类型的名称
     */
    public String getBusinessNameWithNoQuery(String type, String value) {
        if (org.apache.commons.lang3.StringUtils.isAnyEmpty(type, value)) {
            return null;
        }
        // 从容器中获取指定编号缓存的数据，如果没有则返回null
        List<BusinessParamItemVo> vos = businessContainer.get(type);
        if (vos == null) return null;
        for (BusinessParamItemVo vo : vos) {
            if (vo.getValue().equals(value)) {
                return vo.getName();
            }
        }
        return null;
    }

    /**
     * <p>清空容器中缓存的所有系统参数</p>
     */
    public void removeAllSys() {
//        synchronized (systemContainer) {
        systemContainer.clear();
//        }
    }

    /**
     * <p>清空容器中缓存的所有业务参数</p>
     */
    public void removeAllBusiness() {
//        synchronized (businessContainer) {
        businessContainer.clear();
//        }
    }

    /**
     * <p>从容器中移除缓存的指定系统类型的数据</p>
     *
     * @param sysType 系统参数类型编号，如果为空，则不执行任何操作
     */
    public void removeSys(String sysType) {
        if (org.apache.commons.lang3.StringUtils.isNotEmpty(sysType)) {
//            synchronized (systemContainer) {
            systemContainer.remove(sysType);
//            }
        }
    }

    /**
     * <p>从容器中移除缓存的指定业务类型的数据</p>
     *
     * @param businessType 业务参数类型编号，如果为空，则不执行任何操作
     */
    public void removeBusiness(String businessType) {
        if (org.apache.commons.lang3.StringUtils.isNotEmpty(businessType)) {
//            synchronized (businessContainer) {
            businessContainer.remove(businessType);
//            }
        }
    }
}
