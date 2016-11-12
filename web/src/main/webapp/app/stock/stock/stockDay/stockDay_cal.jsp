<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String contextPath = request.getContextPath();
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>投影计算</title>
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
<div class="main" ng-app="stock.db.calculate" ng-controller="Ctrl">
    <div class="block">
        <div class="block-header">
            <div class="header-text">
                <div class="row float">
                    <div class="item w600">
                        <div class="w100">
                            <label>
                                <input type="radio" name="type" ng-model="type" value="3"
                                       ng-change="query();"/> 三元运算
                            </label>
                        </div>
                        <div class="w100">
                            <label>
                                <input type="radio" name="type" ng-model="type" value="4"
                                       ng-change="query();"/> 四元运算
                            </label>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="block-content">
            <div class="content-wrap">
                <div class="table-responsive panel panel-table">
                    <table class="table table-striped table-hover">
                        <thead class="table-header">
                        <tr>
                            <td class="width-min">序号</td>
                            <td class="cp" ng-click="orderBy='bk';reverse=!reverse">BK
                                <span>
                                        <span ng-show="orderBy=='bk'">
                                            <span ng-show="reverse">▼</span>
                                            <span ng-show="!reverse">▲</span>
                                        </span>
                                </span>
                            </td>
                            <td class="cp" ng-click="orderBy='a1';reverse=!reverse">A1
                                <span>
                                        <span ng-show="orderBy=='a1'">
                                            <span ng-show="reverse">▼</span>
                                            <span ng-show="!reverse">▲</span>
                                        </span>
                                </span>
                            </td>
                            <td class="cp" ng-click="orderBy='a2';reverse=!reverse">A2
                                <span>
                                        <span ng-show="orderBy=='a2'">
                                            <span ng-show="reverse">▼</span>
                                            <span ng-show="!reverse">▲</span>
                                        </span>
                                </span>
                            </td>
                            <td class="cp" style="width: inherit" ng-click="orderBy='a3';reverse=!reverse">A3
                                <span>
                                        <span ng-show="orderBy=='a3'">
                                            <span ng-show="reverse">▼</span>
                                            <span ng-show="!reverse">▲</span>
                                        </span>
                                </span>
                            </td>
                            <td style="width: inherit" ng-cloak ng-if="type=='4'" class="cp"
                                ng-click="orderBy='a4';reverse=!reverse">A4
                                <span>
                                        <span ng-show="orderBy=='a4'">
                                            <span ng-show="reverse">▼</span>
                                            <span ng-show="!reverse">▲</span>
                                        </span>
                                </span>
                            </td>
                        </tr>
                        </thead>
                        <tbody class="table-body">
                        <tr ng-show="!beans.length">
                            <td colspan="5" class="text-center" ng-cloak ng-if="type=='3'">没有查询到数据！</td>
                            <td colspan="6" class="text-center" ng-cloak ng-if="type=='4'">没有查询到数据！</td>
                        </tr>
                        <tr bindonce ng-repeat="foo in beans|orderBy:orderBy:reverse" ng-cloak>
                            <td bo-text="$index+1"></td>
                            <td bo-text="foo.bk|eccrmDate"></td>
                            <td bo-text="foo.a1|eccrmDate"></td>
                            <td bo-text="foo.a2|eccrmDate"></td>
                            <td bo-text="foo.a3|eccrmDate" style="width: inherit"></td>
                            <td bo-text="foo.a4|eccrmDate" ng-if="type=='4'" style="width: inherit"></td>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
<script type="text/javascript" src="<%=contextPath%>/app/stock/db/dB/dB.js"></script>
<script type="text/javascript" src="<%=contextPath%>/app/stock/stock/stockDay/stockDay_cal.js"></script>
</html>