<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String contextPath = request.getContextPath();
%>
<!DOCTYPE html>
<html lang="en" >
<head >
    <title >快捷方式</title >
    <meta content="text/html" charset="utf-8" >
    <meta http-equiv="X-UA-Compatible" content="IE=EmulateIE8" />
    <link rel="stylesheet" type="text/css" href="<%=contextPath%>/vendor/bootstrap-v3.0/css/bootstrap.min.css" >
    <link rel="stylesheet" type="text/css" href="<%=contextPath%>/style/standard/css/eccrm-common-new.css" >
    <script type="text/javascript" src="<%=contextPath%>/static/ycrl/javascript/jquery-all.js" ></script >
    <script type="text/javascript" src="<%=contextPath%>/static/ycrl/javascript/angular-all.js" ></script >
    <style >
        .item {
            float: left;
            margin-left: 15px;
            height: 80px;
            width: 50px;
        }

        .item img {
            width: 50px;
            height: 50px;
            cursor: pointer;
        }

        .item .text {
            width: 100%;
            height: 30px;
            text-align: center;
            padding-top: 5px;
            display: inline-block;
        }

    </style >
    <script type="text/javascript" >
        window.angular.contextPathURL = "<%=contextPath%>";
        (function () {
            var app = angular.module('eccrm.tools.qs', [
                'eccrm.angular'
            ]);
            app.controller('QsCtrl', function ($scope, CommonUtils) {

            });
        })();
    </script >
</head >
<body >
<div class="main" ng-app="eccrm.tools.qs" ng-controller="QsCtrl" style="overflow: hidden;" >
    <div style="padding: 10px 15px;" >
        <%--<div class="item " >
            <img src="<%=contextPath%>/app/tools/qs/images/1.png" alt="电梯视频" />
            <span class="text" >X</span >
        </div >
        <div class="item" >
            <img src="<%=contextPath%>/app/tools/qs/images/2.png" alt="官方微信" />
            <span class="text" >X</span >
        </div >--%>
        <div class="item" >
            <img src="<%=contextPath%>/app/tools/qs/images/3.png" alt="软电话必装控件" />
            <span class="text" title="点击下载..." ><a href="<%=contextPath%>/attachment/download?id=4028b8814c66091c014c66091cb10001"  target="_blank">软电话必装控件</a ></span >
        </div >
        <div class="item" >
            <img src="<%=contextPath%>/app/tools/qs/images/4.png" alt="控件安装说明" />
            <span class="text" title="点击下载..." ><a href="<%=contextPath%>/attachment/download?id=4028b8814c66091c014c66091cb10002"  target="_blank">控件安装说明</a ></span >
        </div >
            <%--<div class="item" >
                <img src="<%=contextPath%>/app/tools/qs/images/5.png" alt="手机报" />
                <span class="text" >X</span >
            </div >--%>
    </div >
</div >
</body >
</html >