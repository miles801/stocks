package com.data.exp;

import com.michael.base.resource.domain.Resource;
import com.michael.utils.gson.GsonFactory;
import com.michael.base.auth.domain.AccreditData;
import com.michael.base.auth.domain.AccreditFunc;
import com.michael.base.auth.domain.AccreditMenu;
import com.michael.base.parameter.domain.BusinessParamItem;
import com.michael.base.parameter.domain.BusinessParamType;
import com.michael.base.parameter.domain.SysParamItem;
import com.michael.base.parameter.domain.SysParamType;
import com.michael.base.region.domain.Region;
import org.apache.commons.io.IOUtils;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.id.Assigned;
import org.hibernate.persister.entity.SingleTableEntityPersister;
import org.hibernate.tuple.IdentifierProperty;
import org.springframework.util.Assert;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;

/**
 * 导入基础数据
 * 数据来自于ExportData类导出的数据文件
 * @author Michael
 */
public class ImportData {
    private Session session;

    public static void main(String[] args) throws IOException {
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
        ImportData importData = new ImportData();
        importData.session = sessionFactory.openSession();
        importData.session.beginTransaction();

        // 导出员工、用户


        // 导出资源、菜单
        importData.execute(Resource[].class);

        // 导出授权
        importData.execute(AccreditMenu[].class);
        importData.execute(AccreditFunc[].class);
        importData.execute(AccreditData[].class);

        // 导出参数
        importData.execute(BusinessParamType[].class);
        importData.execute(BusinessParamItem[].class);
        importData.execute(SysParamType[].class);
        importData.execute(SysParamItem[].class);

        // 导出行政区域
        importData.execute(Region[].class);

        importData.session.getTransaction().commit();
    }

    private <T> void execute(Class<T[]> clazz) {
        String name = clazz.getSimpleName().replaceAll("\\[\\]", "");
        System.out.println("导入：" + name + "...");
        File file = new File("d:/data/" + name + ".json");
        Assert.isTrue(file.exists(), "文件不存在!" + file.getAbsolutePath());
        T[] data = null;
        String className = null;
        try {
            data = GsonFactory.build().fromJson(IOUtils.toString(new FileInputStream(file)), clazz);
            if (data == null || data.length < 1 || data[0]==null) {
                return;
            }
            className = data[0].getClass().getName();
            // 设置id生成策略为assigned
            SingleTableEntityPersister classMetadata = (SingleTableEntityPersister) session.getSessionFactory().getClassMetadata(className);
            IdentifierProperty identifierProperty = classMetadata.getEntityMetamodel().getIdentifierProperty();
            Field field = identifierProperty.getClass().getDeclaredField("identifierGenerator");
            field.setAccessible(true);
            field.set(identifierProperty, new Assigned());


        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        // 删除历史数据
        session.createQuery("delete from " + className).executeUpdate();

        // 保存
        int i = 0;
        for (T o : data) {
            session.save(o);
            if (i++ % 10 == 0) {
                flush();
            }
        }
        flush();

        System.out.println("导入：" + name + "...END");
        System.out.println("-------------------------------");
    }

    private void flush() {
        try {
            session.flush();
            session.clear();
        } catch (HibernateException e) {
            e.printStackTrace();
        }
    }
}
