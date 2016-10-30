package com.michael.stock.stock.service.impl;

import com.michael.base.attachment.AttachmentProvider;
import com.michael.base.attachment.utils.AttachmentHolder;
import com.michael.base.attachment.vo.AttachmentVo;
import com.michael.base.parameter.service.ParameterContainer;
import com.michael.core.beans.BeanWrapBuilder;
import com.michael.core.beans.BeanWrapCallback;
import com.michael.core.hibernate.HibernateUtils;
import com.michael.core.hibernate.validator.ValidatorUtils;
import com.michael.core.pager.PageVo;
import com.michael.stock.stock.bo.StockWeekBo;
import com.michael.stock.stock.dao.StockWeekDao;
import com.michael.stock.stock.domain.StockWeek;
import com.michael.stock.stock.service.StockWeekService;
import com.michael.stock.stock.vo.StockWeekVo;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * @author Michael
 */
@Service("stockWeekService")
public class StockWeekServiceImpl implements StockWeekService, BeanWrapCallback<StockWeek, StockWeekVo> {
    @Resource
    private StockWeekDao stockWeekDao;

    @Override
    public String save(StockWeek stockWeek) {
        validate(stockWeek);
        String id = stockWeekDao.save(stockWeek);
        return id;
    }

    @Override
    public void update(StockWeek stockWeek) {
        validate(stockWeek);
        stockWeekDao.update(stockWeek);
    }

    private void validate(StockWeek stockWeek) {
        ValidatorUtils.validate(stockWeek);
    }

    @Override
    public PageVo pageQuery(StockWeekBo bo) {
        PageVo vo = new PageVo();
        Long total = stockWeekDao.getTotal(bo);
        vo.setTotal(total);
        if (total == null || total == 0) return vo;
        List<StockWeek> stockWeekList = stockWeekDao.pageQuery(bo);
        List<StockWeekVo> vos = BeanWrapBuilder.newInstance()
                .setCallback(this)
                .wrapList(stockWeekList, StockWeekVo.class);
        vo.setData(vos);
        return vo;
    }


    @Override
    public StockWeekVo findById(String id) {
        StockWeek stockWeek = stockWeekDao.findById(id);
        return BeanWrapBuilder.newInstance()
                .wrap(stockWeek, StockWeekVo.class);
    }

    @Override
    public void deleteByIds(String[] ids) {
        if (ids == null || ids.length == 0) return;
        for (String id : ids) {
            stockWeekDao.deleteById(id);
        }
    }

    @Override
    public List<StockWeekVo> query(StockWeekBo bo) {
        List<StockWeek> stockWeekList = stockWeekDao.query(bo);
        List<StockWeekVo> vos = BeanWrapBuilder.newInstance()
                .setCallback(this)
                .wrapList(stockWeekList, StockWeekVo.class);
        return vos;
    }


    public void importData(String[] attachmentIds) {
        Logger logger = Logger.getLogger(StockDayServiceImpl.class);
        Assert.notEmpty(attachmentIds, "数据导入失败!数据文件不能为空，请重试!");
        Session session = HibernateUtils.getSession(false);
        int index = 0;
        int x = 1;
        for (String id : attachmentIds) {
            AttachmentVo vo = AttachmentProvider.getInfo(id);
            Assert.notNull(vo, "附件已经不存在，请刷新后重试!");
            final File file = AttachmentHolder.newInstance().getTempFile(id);
            long start = System.currentTimeMillis();
            logger.info(String.format("开始导入数据%d/%d....", x++, attachmentIds.length));
            try {
                List<String> content = FileUtils.readLines(file);
                final int size = content.size();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                for (int i = 1; i < size; i++) {
                    String line = content.get(i).replaceAll("\"", "");
                    String[] arr = line.split(",");
                    StockWeek sw = new StockWeek();
                    sw.setCode(arr[0]);
                    sw.setName(arr[1]);
                    sw.setBusinessDate(sdf.parse(arr[2]));
                    sw.setKey(arr[3]);
                    session.save(sw);
                    index++;
                    if (index % 20 == 0) {
                        session.flush();
                        session.clear();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            logger.info(String.format("导入数据成功,用时(%d)s，共导入%d条数据....", (System.currentTimeMillis() - start) / 1000, index));
        }
    }

    @Override
    public void doCallback(StockWeek stockWeek, StockWeekVo vo) {
        ParameterContainer container = ParameterContainer.getInstance();

    }
}
