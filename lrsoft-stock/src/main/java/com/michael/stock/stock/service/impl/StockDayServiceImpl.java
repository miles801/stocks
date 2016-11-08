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
import com.michael.core.pager.Pager;
import com.michael.stock.stock.bo.StockDayBo;
import com.michael.stock.stock.dao.StockDayDao;
import com.michael.stock.stock.domain.StockDay;
import com.michael.stock.stock.service.StockDayService;
import com.michael.stock.stock.vo.StockDayVo;
import com.michael.utils.string.StringUtils;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.michael.core.hibernate.HibernateUtils.getSession;

/**
 * @author Michael
 */
@Service("stockDayService")
public class StockDayServiceImpl implements StockDayService, BeanWrapCallback<StockDay, StockDayVo> {
    @Resource
    private StockDayDao stockDayDao;

    @Override
    public String save(StockDay stockDay) {
        validate(stockDay);
        String id = stockDayDao.save(stockDay);
        return id;
    }

    @Override
    public void update(StockDay stockDay) {
        validate(stockDay);
        stockDayDao.update(stockDay);
    }

    private void validate(StockDay stockDay) {
        ValidatorUtils.validate(stockDay);
    }

    @Override
    public PageVo pageQuery(StockDayBo bo) {
        PageVo vo = new PageVo();
        if (Pager.getStart() == 0) {
            Long total = stockDayDao.getTotal(bo);
            vo.setTotal(total);
        }
        List<StockDay> stockDayList = stockDayDao.pageQuery(bo);
        List<StockDayVo> vos = BeanWrapBuilder.newInstance()
                .setCallback(this)
                .wrapList(stockDayList, StockDayVo.class);
        vo.setData(vos);
        return vo;
    }


    @Override
    public StockDayVo findById(String id) {
        StockDay stockDay = stockDayDao.findById(id);
        return BeanWrapBuilder.newInstance()
                .wrap(stockDay, StockDayVo.class);
    }

    @Override
    public void deleteByIds(String[] ids) {
        if (ids == null || ids.length == 0) return;
        for (String id : ids) {
            stockDayDao.deleteById(id);
        }
    }

    @Override
    public List<StockDayVo> query(StockDayBo bo) {
        List<StockDay> stockDayList = stockDayDao.query(bo);
        List<StockDayVo> vos = BeanWrapBuilder.newInstance()
                .setCallback(this)
                .wrapList(stockDayList, StockDayVo.class);
        return vos;
    }


