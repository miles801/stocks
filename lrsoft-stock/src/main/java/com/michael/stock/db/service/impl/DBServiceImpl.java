package com.michael.stock.db.service.impl;

import com.michael.base.parameter.service.ParameterContainer;
import com.michael.core.beans.BeanWrapBuilder;
import com.michael.core.beans.BeanWrapCallback;
import com.michael.core.hibernate.HibernateUtils;
import com.michael.core.hibernate.validator.ValidatorUtils;
import com.michael.core.pager.PageVo;
import com.michael.stock.db.bo.DBBo;
import com.michael.stock.db.dao.DBDao;
import com.michael.stock.db.dao.FnDBDao;
import com.michael.stock.db.domain.DB;
import com.michael.stock.db.service.DBService;
import com.michael.stock.db.service.FnDBService;
import com.michael.stock.db.vo.DBVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author Michael
 */
@Service("dBService")
public class DBServiceImpl implements DBService, BeanWrapCallback<DB, DBVo> {
    @Resource
    private DBDao dBDao;

    @Resource
    private FnDBDao fnDBDao;

    @Resource
    private FnDBService fnDBService;

    @Override
    public String save(DB dB) {
        validate(dB);
        String id = dBDao.save(dB);

        // 计算某个日期的fn数据库
        fnDBService.add(dB.getType(), dB.getDbDate());

        return id;
    }

    @Override
    public void update(DB dB) {
        validate(dB);
        Date originDate = (Date) HibernateUtils.getSession(false)
                .createQuery("select d.dbDate from " + DB.class.getName() + " d where d.id=?")
                .setParameter(0, dB.getId())
                .uniqueResult();
        if (originDate.getTime() != dB.getDbDate().getTime()) {
            fnDBService.delete(dB.getType(), originDate);
            fnDBService.add(dB.getType(), dB.getDbDate());
        }
        dBDao.update(dB);
    }

    private void validate(DB dB) {
        ValidatorUtils.validate(dB);
    }

    @Override
    public PageVo pageQuery(DBBo bo) {
        PageVo vo = new PageVo();
        Long total = dBDao.getTotal(bo);
        vo.setTotal(total);
        if (total == null || total == 0) return vo;
        List<DB> dBList = dBDao.pageQuery(bo);
        List<DBVo> vos = BeanWrapBuilder.newInstance().setCallback(this).wrapList(dBList, DBVo.class);
        vo.setData(vos);
        return vo;
    }


    @Override
    public DBVo findById(String id) {
        DB dB = dBDao.findById(id);
        return BeanWrapBuilder.newInstance().wrap(dB, DBVo.class);
    }

    @Override
    public void deleteByIds(String[] ids) {
        if (ids == null || ids.length == 0) return;
        for (String id : ids) {
            DB db = dBDao.findById(id);
            if (db != null) {
                fnDBService.delete(db.getType(), db.getDbDate());
                dBDao.delete(db);
            }
        }
    }

    @Override
    public List<DBVo> query(DBBo bo) {
        List<DB> dBList = dBDao.query(bo);
        List<DBVo> vos = BeanWrapBuilder.newInstance().setCallback(this).wrapList(dBList, DBVo.class);
        return vos;
    }


    @Override
    public void doCallback(DB dB, DBVo vo) {
        ParameterContainer container = ParameterContainer.getInstance();

        // 类型
        vo.setTypeName(container.getSystemName("DB_TYPE", dB.getType()));

    }
}
