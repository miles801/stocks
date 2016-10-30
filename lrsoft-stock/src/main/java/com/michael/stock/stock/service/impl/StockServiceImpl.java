package com.michael.stock.stock.service.impl;

import com.michael.base.attachment.AttachmentProvider;
import com.michael.base.attachment.utils.AttachmentHolder;
import com.michael.base.attachment.vo.AttachmentVo;
import com.michael.base.parameter.service.ParameterContainer;
import com.michael.core.SystemContainer;
import com.michael.core.beans.BeanWrapBuilder;
import com.michael.core.beans.BeanWrapCallback;
import com.michael.core.hibernate.HibernateUtils;
import com.michael.core.hibernate.validator.ValidatorUtils;
import com.michael.core.pager.PageVo;
import com.michael.poi.adapter.AnnotationCfgAdapter;
import com.michael.poi.core.Context;
import com.michael.poi.core.Handler;
import com.michael.poi.core.ImportEngine;
import com.michael.poi.core.RuntimeContext;
import com.michael.poi.imp.cfg.Configuration;
import com.michael.stock.stock.bo.StockBo;
import com.michael.stock.stock.dao.StockDao;
import com.michael.stock.stock.domain.Stock;
import com.michael.stock.stock.dto.StockDTO;
import com.michael.stock.stock.service.StockService;
import com.michael.stock.stock.vo.StockVo;
import com.michael.utils.beans.BeanCopyUtils;
import com.michael.utils.string.StringUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Michael
 */
@Service("stockService")
public class StockServiceImpl implements StockService, BeanWrapCallback<Stock, StockVo> {
    @Resource
    private StockDao stockDao;

    private Logger logger = Logger.getLogger(StockServiceImpl.class);
    Map<Integer, String> map = new HashMap<Integer, String>() {{
        put(1, "000");
        put(2, "002");
        put(3, "300");
        put(4, "600");
    }};

    @Override
    public String save(Stock stock) {
        validate(stock);
        String id = stockDao.save(stock);
        logger.info(String.format("========= 新增股票 : %s,%s ===============", stock.getCode(), stock.getName()));
        return id;
    }

    @Override
    public void update(Stock stock) {
        validate(stock);
        stockDao.update(stock);
    }

    private void validate(Stock stock) {
        ValidatorUtils.validate(stock);

        String code = stock.getCode();
        Assert.isTrue(code.matches("\\d{6}"), "操作失败!错误的股票代码!");

        // 设置类型
        String key = code.substring(0, 3);
        int type = getType(key);
        stock.setType(type);
        // 验证重复 - 编号
        boolean hasCode = stockDao.hasCode(stock.getCode(), stock.getId());
        Assert.isTrue(!hasCode, "操作失败!编号[" + stock.getCode() + "]已经存在!");


        // 验证重复 - 名称
        boolean hasName = stockDao.hasName(stock.getName(), stock.getId());
        Assert.isTrue(!hasName, "操作失败!名称[" + stock.getName() + "]已经存在!");

    }

    private int getType(String key) {
        int type = 1;
        for (Map.Entry<Integer, String> entry : map.entrySet()) {
            if (entry.getValue().equals(key)) {
                type = entry.getKey();
                break;
            }
        }
        return type;
    }

    @Override
    public PageVo pageQuery(StockBo bo) {
        PageVo vo = new PageVo();
        Long total = stockDao.getTotal(bo);
        vo.setTotal(total);
        if (total == null || total == 0) return vo;
        List<Stock> stockList = stockDao.pageQuery(bo);
        List<StockVo> vos = BeanWrapBuilder.newInstance()
                .setCallback(this)
                .wrapList(stockList, StockVo.class);
        vo.setData(vos);
        return vo;
    }


    @Override
    public StockVo findById(String id) {
        Stock stock = stockDao.findById(id);
        return BeanWrapBuilder.newInstance()
                .wrap(stock, StockVo.class);
    }

    @Override
    public void deleteByIds(String[] ids) {
        if (ids == null || ids.length == 0) return;
        for (String id : ids) {
            stockDao.deleteById(id);
        }
    }

    @Override
    public List<StockVo> query(StockBo bo) {
        List<Stock> stockList = stockDao.query(bo);
        List<StockVo> vos = BeanWrapBuilder.newInstance()
                .setCallback(this)
                .wrapList(stockList, StockVo.class);
        return vos;
    }

