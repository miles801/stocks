package com.michael.base.position.service.impl;

import com.michael.base.position.bo.PositionBo;
import com.michael.base.position.dao.PositionDao;
import com.michael.base.position.dao.PositionEmpDao;
import com.michael.base.position.domain.Position;
import com.michael.base.position.service.PositionService;
import com.michael.base.position.vo.PositionVo;
import com.michael.core.beans.BeanWrapBuilder;
import com.michael.core.beans.BeanWrapCallback;
import com.michael.core.hibernate.validator.ValidatorUtils;
import com.michael.core.pager.PageVo;
import com.michael.tree.TreeBuilder;
import com.michael.utils.string.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Michael
 */
@Service("positionService")
public class PositionServiceImpl implements PositionService, BeanWrapCallback<Position, PositionVo> {
    @Resource
    private PositionDao positionDao;

    @Resource
    private PositionEmpDao positionEmpDao;


    @Override
    public String save(Position position) {
        position.setDeleted(false);
        // 构建树形对象
        TreeBuilder<Position> treeBuilder = new TreeBuilder<Position>();
        treeBuilder.beforeSave(position);

        // 验证合法性
        validate(position);

        // 保存
        return positionDao.save(position);

    }

    private void validate(Position position) {
        ValidatorUtils.validate(position);

        // 验证编号是否重复
        boolean hasCode = positionDao.hasCode(position.getCode(), position.getId());
        Assert.isTrue(!hasCode, "操作失败!编号为[" + position.getCode() + "]的岗位已经存在!");

        // 验证同一级下名称是否重复
        boolean hasName = positionDao.hasName(position.getName(), position.getParentId(), position.getId());
        Assert.isTrue(!hasName, "操作失败!名称为[" + position.getName() + "]的岗位已经存在!(同一层级下岗位名称不能重复)");

    }

    @Override
    public void update(Position position) {
        // 验证
        validate(position);

        // 预设树形对象数据
        new TreeBuilder<Position>().beforeUpdate(position);

        // 更新
        positionDao.update(position);
    }

    @Override
    public PageVo pageQuery(PositionBo bo) {
        PageVo vo = new PageVo();
        Long total = positionDao.getTotal(bo);
        vo.setTotal(total);
        if (total == null || total == 0) return vo;
        List<Position> positionList = positionDao.query(bo);
        List<PositionVo> vos = BeanWrapBuilder.newInstance()
                .wrapList(positionList, PositionVo.class);
        vo.setData(vos);
        return vo;
    }


    @Override
    public PositionVo findById(String id) {
        Position position = positionDao.findById(id);
        return BeanWrapBuilder.newInstance()
                .wrap(position, PositionVo.class);
    }

    @Override
    public void disable(String[] ids) {
        if (ids == null || ids.length == 0) return;
        for (String id : ids) {
            if (StringUtils.isEmpty(id)) {
                continue;
            }
            Position position = positionDao.findById(id);
            Assert.notNull(position, "禁用失败!数据不存在，请刷新后重试!");
            position.setDeleted(true);

            // 禁用所有下级
            List<Position> children = positionDao.children(id);
            for (Position child : children) {
                child.setDeleted(true);
            }

        }
    }

    @Override
    public void enable(String[] ids) {
        if (ids == null || ids.length == 0) return;
        for (String id : ids) {
            Position position = positionDao.findById(id);
            Assert.notNull(position, "启用失败!数据不存在，请刷新后重试!");
            position.setDeleted(false);

            // 启用所有上级（利用path获取上级，所以必须保证path属性的正确性）
            if (StringUtils.isNotEmpty(position.getParentId())) {
                String path[] = position.getPath().split("/");
                for (String p : path) {
                    if (StringUtils.isEmpty(p) || p.equals(id)) {
                        continue;
                    }
                    Position parent = positionDao.findById(p);
                    Assert.notNull(parent, String.format("数据错误!岗位[%s(%s)]的上级岗位已经不存在，请与管理员联系处理!", position.getName(), position.getCode()));
                    parent.setDeleted(false);
                }
            }
        }
    }

    @Override
    public List<PositionVo> validTree() {
        PositionBo bo = new PositionBo();
        bo.setDeleted(false);
        List<Position> data = positionDao.query(bo);
        return BeanWrapBuilder.newInstance()
                .wrapList(data, PositionVo.class);
    }

    @Override
    public List<PositionVo> tree() {
        List<Position> data = positionDao.query(null);
        return BeanWrapBuilder.newInstance()
                .wrapList(data, PositionVo.class);
    }


    @Override
    public void doCallback(Position position, PositionVo vo) {
    }
}
