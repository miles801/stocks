<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en" >
<%
    String contextPath = request.getContextPath();
%>
<head >
    <title >参数列表</title >
    <meta http-equiv="X-UA-Compatible" content="IE=EmulateIE8" />
    <!--[if IE]>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" >
    <![endif]-->
    <link rel="stylesheet" type="text/css" href="<%=contextPath%>/vendor/artDialog/artDialog.css" />
    <link rel="stylesheet" type="text/css" href="<%=contextPath%>/vendor/bootstrap-v3.0/css/bootstrap.min.css" >
    <link rel="stylesheet" type="text/css" href="<%=contextPath%>/style/standard/css/eccrm-common-new.css" >
    <link rel="stylesheet" type="text/css" href="<%=contextPath%>/vendor/angular-motion-v0.3.2/angular-motion.css" >
    <link rel="stylesheet" type="text/css" href="<%=contextPath%>/vendor/zTree/css/ztree.css" >
    <script type="text/javascript" src="<%=contextPath%>/static/ycrl/javascript/jquery-all.js" ></script >
    <script type="text/javascript" src="<%=contextPath%>/static/ycrl/javascript/angular-all.js" ></script >
    <script type="text/javascript" src="<%=contextPath%>/static/ycrl/javascript/angular-strap-all.js" ></script >
    <script type="text/javascript" src="<%=contextPath%>/static/ycrl/javascript/angular-ztree-all.js" ></script >
    <script >
        window.angular.contextPathURL = '<%=contextPath%>';
    </script >
</head >

<body >
<div class="main" ng-app="base.parameter.type.list" ng-controller="ParameterTypeListController"
     style="height: 100% !important;">
    <div class="list-condition" >
        <div class="block" >
            <div class="block-header" >
                <span class="header-text" >
                    <span class="glyphicons search" ></span >
                        <span >参数类型管理</span >
                </span >
                <span class="header-button" >
                        <button type="button" class="btn btn-green btn-min" ng-click="query();" >
                            <span class="glyphicons search" ></span >
                            查询
                        </button >
                        <button type="button" class="btn btn-green btn-min" ng-click="queryParams={}" >
                            <span class="glyphicons repeat" ></span >
                            重置
                        </button >
                </span >
            </div >
            <div class="block-content" >
                <div class="content-wrap" >
                    <div class="row" >
                        <div class="form-label col-1-half" >
                            <label >参数名称:</label >
                        </div >
                        <input class="col-2-half" type="text" ng-model="condition.name" />
                        <span class="add-on" >
                            <i class="glyphicon glyphicon-remove icon" ></i >
                        </span >

                        <div class="form-label col-1-half" >
                            <label >参数编号:</label >
                        </div >
                        <input class="col-2-half" type="text" ng-model="condition.code" />
                        <span class="add-on" >
                            <i class="glyphicon glyphicon-remove icon" ></i >
                        </span >

                        <div class="form-label col-1-half" >
                            <label >参数状态:</label >
                        </div >
                        <select ng-model="condition.status" class="col-2" ng-options="foo.code as foo.name for foo in status" > </select >

                    </div >
                </div >
            </div >
        </div >
    </div >
    <div class="list-result" >
        <div class="block " >
            <div class="block-header" >
                        <span class="header-content" >
                            <span class="glyphicons list" ></span >
                            <span >参数信息</span >
                        </span >
                        <span class="header-button" >
                            <a class="btn btn-green btn-min" ng-click="add();" >
                                <span class="glyphicons plus" ></span > 新增
                            </a >

                            <a class="btn btn-green btn-min" ng-disabled="!anyone" ng-click='cancelOrDelete()' >
                                <span class="glyphicons remove_2" ></span > 删除/注销
                            </a >
                        </span >
            </div >
            <div class="block-content" >
                <div class="table-responsive panel panel-table first-min" >
                    <table class="table table-striped table-hover" >
                        <thead class="table-header" >
                        <tr >
                            <td >
                                <div select-all-checkbox checkboxes="parameters.data" selected-items="items" anyone-selected="anyone" ></div >
                            </td >
                            <td >名称</td >
                            <td >编号</td >
                            <td >上级类型</td >
                            <td >状态</td >
                            <td >操作</td >
                        </tr >
                        </thead >

                        <tbody class="table-body" ng-cloak >
                        <tr ng-show="!parameters" >
                            <td colspan="11" class="text-center" >没有符合条件的记录！</td >
                        </tr >

                        <tr bindonce ng-repeat="foo in parameters.data ">
                            <td >
                                <input type="checkbox" ng-model="foo.isSelected" />
                            </td >

                            <td title="点击查询明细！" class="detail" ng-click="view(foo.id)" bo-text="foo.name"></td>
                            <td bo-text="foo.code"></td>
                            <td bo-text="foo.parentName"></td>
                            <td bo-text="foo.status | parameterStatus"></td>
                            <td >
                                <a class="btn btn-tiny" title="编辑" ng-click="modify(foo.id);" >
                                    <i class="icons edit" ></i >
                                </a >
                            </td >
                        </tr >
                        </tbody >
                    </table >
                </div >
            </div >
        </div >
    </div >
    <div eccrm-page="pager" class="list-pagination" ></div >
</div >
</body >
<script type="text/javascript" src="<%=contextPath%>/app/base/parameter/sys-param-modal.js" ></script >
<script type="text/javascript" src="<%=contextPath%>/app/base/parameter/system/sysParamType_list.js" ></script >
</html >
