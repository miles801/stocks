<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%String contextPath = request.getContextPath();%><!DOCTYPE html>
<html lang="en">
<head>
    <title>Fn数据库列表</title>
    <meta http-equiv="X-UA-Compatible" content="IE=EmulateIE8"/>
    <link rel="stylesheet" type="text/css" href="<%=contextPath%>/vendor/bootstrap-v3.0/css/bootstrap.min.css">
    <link rel="stylesheet" type="text/css" href="<%=contextPath%>/style/standard/css/eccrm-common-new.css">
    <script type="text/javascript" src="<%=contextPath%>/static/ycrl/javascript/jquery-all.js"></script>
    <script type="text/javascript" src="<%=contextPath%>/static/ycrl/javascript/angular-all.js"></script>
    <script type="text/javascript" src="<%=contextPath%>/static/ycrl/javascript/angular-strap-all.js"></script>
    <script type="text/javascript" src="<%=contextPath%>/vendor/My97DatePicker/WdatePicker.js"></script>
    <script>
        window.angular.contextPathURL = '<%=contextPath%>';
    </script>
</head>
<body>
<div class="main condition-row-1" ng-app="stock.db.fnDB.list" ng-controller="Ctrl">
    <div class="list-condition">
        <div class="block">
            <div class="block-header">
                    <span class="header-button">
                        <a type="button" class="btn btn-green btn-min" ng-click="reset();"> 重置 </a>
                        <a type="button" class="btn btn-green btn-min" ng-click="query();"> 查询 </a>
                    </span>
            </div>
            <div class="block-content">
                <div class="content-wrap">
                    <div class="row float">
                        <div class="item w300">
                            <div class="form-label w100">
                                <label>所属数据库:</label>
                            </div>
                            <input type="text" class="w200" ng-model="condition.type" maxlength="40"/>
                        </div>
                        <div class="item w300">
                            <div class="form-label w100">
                                <label>日期:</label>
                            </div>
                            <div class="w200 pr">
                                <input type="text" class="w200" ng-model="condition.fnDate" eccrm-my97="{}" readonly placeholder="点击选择日期"/>
                                <span class="add-on"><i class="icons icon clock" ng-click="condition.fnDate=null" title="点击清除日期"></i></span>
                            </div>
                        </div>
                        <div class="item w300">
                            <div class="form-label w100">
                                <label>fn系数:</label>
                            </div>
                            <input type="text" class="w200" ng-model="condition.fn" maxlength=""/>
                        </div>
                        <div class="item w300">
                            <div class="form-label w100">
                                <label>原始日期:</label>
                            </div>
                            <div class="w200 pr">
                                <input type="text" class="w200" ng-model="condition.originDate" eccrm-my97="{}" readonly placeholder="点击选择日期"/>
                                <span class="add-on"><i class="icons icon clock" ng-click="condition.originDate=null" title="点击清除日期"></i></span>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="list-result ">
        <div class="block">
            <div class="block-header">
                <div class="header-text">
                    <span>Fn数据库列表</span>
                </div>
                <span class="header-button">
                        <a type="button" class="btn btn-green btn-min" ng-click="exportData();" ng-disabled="!pager.total" ng-cloak> 导出数据 </a>
                        <a type="button" class="btn btn-green btn-min" ng-click="add();"> 新建 </a>
                    <a type="button" class="btn btn-green btn-min" ng-click="remove();" ng-disabled="!anyone" ng-cloak> 删除 </a>
                </span>
            </div>
            <div class="block-content">
                <div class="content-wrap">
                    <div class="table-responsive panel panel-table">
                        <table class="table table-striped table-hover">
                            <thead class="table-header">
                            <tr>
                                <td class="width-min">
                                    <div select-all-checkbox checkboxes="beans.data" selected-items="items" anyone-selected="anyone"></div>
                                </td>
                                <td>所属数据库</td>
                                <td>日期</td>
                                <td>fn系数</td>
                                <td>原始日期</td>
                                <td>操作</td>
                            </tr>
                            </thead>
                            <tbody class="table-body">
                            <tr ng-show="!beans || !beans.total">
                                <td colspan="6" class="text-center">没有查询到数据！</td>
                            </tr>
                            <tr bindonce ng-repeat="foo in beans.data" ng-cloak>
                                <td><input type="checkbox" ng-model="foo.isSelected"/></td>
                                <td>
                                    <a ng-click="view(foo.id)" bo-text="foo.type" class="cp" title="点击查看详情"></a>
                                </td>
                                <td bo-text="foo.fnDate"></td>
                                <td bo-text="foo.fn"></td>
                                <td bo-text="foo.originDate"></td>
                                <td class="text-left">
                                    <a class="btn-op blue" ng-click="modify(foo.id);">编辑</a>
                                    <a class="btn-op red" ng-click="remove(foo.id);">删除</a>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="list-pagination" eccrm-page="pager"></div>
</div>
</body>
<script type="text/javascript" src="<%=contextPath%>/app/stock/db/fnDB/fnDB.js"></script>
<script type="text/javascript" src="<%=contextPath%>/app/stock/db/fnDB/fnDB_list.js"></script>
</html>