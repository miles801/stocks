<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String contextPath = request.getContextPath();
%>
<html>
<head>
    <base href="<%=request.getContextPath()%>/">
    <title>日K导入</title>
    <link rel="stylesheet" type="text/css" href="<%=contextPath%>/vendor/bootstrap-v3.0/css/bootstrap.min.css">
    <link rel="stylesheet" type="text/css" href="<%=contextPath%>/style/standard/css/eccrm-common-new.css">
    <script type="text/javascript" src="<%=contextPath%>/static/ycrl/javascript/jquery-all.js"></script>
    <script type="text/javascript" src="<%=contextPath%>/static/ycrl/javascript/angular-all.js"></script>
    <script type="text/javascript" src="<%=contextPath%>/static/ycrl/javascript/angular-strap-all.js"></script>
    <script type="text/javascript" src="<%=contextPath%>/static/ycrl/javascript/angular-upload.js"></script>

    <script>
        window.angular.contextPathURL = '<%=contextPath%>';
    </script>
</head>
<body>
<div class="main" ng-app="stock.stock.stockDay.import" ng-controller="Ctrl">
    <div class="block">
        <div class="block-header">
                <span class="header-text"> </span>
        </div>
        <div class="block-content">
            <div class="content-wrap">
                <form name="form" class="form-horizontal" role="form">
                    <div class="row dn">
                        <div class="col-1-half form-label"><label >公司:</label></div>
                        <input ng-model="owner" class="col-2-half" />
                    </div>
                    <div class="row" eccrm-upload="fileUpload"></div>
                    <div class="row" style="padding: 10px 2.16% 15px 10.33333332%">
                        <div class="box-info">
                            <p style="font-size: 14px;font-weight: 700;">注意：</p>

                            <p>1. 如果有一条数据不正确，都将会全部失败！</p>
                            <p>2. 为了保证程序的效率，请每次最多上传100个文件！</p>
                        </div>
                    </div>
                    <div class="button-row">
                        <button class="btn" ng-click="importData();" ng-disabled="!canImport"
                                style="margin-left:80px;width: 150px;">执行导入
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
</body>
<script type="text/javascript" src="<%=contextPath%>/app//stock/stock/stockDay/stockDay.js"></script>
<script type="text/javascript" src="<%=contextPath%>/app//stock/stock/stockDay/stockDay_import.js"></script>
</html>
