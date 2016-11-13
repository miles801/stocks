<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String contextPath = request.getContextPath();
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>日K列表</title>
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
        .red {
            color: #ff2409;
        }

        .green {
            color: #0f900a;
        }
    </style>
</head>
<body>
<div class="main condition-row-1" ng-app="stock.stock.stockDay.report" ng-controller="Ctrl">
    <div class="list-condition">
        <div class="block">
            <div class="block-header">
                    <span class="header-button">
                        <a type="button" class="btn btn-green btn-min" ng-click="reset();"> 重置 </a>
                        <a type="button" class="btn btn-green btn-min" ng-click="query();" ng-cloak
                           ng-disabled="form.$invalid"> 查询 </a>
                    </span>
            </div>
            <div class="block-content">
                <div class="content-wrap">
                    <form name="form" role="form">
                        <div class="row float">
                            <div class="item w200">
                                <div class="form-label w80">
                                    <label>股票代码:</label>
                                </div>
                                <input type="text" class="w120" ng-model="condition.code"
                                       maxlength="10" validate validate-required/>
                            </div>
                            <div class="item w200">
                                <div class="form-label w80">
                                    <label>开始时间:</label>
                                </div>
                                <div class="pr w120">
                                    <input type="text" class="w120" ng-model="condition.businessDateGe" readonly
                                           eccrm-my97="{}" validate validate-required/>
                                </div>
                            </div>
                            <div class="item w200">
                                <div class="form-label w80">
                                    <label>截止时间:</label>
                                </div>
                                <div class="pr w120">
                                    <input type="text" class="w120" ng-model="condition.businessDateLt" readonly
                                           eccrm-my97="{}" validate validate-required/>
                                </div>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
    <div class="list-result no-pagination">
        <div class="block">
            <div class="block-header">
                <div class="header-text">
                    <span>6日K分析结果</span>
                </div>
            </div>
            <div class="block-content">
                <div class="content-wrap">
                    <div class="table-responsive panel panel-table">
                        <table class="table table-striped table-hover">
                            <thead class="table-header">
                            <tr>
                                <td class="width-min">序号</td>
                                <td>6线组合</td>
                                <td>阳性比</td>
                                <td>最高值平均值</td>
                                <td>最低值平均值</td>
                                <td>总线</td>
                            </tr>
                            </thead>
                            <tbody class="table-body">
                            <tr ng-show="!beans.length">
                                <td colspan="6" class="text-center">没有查询到数据！</td>
                            </tr>
                            <tr bindonce ng-repeat="foo in beans" ng-cloak>
                                <td bo-text="$index+1"></td>
                                <td bo-text="foo.key1"></td>
                                <td>
                                    <span bo-text="(foo.yang*100/foo.counts)|number:2"></span> %
                                </td>
                                <td bo-text="(foo.nextHigh/foo.counts)|number:3"></td>
                                <td bo-text="(foo.nextLow/foo.counts)|number:3"></td>
                                <td bo-text="foo.yang"></td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
<script type="text/javascript" src="<%=contextPath%>/app/stock/stock/stockDay/stockDay.js"></script>
<script type="text/javascript" src="<%=contextPath%>/app/stock/stock/stockDay/stockDay_report6.js"></script>
</html>