package com.michael.stock.stock.service.impl;

import com.michael.base.attachment.AttachmentProvider;
import com.michael.base.attachment.utils.AttachmentHolder;
import com.michael.base.attachment.vo.AttachmentVo;
import com.michael.base.parameter.service.ParameterContainer;
import com.michael.core.beans.BeanWrapBuilder;
import com.michael.core.beans.BeanWrapCallback;
import com.michael.core.hibernate.HibernateUtils;
import com.michael.core.hibernate.validator.ValidatorUtils;
import com.michael.core.pager.Order;
import com.michael.core.pager.PageVo;
import com.michael.core.pager.Pager;
import com.michael.stock.stock.bo.StockDayBo;
import com.michael.stock.stock.dao.StockDayDao;
import com.michael.stock.stock.domain.StockDay;
import com.michael.stock.stock.domain.StockWeek;
import com.michael.stock.stock.service.StockDayService;
import com.michael.stock.stock.service.StockRequestInstance;
import com.michael.stock.stock.vo.StockDayVo;
import com.michael.utils.date.DateUtils;
import com.michael.utils.string.StringUtils;
import com.miles.stock.core.Configuration;
import com.miles.stock.sina.SinaStockAdapter;
import com.miles.stock.utils.StockUtils;
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
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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
    public Map<String, Object> syncStockBusiness(String... stocks) {
        if (stocks == null || stocks.length == 0) {
            return null;
        }
        if (Configuration.getInstance().getStockAdapter() == null) {
            Configuration.getInstance().setStockAdapter(new SinaStockAdapter());
        }
        String content = StockRequestInstance.getInstance().getStockRequest().get(stocks);
        String stockResult[] = content.split(";");
        List<com.miles.stock.domain.Stock> stockList = StockUtils.wrapToStock(stockResult);
        Date date = DateUtils.getDate(new Date(), 0, 0, 0);
        if (stockList != null) {
            Session session = HibernateUtils.getSession(false);
            for (com.miles.stock.domain.Stock s : stockList) {
                // 如果该条数据已经有交易数据，则跳过
                final String code = s.getCode();
                String id = (String) session.createQuery("select sd.id from " + StockDay.class.getName() + " sd where sd.businessDate=? and sd.code=?")
                        .setParameter(0, date)
                        .setParameter(1, code)
                        .setFirstResult(0)
                        .setMaxResults(1)
                        .uniqueResult();
                if (StringUtils.isNotEmpty(id)) {
                    continue;
                }
                // 获取今天之前5天的交易数据 ,如果不足5日，则添加0补足
                List<StockDay> history = session.createQuery("from " + StockDay.class.getName() + " sd where sd.businessDate<? and sd.code=? order by sd.businessDate desc")
                        .setParameter(0, date)
                        .setParameter(1, code)
                        .setFirstResult(0)
                        .setMaxResults(5)
                        .list();
                int k = 5 - history.size();
                for (int i = 0; i < k; i++) {
                    StockDay foo = new StockDay();
                    foo.setP1(0d);
                    foo.setP2(0d);
                    foo.setP3(0d);
                    foo.setP4(0d);
                    foo.setP5(0d);
                    foo.setYesterdayClosePrice(0d);
                    foo.setKey("000000");
                    foo.setKey3("000");
                    foo.setClosePrice(0d);
                    foo.setUpdown(0d);
                    history.add(foo);
                }
                StockDay stockDay = new StockDay();
                stockDay.setCode(code);
                stockDay.setName(s.getName());
                stockDay.setBusinessDate(date);
                stockDay.setHighPrice(Double.parseDouble(s.getTodayHighPrice().toString()));
                stockDay.setLowPrice(Double.parseDouble(s.getTodayLowPrice().toString()));
                stockDay.setOpenPrice(Double.parseDouble(s.getOpenPrice().toString()));
                stockDay.setClosePrice(Double.parseDouble(s.getClosePrice().toString()));
                stockDay.setYesterdayClosePrice(Double.parseDouble(s.getYesterdayClosePrice().toString()));
                stockDay.setUpdown(stockDay.getClosePrice() - stockDay.getOpenPrice());
                // 6日时间&组合
                stockDay.setDate6(history.get(4).getBusinessDate());
                final StockDay yesterday = history.get(0);
                stockDay.setKey(yesterday.getKey().substring(1) + (stockDay.getUpdown() > 0 ? "1" : "0"));

                // 3日时间&组合
                stockDay.setDate3(history.get(1).getBusinessDate());
                stockDay.setKey3(yesterday.getKey3().substring(1) + (stockDay.getUpdown() > 0 ? "1" : "0"));

                // 七日阴阳
                if (stockDay.getClosePrice() - stockDay.getOpenPrice() == 0) {
                    stockDay.setYang(yesterday.getYang());
                } else {
                    stockDay.setYang(stockDay.getClosePrice() - stockDay.getOpenPrice() > 0);
                }

                // 第1日
                stockDay.setYesterdayClosePrice(yesterday.getClosePrice());
                if (yesterday.getClosePrice() != 0d) {
                    stockDay.setP1((stockDay.getClosePrice() - yesterday.getClosePrice()) / yesterday.getClosePrice());
                    // 第2日
                    stockDay.setP2((stockDay.getClosePrice() - history.get(1).getClosePrice()) / yesterday.getClosePrice());
                    // 第3日
                    stockDay.setP3((stockDay.getClosePrice() - history.get(2).getClosePrice()) / yesterday.getClosePrice());
                    // 第4日
                    stockDay.setP4((stockDay.getClosePrice() - history.get(3).getClosePrice()) / yesterday.getClosePrice());
                    // 第5日
                    stockDay.setP5((stockDay.getClosePrice() - history.get(4).getClosePrice()) / yesterday.getClosePrice());

                    // 第七日高
                    yesterday.setNextHigh(stockDay.getHighPrice() - yesterday.getClosePrice() / yesterday.getClosePrice());
                    // 第七日低
                    yesterday.setNextLow(stockDay.getLowPrice() - yesterday.getClosePrice() / yesterday.getClosePrice());

                    session.update(yesterday);
                }
                history.remove(4);
                history.add(0, stockDay);

                stockDayDao.save(stockDay);
            }
        }
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> report3(StockDayBo bo) {
        Session session = HibernateUtils.getSession(false);
        String sql = "select s_key3 as key1," +
                "sum(case isYang when 1 then 1 else 0 end) as yang," +
                "sum(nextHigh) nextHigh,sum(nextLow) nextLow,count(id) counts " +
                "from stock_day where businessDate BETWEEN ? and ? and code =? " +
                "group by s_key3 ";
        List<Object> params = new ArrayList<>();
        params.add(bo.getBusinessDateGe());
        params.add(bo.getBusinessDateLt());
        params.add(bo.getCode());
        if (Pager.getOrder() != null && Pager.getOrder().hasNext()) {
            Order o = Pager.getOrder().next();
            sql += " order by " + o.getName() + (o.isReverse() ? " desc " : " asc ");
        } else {
            sql += " order by s_key3 asc ";
        }
        Query query = session.createSQLQuery(sql);
        int index = 0;
        for (Object o : params) {
            query.setParameter(index++, o);
        }
        return query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
                .list();
    }

    public PageVo result3(StockDayBo bo) {
        PageVo vo = new PageVo();
        Session session = HibernateUtils.getSession(false);
        String sql = "select code,s_key3 as key1," +
                "sum(case isYang when 1 then 1 else 0 end) as yang," +
                "sum(nextHigh) nextHigh,sum(nextLow) nextLow,count(id) counts " +
                "from stock_day where 1=1 ";
        List<Object> params = new ArrayList<>();
//        params.add(bo.getBusinessDateGe());
//        params.add(bo.getBusinessDateLt());
        if (StringUtils.isNotEmpty(bo.getKey3())) {
            sql += " and s_key3=? ";
            params.add(bo.getKey3());
        }
        if (StringUtils.isNotEmpty(bo.getCode())) {
            sql += " and code=? ";
            params.add(bo.getCode());
        }
        sql += " group by code,s_key3 ";

        int index = 0;
        if (Pager.getStart() == null || Pager.getStart() == 0) {
            String totalSql = "select count(a.code) from (" + sql + ") a";
            Query totalQuery = session.createSQLQuery(totalSql);
            for (Object o : params) {
                totalQuery.setParameter(index++, o);
            }
            BigInteger bigInteger = (BigInteger) totalQuery.uniqueResult();
            vo.setTotal(bigInteger.longValue());
            if (vo.getTotal() == 0) {
                return vo;
            }
        }

        if (Pager.getOrder() != null && Pager.getOrder().hasNext()) {
            Order o = Pager.getOrder().next();
            sql += " order by " + o.getName() + (o.isReverse() ? " desc " : " asc ");
        } else {
            sql += " order by s_key3 asc ";
        }
        Query query = session.createSQLQuery(sql);
        index = 0;
        for (Object o : params) {
            query.setParameter(index++, o);
        }
        List<Map<String, Object>> data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
                .setFirstResult(Pager.getStart())
                .setMaxResults(Pager.getLimit())
                .list();
        vo.setData(data);
        return vo;
    }

    @Override
    @SuppressWarnings("unchecked")
    public PageVo result6(StockDayBo bo) {
        PageVo vo = new PageVo();
        Session session = HibernateUtils.getSession(false);
        String sql = "select code,s_key as key1," +
                "sum(case isYang when 1 then 1 else 0 end) as yang," +
                "sum(nextHigh) nextHigh,sum(nextLow) nextLow,count(id) counts " +
                "from stock_day where 1=1 ";
        List<Object> params = new ArrayList<>();
//        params.add(bo.getBusinessDateGe());
//        params.add(bo.getBusinessDateLt());
        if (StringUtils.isNotEmpty(bo.getKey())) {
            sql += " and s_key=? ";
            params.add(bo.getKey());
        }
        if (StringUtils.isNotEmpty(bo.getCode())) {
            sql += " and code=? ";
            params.add(bo.getCode());
        }
        sql += " group by code,s_key ";

        int index = 0;
        if (Pager.getStart() == null || Pager.getStart() == 0) {
            String totalSql = "select count(a.code) from (" + sql + ") a";
            Query totalQuery = session.createSQLQuery(totalSql);
            for (Object o : params) {
                totalQuery.setParameter(index++, o);
            }
            BigInteger bigInteger = (BigInteger) totalQuery.uniqueResult();
            vo.setTotal(bigInteger.longValue());
            if (vo.getTotal() == 0) {
                return vo;
            }
        }

        if (Pager.getOrder() != null && Pager.getOrder().hasNext()) {
            Order o = Pager.getOrder().next();
            sql += " order by " + o.getName() + (o.isReverse() ? " desc " : " asc ");
        } else {
            sql += " order by s_key asc ";
        }
        Query query = session.createSQLQuery(sql);
        index = 0;
        for (Object o : params) {
            query.setParameter(index++, o);
        }
        List<Map<String, Object>> data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
                .setFirstResult(Pager.getStart())
                .setMaxResults(Pager.getLimit())
                .list();
        vo.setData(data);
        return vo;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> report6(StockDayBo bo) {
        Session session = HibernateUtils.getSession(false);
        String sql = "select s_key as key1," +
                "sum(case isYang when 1 then 1 else 0 end) as yang," +
                "sum(nextHigh) nextHigh,sum(nextLow) nextLow,count(id) counts " +
                "from stock_day where businessDate BETWEEN ? and ? and code =? " +
                "group by s_key ";
        List<Object> params = new ArrayList<>();
        params.add(bo.getBusinessDateGe());
        params.add(bo.getBusinessDateLt());
        params.add(bo.getCode());
        if (Pager.getOrder() != null && Pager.getOrder().hasNext()) {
            Order o = Pager.getOrder().next();
            sql += " order by " + o.getName() + (o.isReverse() ? " desc " : " asc ");
        } else {
            sql += " order by s_key asc ";
        }
        Query query = session.createSQLQuery(sql);
        int index = 0;
        for (Object o : params) {
            query.setParameter(index++, o);
        }
        return query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
                .list();
    }


    @Override
    public Date lastDay() {
        return (Date) HibernateUtils.getSession(false)
                .createQuery("select distinct max(o.businessDate) from " + StockDay.class.getName() + " o ")
                .setMaxResults(1)
                .uniqueResult();
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
                List<String> content = FileUtils.readLines(file, "gbk");
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
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                String titles[] = content.get(0).split("\\s+");
                String code = titles[0];
                String name = titles[1];
                // 清空这支股票的历史数据
                session.createQuery("delete from " + StockDay.class.getName() + " s where s.code=?")
                        .setParameter(0, code)
                        .executeUpdate();
                session.createQuery("delete from " + StockWeek.class.getName() + " s where s.code=?")
                        .setParameter(0, code)
                        .executeUpdate();
                for (int i = 1; i < size; i++) {
                    String[] arr = content.get(i).split(";");
                    if (arr.length != 7) {
                        continue;
                    }
                    StockDay stockDay = new StockDay();
                    stockDay.setCode(code);
                    stockDay.setName(name);
                    stockDay.setBusinessDate(sdf.parse(arr[0]));
                    stockDay.setOpenPrice(Double.parseDouble(arr[1]));
                    stockDay.setHighPrice(Double.parseDouble(arr[2]));
                    stockDay.setLowPrice(Double.parseDouble(arr[3]));
                    stockDay.setClosePrice(Double.parseDouble(arr[4]));
                    stockDay.setUpdown(stockDay.getClosePrice() - stockDay.getOpenPrice());
                    // 6日时间&组合
                    StockDay last = stocks.getLast();
                    stockDay.setDate6(stocks.get(0).getBusinessDate());
                    stockDay.setKey(last.getKey().substring(1) + (stockDay.getUpdown() > 0 ? "1" : "0"));

                    // 3日时间&组合
                    stockDay.setKey3(last.getKey3().substring(1) + (stockDay.getUpdown() > 0 ? "1" : "0"));
                    stockDay.setDate3(stocks.get(3).getBusinessDate());

                    // 七日阴阳
                    if (stockDay.getClosePrice() - stockDay.getOpenPrice() == 0) {
                        stockDay.setYang(last.getYang());
                    } else {
                        stockDay.setYang(stockDay.getClosePrice() - stockDay.getOpenPrice() > 0);
                    }

                    // 第1日
                    stockDay.setYesterdayClosePrice(last.getClosePrice());
                    if (last.getClosePrice() != 0d) {
                        stockDay.setP1((stockDay.getClosePrice() - last.getClosePrice()) / last.getClosePrice());
                        // 第2日
                        stockDay.setP2((stockDay.getClosePrice() - stocks.get(3).getClosePrice()) / last.getClosePrice());
                        // 第3日
                        stockDay.setP3((stockDay.getClosePrice() - stocks.get(2).getClosePrice()) / last.getClosePrice());
                        // 第4日
                        stockDay.setP4((stockDay.getClosePrice() - stocks.get(1).getClosePrice()) / last.getClosePrice());
                        // 第5日
                        stockDay.setP5((stockDay.getClosePrice() - stocks.get(0).getClosePrice()) / last.getClosePrice());

                        // 第七日高
                        last.setNextHigh(stockDay.getHighPrice() - last.getClosePrice() / last.getClosePrice());
                        // 第七日低
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
    }

    @Override
    public void doCallback(StockDay stockDay, StockDayVo vo) {
        ParameterContainer container = ParameterContainer.getInstance();

    }
}