    @Override
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> report3(StockDayBo bo) {
        Session session = HibernateUtils.getSession(false);
        String sql = "select code,s_key3 key1," +
                "sum(case isYang when 1 then 1 else 0 end) yang," +
                "sum(case isYang when 0 then 1 else 0 end) yin," +
                "sum(highPrice) high," +
                "sum(lowPrice) low from stock_day where 1=1 ";
        List<Object> params = new ArrayList<>();
        if (bo != null) {
            if (StringUtils.isNotEmpty(bo.getKey3())) {
                sql += " and s_key3=? ";
                params.add(bo.getKey3());
            }
            if (bo.getBusinessDate() != null) {
                sql += " and businessDate=?";
                params.add(bo.getBusinessDate());
            }
        }
        sql += "group by code,s_key3 ";
        Query query = session.createSQLQuery(sql);
        int index = 0;
        for (Object o : params) {
            query.setParameter(index++, o);
        }
        return query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
                .list();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> report6(StockDayBo bo) {
        Session session = HibernateUtils.getSession(false);
        String sql = "select code,s_key as key1," +
                "sum(case isYang when 1 then 1 else 0 end) yang," +
                "sum(case isYang when 0 then 1 else 0 end) yin," +
                "sum(highPrice) high," +
                "sum(lowPrice) low from stock_day where 1=1 ";
        List<Object> params = new ArrayList<>();
        if (bo != null) {
            if (StringUtils.isNotEmpty(bo.getKey())) {
                sql += " and s_key=? ";
                params.add(bo.getKey());
            }
            if (bo.getBusinessDate() != null) {
                sql += " and businessDate=?";
                params.add(bo.getBusinessDate());
            }
        }
        sql += " group by code,s_key ";
        Query query = session.createSQLQuery(sql);
        int index = 0;
        for (Object o : params) {
            query.setParameter(index++, o);
        }
        return query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
                .list();
    }

    @SuppressWarnings("unchecked")
    public void importData(String[] attachmentIds) {
        Logger logger = Logger.getLogger(StockDayServiceImpl.class);
        Assert.notEmpty(attachmentIds, "数据导入失败!数据文件不能为空，请重试!");
        Session session = getSession(false);
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
                LinkedList<StockDay> stocks = new LinkedList<>();  // 用于保存最近的6条记录
                for (int i = 0; i < 5; i++) {
                    StockDay sd = new StockDay();
                    sd.setP1(0d);
                    sd.setP2(0d);
                    sd.setP3(0d);
                    sd.setP4(0d);
                    sd.setP5(0d);
                    sd.setYesterdayClosePrice(0d);
                    sd.setKey("000000");
                    sd.setKey3("000");
                    sd.setClosePrice(0d);
                    sd.setUpdown(0d);
                    stocks.add(sd);
                }
                final int size = content.size();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

                for (int i = 1; i < size; i++) {
                    String line = content.get(i).replaceAll("\"", "");
                    String[] arr = line.split(",");
                    StockDay stockDay = new StockDay();
                    stockDay.setCode(arr[0]);
                    stockDay.setName(arr[1]);
                    stockDay.setBusinessDate(sdf.parse(arr[2]));
                    stockDay.setClosePrice(Double.parseDouble(arr[3]));
                    stockDay.setOpenPrice(Double.parseDouble(arr[4]));
                    stockDay.setLowPrice(Double.parseDouble(arr[5]));
                    stockDay.setHighPrice(Double.parseDouble(arr[6]));
                    StockDay last = stocks.getLast();
                    // 今日涨跌
                    stockDay.setUpdown(stockDay.getClosePrice() - last.getClosePrice());
                    // 第1日
                    stockDay.setKey(last.getKey().substring(1) + (stockDay.getUpdown() > 0 ? "1" : "0"));
                    stockDay.setKey3(last.getKey3().substring(1) + (stockDay.getUpdown() > 0 ? "1" : "0"));
                    stockDay.setDate3(stocks.get(3).getBusinessDate());
                    stockDay.setDate6(stocks.get(0).getBusinessDate());
                    stockDay.setYesterdayClosePrice(last.getClosePrice());
                    stockDay.setYang(stockDay.getOpenPrice() > stockDay.getClosePrice());   // 阴阳
                    if (last.getClosePrice() != 0d) {
                        stockDay.setP1((stockDay.getClosePrice() - last.getClosePrice()) / last.getClosePrice());
                        // 第2日
                        stockDay.setP2((stockDay.getClosePrice() - stocks.get(4).getClosePrice()) / last.getClosePrice());
                        // 第3日
                        stockDay.setP3((stockDay.getClosePrice() - stocks.get(3).getClosePrice()) / last.getClosePrice());
                        // 第4日
                        stockDay.setP4((stockDay.getClosePrice() - stocks.get(2).getClosePrice()) / last.getClosePrice());
                        // 第5日
                        stockDay.setP5((stockDay.getClosePrice() - stocks.get(1).getClosePrice()) / last.getClosePrice());
                        // 下一日高低
                        last.setNextHigh(stockDay.getHighPrice() - last.getClosePrice() / last.getClosePrice());
                        last.setNextLow(stockDay.getLowPrice() - last.getClosePrice() / last.getClosePrice());
                        session.update(last);
                    }

                    stocks.remove(0);
                    stocks.add(stockDay);
                    session.save(stockDay);
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
        // fixme 触发周K的定时器
    }

    @Override
    public void doCallback(StockDay stockDay, StockDayVo vo) {
        ParameterContainer container = ParameterContainer.getInstance();

    }
}