    @Override
    public Map<String, Object> syncStock() {
        // 从指定网站获取列表，然后解析
        String url = "http://quote.eastmoney.com/stocklist.html";
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpget = new HttpGet(url);
        List<String> codes = stockDao.queryCode();
        Session session = HibernateUtils.getSession(false);
        int i = 0;  // 添加的总个数
        try {
            HttpResponse response = httpClient.execute(httpget);
            int status = response.getStatusLine().getStatusCode();
            if (status == 200) {
                InputStream input = response.getEntity().getContent();
                String content = IOUtils.toString(input, "gb2312");//新浪股票接口使用的是GBK编码
                input.close();
                Pattern pattern = Pattern.compile("<li><a .* href=.+>(.+)\\((\\d{6})\\)</a></li>");
                Matcher matcher = pattern.matcher(content);
                while (matcher.find()) {
                    String code = matcher.group(2);
                    if (codes.contains(code)) {
                        continue;
                    }
                    String name = matcher.group(1);
                    Stock stock = new Stock();
                    stock.setCode(code);
                    stock.setName(name);
                    stockDao.save(stock);
                    codes.add(code);
                    i++;
                    if (i % 20 == 0) {
                        session.flush();
                        session.clear();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            Assert.isTrue(false, e.getMessage());
        }
        httpClient.getConnectionManager().shutdown();
        Map<String, Object> map = new HashMap<>();
        map.put("add", i);
        return map;
    }

    public void importData(String[] attachmentIds) {
        Logger logger = Logger.getLogger(StockServiceImpl.class);
        Assert.notEmpty(attachmentIds, "数据导入失败!数据文件不能为空，请重试!");
        final List<String> codes = stockDao.queryCode();
        for (String id : attachmentIds) {
            AttachmentVo vo = AttachmentProvider.getInfo(id);
            Assert.notNull(vo, "附件已经不存在，请刷新后重试!");
            final File file = AttachmentHolder.newInstance().getTempFile(id);
            logger.info("准备导入数据：" + file.getAbsolutePath());
            logger.info("初始化导入引擎....");
            final long start = System.currentTimeMillis();

            // 初始化引擎
            Configuration configuration = new AnnotationCfgAdapter(StockDTO.class).parse();
            configuration.setStartRow(1);
            String newFilePath = file.getAbsolutePath() + vo.getFileName().substring(vo.getFileName().lastIndexOf(".")); //获取路径
            try {
                FileUtils.copyFile(file, new File(newFilePath));
            } catch (IOException e) {
                e.printStackTrace();
            }
            // 获取session
            SessionFactory sessionFactory = (SessionFactory) SystemContainer.getInstance().getBean("sessionFactory");
            final Session session = sessionFactory.getCurrentSession();
            configuration.setPath(newFilePath);
            configuration.setHandler(new Handler<StockDTO>() {
                @Override
                public void execute(StockDTO dto) {
                    Context context = RuntimeContext.get();
                    Stock stock = new Stock();
                    BeanUtils.copyProperties(dto, stock);
                    if (BeanCopyUtils.isEmpty(stock)) {
                        return;
                    }
                    String code = stock.getCode();
                    if (StringUtils.isEmpty(code)) {
                        return;
                    }
                    // 去重
                    if (codes.contains(code)) {
                        return;
                    }
                    session.save(stock);
                    if (context.getRowIndex() % 10 == 0) {
                        session.flush();
                        session.clear();
                    }
                }
            });
            logger.info("开始导入数据....");
            ImportEngine engine = new ImportEngine(configuration);
            try {
                engine.execute();
            } catch (Exception e) {
                Assert.isTrue(false, String.format("数据异常!发生在第%d行,%d列!原因:%s", RuntimeContext.get().getRowIndex(), RuntimeContext.get().getCellIndex(), e.getCause() == null ? e.getMessage() : e.getCause().getMessage()));
            }
            logger.info(String.format("导入数据成功,用时(%d)s....", (System.currentTimeMillis() - start) / 1000));
            new File(newFilePath).delete();

        }
    }

    @Override
    public void doCallback(Stock stock, StockVo vo) {
        ParameterContainer container = ParameterContainer.getInstance();

        // 状态
        vo.setStatusName(container.getSystemName("STOCK_STATUS", stock.getStatus()));

    }
}
