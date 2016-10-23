package com.michael.base.attachment;

import com.michael.base.attachment.service.AttachmentService;
import com.michael.common.CommonDomain;
import com.michael.core.SystemContainer;
import org.apache.log4j.Logger;
import org.springframework.aop.AfterReturningAdvice;
import org.springframework.util.Assert;

import java.lang.reflect.Method;

/**
 * 使用该AOP必须保证实体对象保存后会返回ID
 *
 * @author miles
 * @datetime 2014/5/12 15:11
 */
public class AttachmentAop implements AfterReturningAdvice {

    private Logger logger = Logger.getLogger(AttachmentAop.class);

    @Override
    public void afterReturning(Object returnValue, Method method, Object[] args, Object target) throws Throwable {
        String[] attachmentIdArray = AttachmentThreadLocal.get();
        if (attachmentIdArray == null || attachmentIdArray.length < 1) {
            return;
        }

        AttachmentService service = SystemContainer.getInstance().getBean(AttachmentService.class);
        if (service == null) {
            logger.error("没有初始化附件服务AttachmentService!");
            return;
        }

        // 从接口注解中判断是否启用附件
        HasAttachment hasAttachment = method.getAnnotation(HasAttachment.class);
        if (hasAttachment != null) {
            BusinessIdSource source = hasAttachment.bid();
            if (source.equals(BusinessIdSource.RETURN_VALUE)) {
                service.bind(returnValue.toString(), attachmentIdArray);
                AttachmentThreadLocal.clear();
            } else if (source.equals(BusinessIdSource.ASSIGN)) {
                String bid = AttachmentThreadLocal.getBusiness();
                Assert.hasText(bid, "保存附件时出错,没有指定附件绑定的业务ID!");
                service.bind(bid, attachmentIdArray);
                AttachmentThreadLocal.clear();
            }
            return;
        }

        // 从参数来判断是否启用附件（不建议使用这种方式，后续可能关闭该功能）
        if (args == null || args.length < 1) {
            return;
        }
        Object domain = args[0];
        if (domain != null && AttachmentSymbol.class.isAssignableFrom(domain.getClass())) {
            // 先获得businessId
            AttachmentSymbol symbol = (AttachmentSymbol) domain;
            String id = symbol.businessId();
            // 不存在则取返回值，如果返回值不存在，则取id
            if (id == null || "".equals(id.trim())) {
                if (returnValue == null) {
                    if (domain instanceof CommonDomain) {
                        CommonDomain o = (CommonDomain) domain;
                        returnValue = o.getId();
                    }
                }
                if (returnValue == null) {
                    logger.error("业务对象保存后，没有返回值，无法进行附件的绑定!");
                    return;
                }
                id = (String) returnValue;
            }

            service.bind(id, attachmentIdArray);
            // 保存完后清空缓存的附件
            AttachmentThreadLocal.clear();
        }

    }
}
