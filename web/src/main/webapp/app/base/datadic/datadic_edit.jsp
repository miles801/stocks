<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String contextPath = request.getContextPath();
%>
<!DOCTYPE html>
<html lang="en" >
<head >
    <title >编辑数据字典</title >
    <meta content="text/html" charset="utf-8" >
    <link rel="stylesheet" type="text/css" href="<%=contextPath%>/vendor/bootstrap-v3.0/css/bootstrap.min.css" >
    <link rel="stylesheet" type="text/css" href="<%=contextPath%>/style/standard/css/eccrm-common-new.css" >
    <link rel="stylesheet" type="text/css" href="<%=contextPath%>/vendor/angular-motion-v0.3.2/angular-motion.css" >
    <script type="text/javascript" src="<%=contextPath%>/static/ycrl/javascript/jquery-all.js" ></script >
    <script type="text/javascript" src="<%=contextPath%>/static/ycrl/javascript/angular-all.js" ></script >
    <script type="text/javascript" src="<%=contextPath%>/static/ycrl/javascript/angular-strap-all.js" ></script >
    <style >
        td > input[type="text"] {
            width: 100%;
        }

        td input[type="checkbox"] {
            margin-left: 5px;
        }
    </style >
    <script >
        window.contextPathURL = "<%=contextPath%>";
    </script >
</head >
<body >
<div class="main" ng-app="com.michael.base.datadic.edit" ng-controller="MainCtrl">
    <div class="dn" >
        <input type="hidden" id="id" value="${id}" />
        <input type="hidden" id="pageType" value="${pageType}" />
    </div >
    <div class="row-10 block" >
        <div class="block-header" >
            <span class="header-button" >
                <button type="button" class="btn btn-green btn-min" data-ng-if="pageType=='add'"
                        data-ng-click="save()" data-ng-disabled="form.$invalid" data-ng-cloak >
                    <span class="glyphicons floppy_saved" ></span > 保存
                </button >
                <button type="button" class="btn btn-green btn-min" data-ng-if="pageType=='modify'"
                        data-ng-click="update()" data-ng-disabled="form.$invalid" data-ng-cloak >
                    <span class="glyphicons claw_hammer" ></span > 更新
                </button >
                <a type="button" class="btn btn-green btn-min" data-ng-click="back();" >
                    <span class="glyphicons message_forward" ></span > 返回
                </a >
            </span >
        </div >
        <div class="block-content" >
            <div class="content-wrap" >
                <form name="form" class="form-horizontal" role="form" >
                    <div class="row" >
                        <div class="form-label col-1-half" >
                            <label >字典名称:</label >
                        </div >
                        <input type="text" class="col-6-half" ng-model="beans.name" validate validate-required validate-max-length="40" maxlength="40" />

                        <div class="form-label col-1-half" >
                            <label >字典编号:</label >
                        </div >
                        <input type="text" class="col-2-half" ng-model="beans.code" validate validate-required validate-max-length="40" maxlength="40" />

                    </div >
                    <div class="row" >
                        <div class="form-label col-1-half" >
                            <label >所属类:</label >
                        </div >
                        <div class="col-10-half" >
                            <input type="text" class="col-12" ng-model="beans.className" validate validate-required />
                        </div >
                    </div >
                    <div class="row" >
                        <div class="form-label col-1-half" >
                            <label >备注:</label >
                        </div >
                        <textarea rows="6" class="col-10-half" ng-model="beans.description" maxlength="2000" ></textarea >
                    </div >
                    <div class="row" >
                        <div class="col-1-half" >&nbsp;</div >
                        <div class="block col-10-half" >
                            <div class="block-header" >
                                <span class="header-text" >属性列表</span >
                                <span class="header-button" >
                                    <a class="btn btn-green btn-min" ng-click="addAttr();" ng-if="pageType=='add' || pageType=='modify'" >添加 </a >
                                </span >
                            </div >
                            <div class="block-content" >
                                <div class="content-wrap" >
                                    <div class="table-responsive panel panel-table" >
                                        <table class="table table-striped table-hover" >
                                            <thead class="table-header" >
                                            <tr >
                                                <td title="要展示的名称" style="width: 120px;" >标签</td >
                                                <td title="对应到当前类中的Field的名称" style="width: 120px;" >字段名称</td >
                                                <td style="width: 300px;" >可选条件</td >
                                                <td title="属性的值将从什么地方获取" style="width: 120px;" >值来源</td >
                                                <td style="width: 100px;" >值类型</td >
                                                <td >值</td >
                                                <td style="width: 20px;" >操作</td >
                                            </tr >
                                            </thead >
                                            <tbody class="table-body" ng-cloak >
                                            <tr ng-show="items.length == 0" >
                                                <td colspan="7" class="text-center" >未添加属性！</td >
                                            </tr >
                                            <tr bindonce ng-repeat="foo in items" >
                                                <td ><input type="text" ng-model="foo.label" /></td >
                                                <td ><input type="text" ng-model="foo.fieldName" /></td >
                                                <td >
                                                    <span > <label ><input type="checkbox" name="conditionType" ng-model="foo.conditionType.gt" />&gt;
                                                    </label > </span >
                                                    <span > <label ><input type="checkbox" name="conditionType" ng-model="foo.conditionType.lt" />&lt;
                                                    </label ></span >
                                                    <span > <label ><input type="checkbox" name="conditionType" ng-model="foo.conditionType.ge" />&ge;
                                                    </label ></span >
                                                    <span > <label ><input type="checkbox" name="conditionType" ng-model="foo.conditionType.le" />&le;
                                                    </label ></span >
                                                    <span > <label ><input type="checkbox" name="conditionType" ng-model="foo.conditionType.eq" />=
                                                    </label ></span >
                                                    <span > <label ><input type="checkbox" name="conditionType" ng-model="foo.conditionType.ne" />!=
                                                    </label ></span >
                                                    <span > <label ><input type="checkbox" name="conditionType" ng-model="foo.conditionType.like" />like
                                                    </label ></span >
                                                    <span > <label ><input type="checkbox" name="conditionType" ng-model="foo.conditionType.in" />in
                                                    </label ></span >
                                                    <span > <label ><input type="checkbox" name="conditionType" ng-model="foo.conditionType.null" />NULL
                                                    </label ></span >
                                                    <span > <label ><input type="checkbox" name="conditionType" ng-model="foo.conditionType.notnull" />NOT NULL
                                                    </label ></span >
                                                </td >
                                                <td >
                                                    <select ng-model="foo.valueSource" ng-options="v.value as v.name for v in sources" ></select >
                                                </td >
                                                <td >
                                                    <select ng-model="foo.valueType" ng-options="v.value as v.name for v in valueTypes" ></select >
                                                </td >
                                                <td >
                                                    <input type="text" ng-if="foo.valueSource=='sys'" ng-model="foo.value" />
                                                    <input type="text" ng-if="foo.valueSource=='busi'" ng-model="foo.value" />
                                                </td >
                                                <td >
                                                    <a ng-click="removeAttr($index);" class="btn btn-tiny ph0" title="删除！" >
                                                        <i class="icons fork" ></i >
                                                    </a >
                                                </td >
                                            </tr >
                                            </tbody >
                                        </table >
                                    </div >
                                </div >
                            </div >
                        </div >
                    </div >
                </form >
            </div >
        </div >
    </div >
</div >
</body >

<script type="text/javascript" src="<%=contextPath%>/app/base/datadic/datadic.js" ></script >
<script type="text/javascript" src="<%=contextPath%>/app/base/datadic/datadic_edit.js" ></script >
</html >