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
    <script type="text/javascript" src="<%=contextPath%>/vendor/moment/moment.min.js"></script>
    <script>
        window.angular.contextPathURL = '<%=contextPath%>';
    </script>
    <style>
        .red{
            color: #ff2409;
        }
        .green{
            color: #0f900a;
        }
    </style>
</head>
<body>
<div class="main condition-row-1" ng-app="stock.stock.stockDay.list" ng-controller="Ctrl">
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
                                <label>股票代码:</label>
                            </div>
                            <input type="text" class="w120" ng-model="condition.code"
                                   maxlength="10"/>
                        </div>
                        <div class="item w200">
                            <div class="form-label w80">
                                <label>股票名称:</label>
                            </div>
                            <input type="text" class="w120" ng-model="condition.name"
                                   maxlength="20"/>
                        </div>
                        <div class="item w200">
                            <div class="form-label w80">
                                <label>代码:</label>
                            </div>
                            <input type="text" class="w120" ng-model="condition.key"
                                   maxlength="10"/>
                        </div>
                        <div class="item w200">
                            <div class="form-label w80">
                                <label>交易日期:</label>
                            </div>
                            <input type="text" class="w120" ng-model="condition.businessDate"
                                   placeholder="格式:20161123"/>
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
                    <span>日K列表</span>
                </div>
                <span class="header-button">
                    <c:if test="${sessionScope.get('STOCKDAY_IMPORT') eq true}">
                        <a type="button" class="btn btn-green btn-min" ng-click="importData();"> 导入 </a>
                    </c:if>
                    <c:if test="${sessionScope.get('STOCKDAY_SYNC') eq true}">
                        <a type="button" class="btn btn-green btn-min" ng-click="sync();"> 同步今日交易数据 </a>
                    </c:if>
                    <c:if test="${sessionScope.get('STOCKDAY_RESET') eq true}">
                        <a type="button" class="btn btn-green btn-min" ng-click="reset7();"> 重置第7日信息 </a>
                    </c:if>
                </span>
            </div>
            <div class="block-content">
                <div class="content-wrap">
                    <div class="table-responsive panel panel-table">
                        <table class="table table-striped table-hover">
                            <thead class="table-header">
                            <tr>
                                <td class="width-min">序号</td>
                                <td>股票代码</td>
                                <td>股票名称</td>
                                <td>交易日期</td>
                                <td>昨日收盘价</td>
                                <td>开盘价</td>
                                <td>收盘价</td>
                                <td>最高价</td>
                                <td>最低价</td>
                                <td>涨跌</td>
                                <td>6日代码</td>
                                <td>3日代码</td>
                            </tr>
                            </thead>
                            <tbody class="table-body">
                            <tr ng-show="!pager.total">
                                <td colspan="12" class="text-center">没有查询到数据！</td>
                            </tr>
                            <tr bindonce ng-repeat="foo in beans.data" ng-cloak>
                                <td bo-text="pager.start+$index+1"></td>
                                <td>
                                    <a ng-click="view(foo.id)" bo-text="foo.code" class="cp" title="点击查看详情"></a>
                                </td>
                                <td bo-text="foo.name"></td>
                                <td bo-text="foo.businessDate|eccrmDate"></td>
                                <td bo-text="foo.yesterdayClosePrice|number:3"></td>
                                <td bo-text="foo.openPrice|number:3"></td>
                                <td bo-text="foo.closePrice|number:3"></td>
                                <td bo-text="foo.highPrice|number:3"></td>
                                <td bo-text="foo.lowPrice|number:3"></td>
                                <td bo-text="foo.updown>0?'阳':'阴'" ng-class="{'red':foo.updown>0,'green':foo.updown<0}"></td>
                                <td bo-text="foo.key"></td>
                                <td bo-text="foo.key3"></td>
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
<script type="text/javascript" src="<%=contextPath%>/app/stock/stock/stockDay/stockDay_list.js"></script>
</html>