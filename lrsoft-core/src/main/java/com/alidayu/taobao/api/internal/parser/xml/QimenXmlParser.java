package com.alidayu.taobao.api.internal.parser.xml;

import com.alidayu.qimen.api.QimenResponse;
import com.alidayu.taobao.api.ApiException;
import com.alidayu.taobao.api.TaobaoParser;
import com.alidayu.taobao.api.internal.mapping.Converter;

public class QimenXmlParser<T extends QimenResponse> implements TaobaoParser<T> {

    private Class<T> clazz;

    public QimenXmlParser(Class<T> clazz) {
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
