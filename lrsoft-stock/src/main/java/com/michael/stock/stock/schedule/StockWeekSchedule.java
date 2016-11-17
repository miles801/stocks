package com.michael.stock.stock.schedule;

import com.michael.core.hibernate.HibernateUtils;
import com.michael.core.pool.ThreadPool;
import com.michael.stock.stock.domain.StockDay;
import com.michael.utils.number.IntegerUtils;
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
        // 获取所有的股票编号
        try (Session session = HibernateUtils.openSession()) {
            List<String> codes = session.createQuery("select o.code from " + StockDay.class.getName() + " o group by o.code ")
                    .setMaxResults(5)
                    .list();
            int size = codes.size();
            final int batch = 5;
            long times = IntegerUtils.times(size, batch);
            for (int i = 0; i < times; i++) {
                int last = (i + 1) * batch;
                if (last > size) {
                    last = size;
                }
                ThreadPool.getInstance().execute(new StockWeekThread(codes.subList(i * batch, last).toArray(new String[]{})));
            }
        }
        logger.info("****************** 初始化股票周交易数据:start ******************");
    }


    public void add() {

    }
}
