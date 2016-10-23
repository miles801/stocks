package com.alidayu.taobao.api.internal.parser.xml;


import com.alidayu.taobao.api.ApiException;
import com.alidayu.taobao.api.TaobaoParser;
import com.alidayu.taobao.api.TaobaoResponse;
import com.alidayu.taobao.api.internal.mapping.Converter;

/**
 * 单个JSON对象解释器。
 *
 * @author carver.gu
 * @since 1.0, Apr 11, 2010
 */
public class ObjectXmlParser<T extends TaobaoResponse> implements TaobaoParser<T> {

    private Class<T> clazz;

    public ObjectXmlParser(Class<T> clazz) {
        this.clazz = clazz;
    }

    public T parse(String rsp) throws ApiException {
        Converter converter = new XmlConverter();
        return converter.toResponse(rsp, clazz);
    }

    public Class<T> getResponseClass() {
        return clazz;
    }

}
