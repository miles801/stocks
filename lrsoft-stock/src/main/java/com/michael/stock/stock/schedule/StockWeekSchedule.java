package com.michael.stock.stock.schedule;

import com.michael.core.SystemContainer;
import com.michael.core.hibernate.HibernateUtils;
import com.michael.stock.stock.domain.StockDay;
import com.michael.stock.stock.service.StockWeekService;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Michael
 */
@Component
public class StockWeekSchedule {

    @SuppressWarnings("unchecked")
    public void execute() {
        Logger logger = Logger.getLogger(StockWeekSchedule.class);
        logger.info("****************** 初始化股票周交易数据:start ******************");
        // 获取所有的股票代码
        try (Session session = HibernateUtils.openSession()) {
            List<String> codes = session.createQuery("select distinct o.code from " + StockDay.class.getName() + " o group by o.code ")
                    .list();
            StockWeekService stockWeekService = SystemContainer.getInstance().getBean(StockWeekService.class);
            int index = 0;
            int size = codes.size();
            for (String code : codes) {
                stockWeekService.reset(code);
                logger.info(String.format("股票周交易数据初始化成功：%s,总进度：%d / %d", code, ++index, size));
            }
        }
        logger.info("****************** 初始化股票周交易数据:start ******************");
    }


    public void add() {
        StockWeekService stockWeekService = SystemContainer.getInstance().getBean(StockWeekService.class);
        stockWeekService.add("000001");
    }
}
