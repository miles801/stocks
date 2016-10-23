package com.michael.base.org.service.impl;

import com.michael.base.emp.bo.EmpBo;
import com.michael.base.emp.dao.EmpDao;
import com.michael.base.emp.domain.Emp;
import com.michael.base.emp.vo.EmpVo;
import com.michael.base.org.bo.OrgBo;
import com.michael.base.org.dao.OrgDao;
import com.michael.base.org.dao.OrgEmpDao;
import com.michael.base.org.domain.Org;
import com.michael.base.org.service.OrgService;
import com.michael.base.org.vo.OrgDTO;
import com.michael.base.org.vo.OrgVo;
import com.michael.core.SystemContainer;
import com.michael.core.beans.BeanWrapBuilder;
import com.michael.core.beans.BeanWrapCallback;
import com.michael.core.context.SecurityContext;
import com.michael.core.pager.PageVo;
import com.michael.pinyin.SimplePinYin;
import com.michael.pinyin.StandardStrategy;
import com.michael.tree.TreeBuilder;
import com.michael.utils.string.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Michael
 */
@Service("orgService")
public class OrgServiceImpl implements OrgService, BeanWrapCallback<Org, OrgVo> {
    @Resource
    private OrgDao orgDao;

    @Resource
    private OrgEmpDao orgEmpDao;

    @Override
    public String save(Org org) {
        org.setDeleted(false);


        // 构建树形对象
        TreeBuilder<Org> treeBuilder = new TreeBuilder<Org>();
        treeBuilder.beforeSave(org);

        // 验证合法性
        validateAndInit(org);

        String id = orgDao.save(org);

        return id;
    }


    @Override
    public void update(Org org) {

        validateAndInit(org);

        // 构建树形对象
        TreeBuilder<Org> treeBuilder = new TreeBuilder<Org>();
        treeBuilder.beforeUpdate(org);

        // 更新
        orgDao.update(org);
    }

    private void validateAndInit(Org org) {
        // 检查编号
        if (StringUtils.isNotEmpty(org.getCode())) {
            Boolean exists = orgDao.hasCode(org.getCode(), org.getId());
            Assert.isTrue(!exists, "保存失败!机构名称不能重复!");
        }

        // 检查名称重复性
        String name = org.getName();
        String parentId = org.getParentId();
        boolean exist = orgDao.hasName(name, parentId, org.getId());
        Assert.isTrue(!exist, "操作失败!同一层级下机构名称不能重复!");

        // 设置拼音
        String pinyin = org.getPinyin();
        if (StringUtils.isEmpty(pinyin)) {
            SimplePinYin pinYin = new SimplePinYin();
            StandardStrategy strategy = new StandardStrategy();
            String[] nameChar = name.split(".");
            StringBuilder builder = new StringBuilder(nameChar.length);
            for (String foo : nameChar) {
                builder.append(pinYin.toPinYin(foo, strategy).substring(0, 1));
            }
            org.setPinyin(builder.toString());
        }

        // 设置全路径
        if (StringUtils.isEmpty(parentId)) {
            org.setLongName(org.getName());
        } else {
            Org parent = orgDao.findById(parentId);
            Assert.notNull(parent, "操作失败!上级机构已经不存在，请刷新后重试!");
            org.setLongName(parent.getLongName() + " - " + org.getName());
        }
    }

    @Override
    public List<OrgVo> children(String parentId) {
        OrgBo bo = new OrgBo();
        bo.setParentId(parentId);
        bo.setDeleted(false);
        List<Org> data = orgDao.query(bo);
        return BeanWrapBuilder.newInstance().wrapList(data, OrgVo.class);
    }

    @Override
    public OrgDTO childrenAndEmp(String parentId) {
        OrgBo bo = new OrgBo();
        if (StringUtils.isEmpty(parentId)) {
            bo.setParentId("0");
        } else {
            bo.setParentId(parentId);
        }
        bo.setDeleted(true);
        List<Org> data = orgDao.query(bo);
        OrgDTO dto = new OrgDTO();
        dto.setOrgs(data);
        if (StringUtils.isNotEmpty(parentId)) {
            EmpBo empBo = new EmpBo();
            empBo.setLocked(0);
            List<Emp> emps = SystemContainer.getInstance().getBean(EmpDao.class).queryByOrg(parentId, empBo);
            dto.setEmps(BeanWrapBuilder.newInstance()
                    .wrapList(emps, EmpVo.class));
        }
        return dto;
    }

    @Override
    public PageVo pageQuery(OrgBo bo) {
        PageVo vo = new PageVo();
        Long total = orgDao.getTotal(bo);
        vo.setTotal(total);
        if (total == null || total == 0) return vo;
        List<Org> orgList = orgDao.query(bo);
        List<OrgVo> vos = BeanWrapBuilder.newInstance()
                .setCallback(this)
                .wrapList(orgList, OrgVo.class);
        vo.setData(vos);
        return vo;
    }


    @Override
    public OrgVo findById(String id) {
        Org org = orgDao.findById(id);
        return BeanWrapBuilder.newInstance()
                .wrap(org, OrgVo.class);
    }

    @Override
    public List<OrgVo> tree(OrgBo bo) {
        if (bo == null) {
            bo = new OrgBo();
        }
        List<Org> data = orgDao.query(bo);

        return BeanWrapBuilder.newInstance()
                .wrapList(data, OrgVo.class);
    }

    @Override
    public void deleteByIds(String[] ids) {
        if (ids == null || ids.length == 0) return;
        for (String id : ids) {
            if (StringUtils.isEmpty(id)) {
                continue;
            }
            Org org = orgDao.findById(id);
            Assert.notNull(org, "删除失败!机构已经不存在，请刷新后重试!");
            Long childrenCounts = orgDao.childrenCounts(id);
            Assert.isTrue(childrenCounts == null || childrenCounts == 0, "删除失败!该机构下还存在子机构，不能够删除!");
            orgDao.delete(org);
        }
    }


    @Override
    public void forceDelete(String[] ids) {
        Logger logger = Logger.getLogger(OrgService.class);
        logger.warn(String.format("%s(%s) 执行了强制删除机构的操作!", SecurityContext.getEmpName(), SecurityContext.getEmpCode()));
        Assert.notEmpty(ids, "强制删除机构失败!机构ID不能为空!");
        for (String id : ids) {
            if (StringUtils.isEmpty(id)) {
                continue;
            }
            // 强制删除当前机构及子机构
            orgDao.forceDelete(id);
        }
    }

    @Override
    public void disable(String[] ids) {

    }

    @Override
    public void enable(String[] ids) {
        if (ids == null || ids.length == 0) return;
        for (String id : ids) {
            Org org = orgDao.findById(id);
            Assert.notNull(org, "启用失败!组织机构不存在!请刷新后重试!");

            // 获取当前机构的所有上级机构
            String path[] = org.getPath().split("/");
            Set<String> parentIds = new HashSet<String>();
            for (String p : path) {
                if (StringUtils.isEmpty(p)) {
                    continue;
                }
                parentIds.add(p);
            }
            // 启用包括自身在内的所有机构
            orgDao.enableAll(parentIds);
        }
    }

    @Override
    public void doCallback(Org org, OrgVo vo) {
    }
}
