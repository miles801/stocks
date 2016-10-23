package com.michael.docs;

import com.michael.core.SystemContainer;
import com.michael.core.context.ApiContext;
import com.michael.docs.annotations.Api;
import com.michael.docs.annotations.ApiField;
import com.michael.docs.annotations.ApiOperate;
import com.michael.docs.annotations.ApiParam;
import com.michael.docs.model.ApiModel;
import com.michael.docs.model.FieldModel;
import com.michael.docs.model.OperateModel;
import com.michael.docs.model.ParamModel;
import com.michael.utils.gson.GsonUtils;
import com.michael.utils.string.StringUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 解析所有定义了@Api的Bean
 * 将定义的api信息放到内存里，以便于访问
 *
 * @author Michael
 */
@WebServlet(name = "api", urlPatterns = "/api")
public class ApiServlet extends HttpServlet {

    public ApiServlet() {
        final List<ApiModel> models = new ArrayList<ApiModel>();
        final Map<String, Object> map = ((WebApplicationContext) SystemContainer.getInstance().getBeanFactory()).getBeansWithAnnotation(Api.class);
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            Class<?> clazz = entry.getValue().getClass();
            final ApiModel model = new ApiModel();
            RequestMapping requestMapping = clazz.getAnnotation(RequestMapping.class);
            if (requestMapping == null) {
                continue;
            }
            final Api api = clazz.getAnnotation(Api.class);
            model.setName(api.value());
            model.setUrl(StringUtils.join(requestMapping.value(), ","));
            model.setDescription(api.desc());

            ReflectionUtils.doWithMethods(clazz, new ReflectionUtils.MethodCallback() {
                @Override
                public void doWith(Method method) throws IllegalArgumentException, IllegalAccessException {
                    ApiOperate operate = method.getAnnotation(ApiOperate.class);
                    if (operate == null) {
                        return;
                    }
                    RequestMapping mapping = method.getAnnotation(RequestMapping.class);
                    OperateModel operateModel = new OperateModel();
                    operateModel.setName(operate.value());
                    operateModel.setDesc(operate.desc());
                    operateModel.setUrl(StringUtils.join(mapping.value(), ","));

                    RequestMethod[] methods = mapping.method();
                    operateModel.setMethod(methods[0].name());
                    Class<?> response = operate.response();
                    Class<?> request = operate.request();
                    List<FieldModel> requestModels = wrapFieldModel(request);
                    List<FieldModel> responseModels = wrapFieldModel(response);
                    operateModel.setRequestModels(requestModels);
                    operateModel.setResponseModels(responseModels);

                    // 获取参数
                    List<ParamModel> params = new ArrayList<ParamModel>();
                    Annotation[][] annotations = method.getParameterAnnotations();
                    for (Annotation[] anoArr : annotations) {
                        for (Annotation ann : anoArr) {
                            if (ann.annotationType().getName().equals(ApiParam.class.getName())) {
                                ApiParam param = (ApiParam) ann;
                                ParamModel pm = new ParamModel();
                                pm.setName(param.name());
                                pm.setValue(param.value());
                                pm.setDesc(param.desc());
                                pm.setRequired(param.required());
                                params.add(pm);
                                break;
                            }
                        }
                    }

                    operateModel.setParams(params);
                    model.addOperate(operateModel);
                }

            });
            models.add(model);
        }
        ApiContext.getInstance().setModels(models);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        GsonUtils.printData(resp, ApiContext.getInstance().getModels());
    }


    private List<FieldModel> wrapFieldModel(Class<?> request) {
        final List<FieldModel> models = new ArrayList<FieldModel>();
        List<Field> fieldList = getAllFields(request);
        for (Field field : fieldList) {
            ApiField apiField = field.getAnnotation(ApiField.class);
            if (apiField == null) {
                continue;
            }
            FieldModel fm = new FieldModel();
            fm.setName(apiField.value());
            fm.setDesc(apiField.desc());
            fm.setType(field.getType().getName());
            fm.setField(field.getName());
            fm.setRequired(apiField.required());
            if (!apiField.relate().getName().equals(Void.class.getName())) {
                List<FieldModel> sub = wrapFieldModel(apiField.relate());
                if (sub != null && !sub.isEmpty()) {
                    fm.setModels(sub);
                }
            }
            models.add(fm);
        }
        return models;
    }

    /**
     * 获得指定类型的所有被定义的字段
     * 采用的是递归的方式
     */
    private List<Field> getAllFields(Class<?> clazz) {
        List<Field> fieldList = new ArrayList<Field>();
        Collections.addAll(fieldList, clazz.getDeclaredFields());
        if (clazz.getSuperclass() != null && !clazz.getSuperclass().getName().equals(Object.class.getName())) {
            fieldList.addAll(getAllFields(clazz.getSuperclass()));
        }
        return fieldList;
    }
}
