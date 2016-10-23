package com.michael.utils.gson;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * gson转换器的过滤器：转换的时候不转换被指定的属性名称或者类类型
 *
 * @author miles
 */
public class GsonExclusion implements ExclusionStrategy {
    private List<String> exclusionFields;//要被排除的属性名称集合
    private List<Class> exclusionClasses;//需要被排除的类型

    /**
     * 添加需要被排除的属性名称
     *
     * @param fieldName
     * @return
     */
    public GsonExclusion addExclusionField(String... fieldName) {
        if (exclusionFields == null) {
            exclusionFields = new ArrayList<String>();
        }
        if (fieldName != null && fieldName.length > 0) {
            for (int i = 0; i < fieldName.length; i++) {
                exclusionFields.add(fieldName[i]);
            }
        }
        return this;
    }

    public GsonExclusion addExclusionClass(Class<?>... classes) {
        if (exclusionClasses == null) {
            exclusionClasses = new ArrayList<Class>();
        }
        exclusionClasses.addAll(Arrays.asList(classes));
        return this;
    }

    @Override
    public boolean shouldSkipField(FieldAttributes f) {
        if (exclusionFields == null || exclusionFields.size() < 1) {
            return false;
        }
        String fieldName = f.getName();
        if (exclusionFields.contains(fieldName)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean shouldSkipClass(Class<?> clazz) {
        if (exclusionClasses == null || exclusionClasses.size() < 1) {
            return false;
        }
        if (exclusionClasses.contains(clazz)) {
            return true;
        }
        return false;
    }

    public List<String> getExclusionFields() {
        return exclusionFields;
    }

    public void setExclusionFields(List<String> exclusionFields) {
        this.exclusionFields = exclusionFields;
    }

}
