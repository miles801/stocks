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
    <script type="text/javascript" src="<%=contextPath%>/vendor/moment/moment.min.js"></script>
    <script>
        window.angular.contextPathURL = '<%=contextPath%>';
    </script>
    <style>
        .pager-wrap .center {
            float: left;
        }

        tr.checked td {
            background-color: #d34c20 !important;
            color: #ffffff !important;
        }

        tr.checked td a {
            color: #ffffff;
        }
    </style>
</head>
<body>
<div class="main" ng-app="stock.db.calculate" ng-controller="Ctrl">
    <div class="block">
        <div class="block-header">
            <div class="header-text">
                <div class="row float">
                    <div class="item w900" style="width: 100%">
                        <div class="w100">
                            <label>
                                <input type="radio" name="type" ng-model="condition.type" value="3"
                                       ng-change="query();"/> 三元运算
                            </label>
                        </div>
                        <div class="w100">
                            <label>
                                <input type="radio" name="type" ng-model="condition.type" value="4"
                                       ng-change="query();"/> 四元运算
                            </label>
                        </div>
                        <div class="w100">
                            <label>
                                <input type="radio" name="type" ng-model="condition.type" value="5"
                                       ng-change="query();"/> 五元运算
                            </label>
                        </div>
                        <select class="w180" ng-model="condition.db" style="width: 150px;"
                                ng-options="foo.value as foo.name for foo in types" ng-change="query();"> </select>
                        <input type="number" name="type" ng-model="condition.value" value="4" placeholder="误差范围,最大为10"
                               style="margin-left: 15px;"/>
                        <input type="text" ng-model="startDate" placeholder="bk起始日期" style="margin-left: 10px;"/>
                        <input type="text" ng-model="endDate" placeholder="bk截止日期" style="margin-left: 10px;"/>
                        <a type="button" class="btn btn-blue" ng-click="query();" style="margin-left: 5px;"> 计算 </a>
                        <a type="button" class="btn btn-blue" ng-click="handle(condition.type);"
                           style="margin-left: 5px;" ng-cloak ng-show="condition.type==4||condition.type==5"> 处理 </a>
                    </div>
                </div>
            </div>
        </div>
        <div class="block-content">
            <div class="content-wrap">
                <div class="table-responsive panel panel-table" style="width: 500px;float: left;">
                    <table class="table table-striped table-hover" style="margin-top: 26px;">
                        <thead class="table-header">
                        <tr>
                            <td class="width-min">序号</td>
                            <td class="cp" style="width: 200px;" ng-click="order('bk')">BK
                                <span>
                                    <span ng-show="orderBy=='bk'">
                                        <span ng-show="reverse">▼</span>
                                        <span ng-show="!reverse">▲</span>
                                    </span>
                                </span>
                            </td>
                            <td class="cp" style="width: 140px" ng-click="order('bkCount')">数量
                                <span>
                                    <span ng-show="orderBy=='bkCount'">
                                        <span ng-show="reverse">▼</span>
                                        <span ng-show="!reverse">▲</span>
                                    </span>
                                </span>
                            </td>
                        </tr>
                        </thead>
                        <tbody class="table-body">
                        <tr ng-show="!beans.total">
                            <td colspan="3" class="text-center">没有查询到数据！</td>
                        </tr>
                        <tr bindonce ng-repeat="foo in beans.data" ng-cloak ng-class="{checked:bk==foo.bk}">
                            <td bo-text="$index+1"></td>
                            <td>
                                <a ng-click="view(foo.bk,foo.data);" bo-text="foo.bk|eccrmDate" class="cp"></a>
                            </td>
                            <td bo-text="foo.bkCount"></td>
                        </tr>
                        </tbody>
                    </table>
                    <div class="list-pagination" eccrm-page="pager"></div>
                </div>
                <div class="table-responsive panel panel-table"
                     style="width: 600px;float: left;margin-left: 20px;height: 400px;">
                    <table class="table table-striped table-hover">
                        <caption ng-cloak style="font-size: 14px;font-weight: 700;padding-bottom: 5px;">
                            {{bk|eccrmDate}}
                        </caption>
                        <thead class="table-header">
                        <tr>
                            <td class="width-min">序号</td>
                            <td style="width: 100px;">BK</td>
                            <td style="width: 100px;">A1</td>
                            <td style="width: 100px;">A2</td>
                            <td style="width: 100px;">A3</td>
                            <td style="width: 100px;" ng-if="condition.type>3">A4</td>
                            <td style="width: 100px;" ng-if="condition.type==5">A5</td>
                        </tr>
                        </thead>
                        <tbody class="table-body">
                        <tr ng-show="!bks.length">
                            <td colspan="5" class="text-center" ng-if="condition.type==3">无匹配项！</td>
                            <td colspan="7" class="text-center" ng-if="condition.type>3">无匹配项！</td>
                        </tr>
                        <tr bindonce ng-repeat="foo in bks" ng-cloak>
                            <td bo-text="$index+1"></td>
                            <td bo-text="foo.bk|eccrmDate"></td>
                            <td bo-text="foo.a1|eccrmDate"></td>
                            <td bo-text="foo.a2|eccrmDate"></td>
                            <td bo-text="foo.a3|eccrmDate"></td>
                            <td bo-text="foo.a4|eccrmDate" ng-if="condition.type>3"></td>
                            <td bo-text="foo.a5|eccrmDate" ng-if="condition.type==5"></td>
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
<script type="text/javascript" src="<%=contextPath%>/vendor/draggable.js"></script>
<script type="text/javascript" src="<%=contextPath%>/app/stock/stock/stockDay/stockDay_cal.js"></script>
</html>