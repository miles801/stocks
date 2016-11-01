package com.michael.stock.db.service.impl;

import com.michael.base.parameter.service.ParameterContainer;
import com.michael.core.beans.BeanWrapBuilder;
import com.michael.core.beans.BeanWrapCallback;
import com.michael.core.hibernate.HibernateUtils;
import com.michael.core.hibernate.validator.ValidatorUtils;
import com.michael.core.pager.PageVo;
import com.michael.stock.db.bo.FnDBBo;
import com.michael.stock.db.dao.FnDBDao;
import com.michael.stock.db.domain.FnDB;
import com.michael.stock.db.service.FnDBService;
import com.michael.stock.db.vo.FnDBVo;
import com.michael.utils.date.DateUtils;
import com.michael.utils.number.IntegerUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author Michael
 */
@Service("fnDBService")
public class FnDBServiceImpl implements FnDBService, BeanWrapCallback<FnDB, FnDBVo> {
    @Resource
    private FnDBDao fnDBDao;

    @Override
    public String save(FnDB fnDB) {
        validate(fnDB);
        String id = fnDBDao.save(fnDB);
        return id;
    }

    @Override
    public void update(FnDB fnDB) {
        validate(fnDB);
        fnDBDao.update(fnDB);
    }

    private void validate(FnDB fnDB) {
        ValidatorUtils.validate(fnDB);
    }

    @Override
    public PageVo pageQuery(FnDBBo bo) {
        PageVo vo = new PageVo();
        Long total = fnDBDao.getTotal(bo);
        vo.setTotal(total);
        if (total == null || total == 0) return vo;
        List<FnDB> fnDBList = fnDBDao.pageQuery(bo);
        List<FnDBVo> vos = BeanWrapBuilder.newInstance().setCallback(this).wrapList(fnDBList, FnDBVo.class);
        vo.setData(vos);
        return vo;
    }


    @Override
    public FnDBVo findById(String id) {
        FnDB fnDB = fnDBDao.findById(id);
        return BeanWrapBuilder.newInstance().wrap(fnDB, FnDBVo.class);
    }

    @Override
    public void deleteByIds(String[] ids) {
        if (ids == null || ids.length == 0) return;
        for (String id : ids) {
            fnDBDao.deleteById(id);
        }
    }

    @Override
    public void add(String type, Date dbDate) {
        Assert.hasText(type, "操作失败!数据库不能为空!");
        Assert.notNull(dbDate, "操作失败!计算日期不能为空!");
        // 设定最大时间为2050年1月1日
        long maxDate = DateUtils.getDate(2050, 1, 1).getTime();
        int i = 2;
        while (true) {
            Date newDate = org.apache.commons.lang3.time.DateUtils.addDays(dbDate, (int) (Math.sqrt(IntegerUtils.fn(i)) * 29.5306d));
            if (newDate.getTime() > maxDate) {
                break;
            }
            FnDB fn = new FnDB();
            fn.setType(type);
            fn.setOriginDate(dbDate);
            fn.setFnDate(newDate);
            fn.setFn(i);
            fnDBDao.save(fn);
            i++;
        }
    }

    @Override
    public void delete(String type, Date dbDate) {
        Assert.hasText(type, "操作失败!数据库不能为空!");
        Assert.notNull(dbDate, "操作失败!计算日期不能为空!");
        HibernateUtils.getSession(false)
                .createQuery("delete from " + FnDB.class.getName() + " fn where fn.originDate=? and fn.type=?")
                .setParameter(0, dbDate)
                .setParameter(1, type)
                .executeUpdate();
    }

    @Override
    public List<FnDBVo> query(FnDBBo bo) {
        List<FnDB> fnDBList = fnDBDao.query(bo);
        List<FnDBVo> vos = BeanWrapBuilder.newInstance()
                .setCallback(this)
                .wrapList(fnDBList, FnDBVo.class);
        return vos;
    }


    @Override
    public void doCallback(FnDB fnDB, FnDBVo vo) {
        ParameterContainer container = ParameterContainer.getInstance();

        // 所属数据库
        vo.setTypeName(container.getSystemName("DB_TYPE", fnDB.getType()));

    }
}
