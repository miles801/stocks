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
</div>
</body>
<script type="text/javascript" src="<%=contextPath%>/app/home/panel/panel.js"></script>
<script type="text/javascript" src="<%=contextPath%>/vendor/echart/echarts.min.js"></script>
</html>



