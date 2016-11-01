<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%String contextPath = request.getContextPath();%><!DOCTYPE html>
<html lang="en">
<head>
    <title>数据库列表</title>
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
<div class="main" ng-app="stock.db.dB.calculate" ng-controller="Ctrl">
    <div class="row float">
        <div class="item w300">
            <div class="form-label w100"><label>类型:</label></div>
            <select class="w200" ng-model="condition.type" ng-options="foo.value as foo.name for foo in types" ng-change="query();"> </select>
        </div>
        <div class="item w300">
            <div class="form-label w100">
                <label>最小日期:</label>
            </div>
            <div class="w200 pr">
                <input type="text" class="w200" ng-model="condition.dbDate" eccrm-my97="{}" readonly placeholder="点击选择日期"/>
                <span class="add-on">
                    <i class="icons icon clock" ng-click="condition.dbDate=null" title="点击清除日期"></i>
                </span>
            </div>
        </div>
        <div class="item w300">
            <div class="form-label w100">
                <label>最大日期:</label>
            </div>
            <div class="w200 pr">
                <input type="text" class="w200" ng-model="condition.dbDate2" eccrm-my97="{}" readonly placeholder="点击选择日期"/>
                <span class="add-on">
                                    <i class="icons icon clock" ng-click="condition.dbDate2=null" title="点击清除日期"></i>
                                </span>
            </div>
        </div>
    </div>
    <div class="ycrl split"></div>
    <div class="row pr">
        <div style="width: 20%;float: left;padding: 20px;">
            <div style="width: 100%;height: 500px;border: 1px solid #ddd;"></div>
        </div>
        <div style="width: 35%;float: left;">
            <div class="table-responsive panel panel-table">
                <table class="table table-striped table-hover">
                    <thead class="table-header">
                    <tr>
                        <td style="width: 20px;">序号</td>
                        <td>计算日期</td>
                        <td>Fn系数</td>
                    </tr>
                    </thead>
                    <tbody class="table-body">
                    <tr ng-show="!beans || !beans.total">
                        <td colspan="3" class="text-center">没有查询到数据！</td>
                    </tr>
                    <tr bindonce ng-repeat="foo in beans.data" ng-cloak>
                        <td bo-text="pager.start+$index+1"></td>
                        <td bo-text="foo.dbDate|eccrmDate"></td>
                        <td bo-text="foo.fn"></td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
        <div style="width: 35%;float: left;">
            <div class="table-responsive panel panel-table">
                <table class="table table-striped table-hover">
                    <thead class="table-header">
                    <tr>
                        <td style="width: 20px;">序号</td>
                        <td>计算日期</td>
                        <td>集团数据</td>
                    </tr>
                    </thead>
                    <tbody class="table-body">
                    <tr ng-show="!beans || !beans.total">
                        <td colspan="3" class="text-center">没有查询到数据！</td>
                    </tr>
                    <tr bindonce ng-repeat="foo in beans.data" ng-cloak>
                        <td bo-text="pager.start+$index+1"></td>
                        <td bo-text="foo.dbDate|eccrmDate"></td>
                        <td bo-text="foo.fn"></td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>
</body>
<script type="text/javascript" src="<%=contextPath%>/app/stock/db/dB/dB.js"></script>
<script type="text/javascript" src="<%=contextPath%>/app/stock/db/dB/dB_calculate.js"></script>
</html>