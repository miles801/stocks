package com.data.exp;

import com.michael.base.position.domain.PositionEmp;
import com.michael.base.resource.domain.Resource;
import com.michael.utils.gson.GsonUtils;
import com.michael.base.auth.domain.AccreditData;
import com.michael.base.auth.domain.AccreditFunc;
import com.michael.base.auth.domain.AccreditMenu;
import com.michael.base.parameter.domain.BusinessParamItem;
import com.michael.base.parameter.domain.BusinessParamType;
import com.michael.base.parameter.domain.SysParamItem;
import com.michael.base.parameter.domain.SysParamType;
import com.michael.base.region.domain.Region;
import org.apache.commons.io.FileUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Order;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author Michael
 */
public class ExportData {
    private Session session;

    public static void main(String[] args) throws IOException {
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
        ExportData exportData = new ExportData();
        exportData.session = sessionFactory.openSession();


        // 导出机构、岗位、机构岗位
        exportData.export(PositionEmp.class);

        // 导出资源、菜单
        exportData.export(Resource.class, "path");

        // 导出授权
        exportData.export(AccreditMenu.class);
        exportData.export(AccreditFunc.class);
        exportData.export(AccreditData.class);

        // 导出参数
        exportData.export(BusinessParamType.class, "path");
        exportData.export(SysParamType.class, "path");
        exportData.export(BusinessParamItem.class);
        exportData.export(SysParamItem.class);

        // 导出行政区域
        exportData.export(Region.class, "parentId");
    }

    private void export(Class<?> clazz) {
        export(clazz, null);
    }

    private void export(Class<?> clazz, String orderBy) {
        String name = clazz.getName();
        System.out.println("导出：" + name + "...");
        Criteria criteria = session.createCriteria(clazz);
        if (orderBy != null && !"".equals(orderBy)) {
            criteria.addOrder(Order.asc(orderBy));
        }
        List data = criteria.list();

        //设置时间格式
        try {
            FileUtils.writeStringToFile(new File("d:/data/" + clazz.getSimpleName() + ".json"), GsonUtils.toJson(data));
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("导出：" + name + "...END");
        System.out.println("-------------------------------");
    }
}
