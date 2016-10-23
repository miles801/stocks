package com.michael.base.parameter.web;

import com.michael.core.web.BaseController;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created on 2014/7/24 12:34
 *
 * @author miles
 */
@Controller
@Scope("prototype")
@RequestMapping("/base/parameter/type")
public class ParameterTypeCtrl extends BaseController {
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String toTypeTab() {
        return "base/parameter/parameterType";
    }
}
