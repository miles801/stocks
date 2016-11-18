<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String contextPath = request.getContextPath();
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>报表</title>
    <meta http-equiv="X-UA-Compatible" content="IE=EmulateIE8"/>
    <link rel="stylesheet" type="text/css" href="<%=contextPath%>/vendor/bootstrap-v3.0/css/bootstrap.min.css">
    <link rel="stylesheet" type="text/css" href="<%=contextPath%>/style/standard/css/eccrm-common-new.css">
    <link rel="stylesheet" type="text/css" href="<%=contextPath%>/vendor/zTree/css/ztree.css">
    <script type="text/javascript" src="<%=contextPath%>/static/ycrl/javascript/jquery-all.js"></script>
    <script type="text/javascript" src="<%=contextPath%>/static/ycrl/javascript/angular-all.js"></script>
    <script type="text/javascript" src="<%=contextPath%>/static/ycrl/javascript/angular-strap-all.js"></script>
    <script>
        window.angular.contextPathURL = '<%=contextPath%>';
    </script>
</head>
<body>
<div class="main" ng-app="settle.report" ng-controller="Ctrl">
    <div id="tab" style="height: 100%;width: 100%;"></div>
</div>
<script>
    (function (window, angular, $) {
        var app = angular.module('settle.report', [
            'eccrm.angular',
            'eccrm.angularstrap'
        ]);
        app.controller('Ctrl', function ($scope, CommonUtils, AlertFactory, ModalFactory) {
            $scope.initTab = function () {
                // 删除之前的
                $('#tab').html('');
                $(window.parent.document.body).find('ul.nav-tabs').hide();
                var data = [
                    {
                        title: '6周',
                        url: 'app/stock/stock/stockWeek/stockWeek_result6.jsp'
                    },
                    {
                        title: '3周',
                        url: 'app/stock/stock/stockWeek/stockWeek_result3.jsp'
                    }
                ];
                angular.forEach(data, function (o, i) {
                    CommonUtils.addTab({
                        isRoot: i == 0,
                        title: o.title,
                        canClose: false,
                        active: i == 0,
                        targetObj: window.self,
                        targetElm: '#tab',
                        url: o.url
                    });
                });
            };

            $scope.initTab();
        });

    })(window, angular, jQuery);
</script>
</body>
</html>