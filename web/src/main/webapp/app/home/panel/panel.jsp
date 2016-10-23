<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <%
        String contextPath = request.getContextPath();
    %>
    <title>仪表盘</title>
    <link rel="stylesheet" type="text/css" href="<%=contextPath%>/vendor/bootstrap-v3.0/css/bootstrap.min.css">
    <link rel="stylesheet" type="text/css" href="<%=contextPath%>/style/standard/css/eccrm-common-new.css">
    <link rel="stylesheet" type="text/css" href="<%=contextPath%>/vendor/angular-motion-v0.3.2/angular-motion.css">
    <script type="text/javascript" src="<%=contextPath%>/static/ycrl/javascript/jquery-all.js"></script>
    <script type="text/javascript" src="<%=contextPath%>/static/ycrl/javascript/angular-all.js"></script>
    <script type="text/javascript" src="<%=contextPath%>/static/ycrl/javascript/angular-strap-all.js"></script>
    <script type="text/javascript">
        window.angular.contextPathURL = '<%=contextPath%>';
    </script>
    <style>
        .panel.panel-table tbody.table-body tr {
            height: 28px;
            line-height: 28px;
        }

        .main .box {
            float: left;
            width: 50%;
            padding: 5px;
            height: 380px;
            margin-top: 15px;
        }
    </style>
</head>

<body id="ng-app">
<div class="main" ng-app="home.panel" ng-controller="Ctrl" style="overflow: auto;">
    <div class="box">
        <div id="appBar" style="height: 100%;width: 100%;"></div>
    </div>
    <div class="box">
        <div class="table-responsive panel panel-table" style="height: auto;padding: 0;">
            <table class="table table-striped table-hover">
                <caption style="font-size: 18px; padding: 5px; font-weight: 700;">技术漏洞 Top10</caption>
                <thead class="table-header">
                <tr>
                    <td style="width: 20px;">序号</td>
                    <td style="width: 150px;">漏洞名称</td>
                    <td style="width: 100px;">漏洞等级</td>
                    <td style="width: 120px;">漏洞编号</td>
                    <td style="width: 80px;">涉及资产数</td>
                </tr>
                </thead>
                <tbody class="table-body">
                <tr ng-show="!bugs.length">
                    <td colspan="8" class="text-center">没有查询到数据！</td>
                </tr>
                <tr bindonce ng-repeat="foo in bugs" ng-cloak>
                    <td bo-text="$index+1"></td>
                    <td bo-text="foo.name|substr:30" bo-title="foo.name" class="text-left"></td>
                    <td bo-text="foo.riskLevel"></td>
                    <td bo-text="foo.code"></td>
                    <td bo-text="foo.count"></td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
    <div class="box">
        <div id="riskBar" style="height: 100%;width: 100%;"></div>
    </div>
    <div class="box">
        <div id="bugBar" style="height: 100%;width: 100%;"></div>
    </div>
    </div>
</div>
</body>
<script type="text/javascript" src="<%=contextPath%>/app/home/panel/panel.js"></script>
<script type="text/javascript" src="<%=contextPath%>/vendor/echart/echarts.min.js"></script>
<script type="text/javascript" src="<%=contextPath%>/app/itsm/bug/bug/bug.js"></script>
</html>



