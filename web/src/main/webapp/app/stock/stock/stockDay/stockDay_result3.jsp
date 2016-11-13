<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String contextPath = request.getContextPath();
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>风险估值结果</title>
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
<div class="main condition-row-1" ng-app="stock.stock.stockDay.result" ng-controller="Ctrl">
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
                        <div class="item w200">
                            <div class="form-label w80">
                                <label>3线组合:</label>
                            </div>
                            <input type="text" class="w120" ng-model="condition.key3"
                                   maxlength="10"/>
                        </div>
                        <div class="item w200">
                            <div class="form-label w80">
                                <label>股票代码:</label>
                            </div>
                            <input type="text" class="w120" ng-model="condition.code"
                                   maxlength="10"/>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="list-result">
        <div class="block">
            <div class="block-header">
                <div class="header-text">
                    <span>分析结果</span>
                </div>
                <div class="header-button">
                    <a type="button" class="btn btn-green btn-min" ng-click="exportData();"> 导出 </a>
                </div>
            </div>
            <div class="block-content">
                <div class="content-wrap">
                    <div class="table-responsive panel panel-table">
                        <table class="table table-striped table-hover">
                            <thead class="table-header">
                            <tr>
                                <td class="width-min">序号</td>
                                <td class="cp" ng-click="order('code');">股票代码
                                    <span>
                                        <span ng-show="condition.orderBy=='code'">
                                        <span ng-show="condition.reverse">▼</span>
                                        <span ng-show="!condition.reverse">▲</span>
                                    </span>
                                    </span>
                                </td>
                                <td class="cp" ng-click="order('key3');">特征值
                                    <span>
                                        <span ng-show="condition.orderBy=='key3'">
                                        <span ng-show="condition.reverse">▼</span>
                                        <span ng-show="!condition.reverse">▲</span>
                                    </span>
                                    </span>
                                </td>
                                <td class="cp" ng-click="order('yangCount');">四阳比
                                    <span>
                                        <span ng-show="condition.orderBy=='yangCount'">
                                        <span ng-show="condition.reverse">▼</span>
                                        <span ng-show="!condition.reverse">▲</span>
                                    </span>
                                    </span>
                                </td>
                                <td class="cp" ng-click="order('avgHighPrice');">4H均值
                                    <span>
                                        <span ng-show="condition.orderBy=='avgHighPrice'">
                                        <span ng-show="condition.reverse">▼</span>
                                        <span ng-show="!condition.reverse">▲</span>
                                    </span>
                                    </span>
                                </td>
                                <td class="cp" ng-click="order('avgLowPrice');">4L均值
                                    <span>
                                        <span ng-show="condition.orderBy=='avgLowPrice'">
                                        <span ng-show="condition.reverse">▼</span>
                                        <span ng-show="!condition.reverse">▲</span>
                                    </span>
                                    </span>
                                </td>
                                <td style="width: inherit" class="cp" ng-click="order('yangCount');">总线
                                    <span>
                                        <span ng-show="condition.orderBy=='yangCount'">
                                        <span ng-show="condition.reverse">▼</span>
                                        <span ng-show="!condition.reverse">▲</span>
                                    </span>
                                    </span>
                                </td>
                            </tr>
                            </thead>
                            <tbody class="table-body">
                            <tr ng-show="!beans.total">
                                <td colspan="7" class="text-center">没有查询到数据！</td>
                            </tr>
                            <tr bindonce ng-repeat="foo in beans.data" ng-cloak>
                                <td bo-text="$index+1"></td>
                                <td bo-text="foo.code"></td>
                                <td bo-text="foo.key3"></td>
                                <td bo-text="foo.percent"></td>
                                <td bo-text="foo.avgHighPrice3|number:3"></td>
                                <td bo-text="foo.avgLowPrice3|number:3"></td>
                                <td bo-text="foo.yangCount3" style="width: inherit"></td>
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
<script type="text/javascript" src="<%=contextPath%>/app/stock/stock/stockDay/stockDay.js"></script>
<script type="text/javascript" src="<%=contextPath%>/app/stock/stock/stockDay/stockDay_result3.js"></script>
</html>