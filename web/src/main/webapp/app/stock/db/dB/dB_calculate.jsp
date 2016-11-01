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
    <script type="text/javascript" src="<%=contextPath%>/vendor/moment/moment.min.js" class="js"></script>
    <script>
        window.angular.contextPathURL = '<%=contextPath%>';
    </script>
    <style>
        .titem {
            cursor: pointer;
            padding: 5px 10px;
        }

        .titem.checked {
            background: #7687ff;
            color: #FFFFFF;
        }

        .titem:hover {
            text-decoration: underline;
            color: #ff1b08;
        }
    </style>
</head>
<body>
<div class="main" ng-app="stock.db.dB.calculate" ng-controller="Ctrl">
    <form name="form" role="form">
        <div class="row float">
            <div class="item w200">
                <div class="form-label w80"><label validate-error="form.type">类型:</label></div>
                <select class="w120" ng-model="condition.type" ng-options="foo.value as foo.name for foo in types" validate validate-required name="type"> </select>
            </div>
            <div class="item w200">
                <div class="form-label w80">
                    <label>最小日期:</label>
                </div>
                <div class="w120 pr">
                    <input type="text" class="w120" ng-model="condition.fnDateGe" eccrm-my97="{}" readonly placeholder="点击选择日期" validate validate-required/>
                </div>
            </div>
            <div class="item w200">
                <div class="form-label w80">
                    <label>最大日期:</label>
                </div>
                <div class="w120 pr">
                    <input type="text" class="w120" ng-model="condition.fnDateLt" eccrm-my97="{}" readonly placeholder="点击选择日期" validate validate-required/>
                </div>
            </div>
            <div class="item w200">
                <div class="form-label w80">
                    <label>扫描范围:</label>
                </div>
                <div class="w120 pr">
                    <input type="text" class="w120" ng-model="condition.days" placeholder="天数" validate validate-int ng-change="rangeChange();"/>
                </div>
            </div>
            <button class="btn btn-blue" ng-click="query();" style="margin-left: 15px;" ng-disabled="form.$invalid" ng-cloak>加载</button>
        </div>
    </form>
    <div class="ycrl split"></div>
    <div class="row pr">
        <div style="width: 20%;float: left;padding: 20px;">
            <div style="width: 100%;height: 500px;border: 1px solid #ddd;">
                <div ng-repeat="foo in dates" ng-cloak class="titem" ng-class="{checked:checked==foo.id}">
                    <span ng-click="load(foo)" style="display: block;">{{foo.dbDate | eccrmDate}}</span>
                </div>
            </div>
        </div>
        <div style="width: 35%;float: left;padding: 20px;" ng-style="{height:height+'px'}">
            <div class="table-responsive panel panel-table" style="padding: 0;">
                <table class="table table-striped table-hover">
                    <caption style="font-weight: 700;padding: 0 20px 10px;font-size: 14px;" ng-cloak>集团数：{{groupCount}}</caption>
                    <thead class="table-header">
                    <tr>
                        <td style="width: 20px;">序号</td>
                        <td>计算日期</td>
                        <td>Fn系数</td>
                    </tr>
                    </thead>
                    <tbody class="table-body">
                    <tr ng-show="!beans1.length">
                        <td colspan="3" class="text-center">没有查询到数据！</td>
                    </tr>
                    <tr bindonce ng-repeat="foo in beans1" ng-cloak>
                        <td bo-text="pager.start+$index+1"></td>
                        <td bo-text="foo.fnDate|eccrmDate"></td>
                        <td bo-text="foo.fn"></td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
        <div style="width: 35%;float: left;" ng-style="{height:height+'px'}">
            <!-- 为 ECharts 准备一个具备大小（宽高）的 DOM -->
            <div id="char1" style="width: 80%;height:400px;margin:0 auto;"></div>
        </div>
    </div>
</div>
</body>
<script type="text/javascript" src="<%=contextPath%>/app/stock/db/dB/dB.js"></script>
<script type="text/javascript" src="<%=contextPath%>/app/stock/db/fnDB/fnDB.js"></script>
<script type="text/javascript" src="<%=contextPath%>/vendor/echart/echarts.min.js"></script>
<script type="text/javascript" src="<%=contextPath%>/app/stock/db/dB/dB_calculate.js"></script>
</html>