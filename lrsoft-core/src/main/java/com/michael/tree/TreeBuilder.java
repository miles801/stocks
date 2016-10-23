package com.michael.tree;

import com.michael.core.SystemContainer;
import com.michael.utils.NullUtils;
import com.michael.utils.SnowflakeID;
import com.michael.utils.string.StringUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.util.Assert;

import java.util.List;

/**
 * 树形构建对象
 * 用于在实体类保存和更新之前对树形实体类的属性进行设置以及相关对象的属性更新
 *
 * @author Michael
 */
public class TreeBuilder<T extends Tree> {

    /**
     * 在保存树形对象之前对树形对象的属性进行预设
     * 注意：该方法并不会触发属性对象保存操作
     *
     * @param tree 树形对象
     */
    public void beforeSave(T tree) {
        Assert.notNull(tree, "树形构建失败!对象不能为空!");
        String id = tree.getId();
        if (StringUtils.isEmpty(id)) {
            id = SnowflakeID.getInstance().nextId() + "";
            tree.setId(id);
        }
        String parentId = tree.getParentId();
        if (StringUtils.isEmpty(parentId)) {
            tree.setPath("/" + id + "/");
            tree.setLevel(0);
        } else {
            SessionFactory sessionFactory = (SessionFactory) SystemContainer.getInstance().getBean("sessionFactory");
            Assert.notNull(sessionFactory, "树形构建失败!在获取sessionFactory对象时失败!");
            Session session = sessionFactory.getCurrentSession();
            Assert.notNull(session, "树形构建失败!当前的SessionFactory中并没有创建Session对象!");
            Tree parent = session.get(tree.getClass(), parentId);
            Assert.notNull(parent, "树形构建失败!当前对象指定的上级已经不存在，请刷新后重试!");

            Integer level = parent.getLevel();
            tree.setLevel(level + 1);
            tree.setPath(parent.getPath() + id + "/");
        }
    }

    /**
     * 在更新一个树形对象之前，对该树形对象的一些属性以及子节点的属性进行设置
     *
     * @param tree 树形对象
     */
    @SuppressWarnings("unchecked")
    public void beforeUpdate(T tree) {
        Assert.notNull(tree, "树形构建失败!对象不能为空!");
        String id = tree.getId();
        Assert.hasText(id, "树形构建失败!没有获取对象的ID!");

        // 获取session对象
        SessionFactory sessionFactory = (SessionFactory) SystemContainer.getInstance().getBean("sessionFactory");
        Assert.notNull(sessionFactory, "树形构建失败!在获取sessionFactory对象时失败!");
        Session session = sessionFactory.getCurrentSession();
        Assert.notNull(session, "树形构建失败!当前的SessionFactory中并没有创建Session对象!");

        // 查询之前的对象
        Tree originTree = session.get(tree.getClass(), id);
        Assert.notNull(originTree, "更新失败!对象已经不存在，请刷新后重试!");
        session.evict(originTree);  // 将对象驱逐，用以防止更新时两个对象冲突的问题


        // 如果两个对象的父级不一致
        String parentId = tree.getParentId();
        String originParentId = originTree.getParentId();
        if (StringUtils.notEquals(originParentId, parentId)) {
            // 设置新的path并更新相关的属性
            if (StringUtils.isEmpty(parentId)) {
                tree.setPath("/" + id + "/");
            } else {
                Tree parent = session.get(tree.getClass(), parentId);
                Assert.notNull(parent, "树形构建失败!当前对象指定的上级已经不存在，请刷新后重试!");

                Integer level = parent.getLevel();
                tree.setLevel(level + 1);
                tree.setPath(parent.getPath() + id + "/");
            }

            // 用新的path替换到所有子对象的path路径（不包含自身）
            String className = tree.getClass().getName();
            List<T> trees = session.createQuery("from " + className + " t where t.path like ?")
                    .setParameter(0, tree.getPath() + "_%")
                    .list();
            if (trees != null && !trees.isEmpty()) {
                // 获取之前的path
                String originPath = originTree.getPath();
                for (T child : trees) {
                    child.setPath(child.getPath().replaceAll(originPath, tree.getPath()));
                }
            }

        }

    }

    /**
     * 删除树形对象，当有孩子节点时，抛出异常
     * 注意：该方法会触发删除操作
     *
     * @param tree 要被删除的对象
     */
    public void delete(T tree) {
        Assert.notNull(tree, "删除失败!要被删除的对象不能为空!");
        String id = tree.getId();
        Assert.hasText(id, "删除失败!要删除的对象必须包含ID属性!");
        // 获取session对象
        SessionFactory sessionFactory = (SessionFactory) SystemContainer.getInstance().getBean("sessionFactory");
        Assert.notNull(sessionFactory, "树形构建失败!在获取sessionFactory对象时失败!");
        Session session = sessionFactory.getCurrentSession();
        Assert.notNull(session, "树形构建失败!当前的SessionFactory中并没有创建Session对象!");

        // 判断是否还有关联
        Long total = (Long) session.createQuery("select count(t.id) from " + tree.getClass().getName() + " t where t.parentId = ?")
                .setParameter(0, id)
                .uniqueResult();
        Assert.isTrue(NullUtils.defaultValue(total, 0L) == 0, "删除失败!对象无法删除，该对象下还有子对象，请先删除子对象后再行尝试!");

        // 删除
        session.delete(tree);

    }

    /**
     * 强制删除，会同时删除该对象下的所有子对象
     * 注意：该方法会触发真的删除操作
     *
     * @param tree 要删除的对象，注意，该对象一定是要在session中管理的（包含完整path的）
     */
    @SuppressWarnings("unchecked")
    public void forceDelete(T tree) {
        Assert.notNull(tree, "删除失败!要被删除的对象不能为空!");
        String path = tree.getPath();
        Assert.hasText(path, "删除失败!要删除的对象必须包含path属性!");
        // 获取session对象
        SessionFactory sessionFactory = (SessionFactory) SystemContainer.getInstance().getBean("sessionFactory");
        Assert.notNull(sessionFactory, "树形构建失败!在获取sessionFactory对象时失败!");
        Session session = sessionFactory.getCurrentSession();
        Assert.notNull(session, "树形构建失败!当前的SessionFactory中并没有创建Session对象!");

        // 删除自身与所有的孩子节点
        List<T> trees = session.createQuery("from " + tree.getClass().getName() + " t where t.path like ?")
                .setParameter(0, tree.getPath() + "%")
                .list();
        if (trees != null && !trees.isEmpty()) {
            for (T child : trees) {
                session.delete(child);
            }
        }

    }
}
