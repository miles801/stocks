<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String contextPath = request.getContextPath();
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>股票管理平台</title>
    <link rel="stylesheet" href="<%=contextPath%>/vendor/bootstrap-v3.0/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="<%=contextPath%>/style/standard/css/eccrm-common-new.css"/>
    <link rel="stylesheet" href="<%=contextPath%>/app/main/css/main.css"/>

    <script type="text/javascript" src="<%=contextPath%>/static/ycrl/javascript/jquery-all.js"></script>
    <script type="text/javascript" src="<%=contextPath%>/static/ycrl/javascript/angular-all.js"></script>
    <script type="text/javascript" src="<%=contextPath%>/static/ycrl/javascript/angular-strap-all.js"></script>
    <script type="text/javascript" src="<%=contextPath%>/app/base/emp/emp.js"></script>
    <script type="text/javascript" src="<%=contextPath%>/app/main/js/notify.js"></script>
    <script>
        window.angular.contextPathURL = "<%=contextPath%>";
    </script>
    <style>
        .aside {
            top: inherit !important;
        }
    </style>
</head>
<body id="ng-app" ng-app="eccrm.main">
<div id="container" ng-controller="MainController">
    <input type="hidden" id="contextPath" value="<%=contextPath%>/"/>
    <div id="header" style="display: none;">
        <div class="top">
            <span style="display: inline-block; height: 28px; width: 200px; color: #fff; font-size: 14px; line-height: 28px; margin-left: 8px;font-weight: 700;">
                <span style="color:#ef1a1a;">股票</span><span style="color: #00ff8b;">管理</span><span >平台</span>
            </span>
            <span style="float: right; margin-right: 125px; color: #ffffff; font-size: 12px;height: 30px;line-height: 30px;position: relative;">
                <i class="icons icon user" style="position: absolute;top:4px;"></i> <span
                    style="margin-left: 24px;">${sessionScope.employeeName}</span>
            </span>
            <div class="tool" style="width: 100px;">
                <a href="<%=contextPath%>/logout">
                    <img src="<%=contextPath%>/app/main/images/icon/h13.png" width="24" height="24" title="退出">
                </a>
                <a ng-click="updatePwd();">
                    <img src="<%=contextPath%>/app/main/images/icon/h7.png" width="24" height="24" title="更改密码">
                </a>
            </div>
        </div>
    </div>
    <div id="main" style="width: 0;">
        <div class="leftbar">
            <div class="LB_container">
                <a title="首页" ng-click="showHome();" class="current">
                    <img src="<%=contextPath%>/app/main/images/home.png" alt="首页"/>
                </a>
                <a bindonce bo-title="menu.name" ng-repeat="menu in menus" ng-repeat-finish
                   ng-click="showChildren(menu);">
                    <img ng-src="<%=contextPath%>/attachment/download?id={{menu.icon}}" ng-cloak/>
                </a>
            </div>
            <div class="btnT dn"></div>
            <div class="btnB dn"></div>
        </div>
        <div class="mainRight">
            <div id="accordian" class="dn">
                <ul>
                    <li bindonce ng-class="{'current':$parent.currentId==level1.id}" ng-repeat="level1 in subMenus"
                        ng-repeat-finish="subFinish">
                        <h3 nav-click-slide=".nav_menus">
							<span class="menu-text">
                                <i class="icons-sj"></i>
								<a ng-click="addTab(level1.name,level1.url,level1,level1.id)" bo-text="level1.name"></a>
							</span>
                            <span class="menu-children" bo-show="level1.children.length>0">
                                    <span class="icon-down">&#9660;</span>
							</span>
                        </h3>
                        <ul class="nav_menus">
                            <li bindonce ng-class="{'current':$parent.$parent.currentId==level2.id}"
                                ng-repeat="level2 in level1.children" bindonce>
                                <div bo-if="level2.children && level2.children.length>0">
                                    <a nav-click-slide="div" style="cursor: pointer;"
                                       ng-click="addTab(level2.name,level2.url,level2,level2.id)">
                                        <span bo-text="level2.name" class="menu-text"></span>
                                        <span class="menu-children">
											<span style="color:#1893dd;">&#9660;</span>
										</span>
                                    </a>

                                    <div style="margin-left: 10px;display: none;"
                                         bo-if="level2.children && level2.children.length>0">
                                        <a ng-click="addTab(level3.name,level3.url,level3,level2.id)"
                                           style="cursor: pointer;"
                                           bindonce ng-repeat="level3 in level2.children">
                                            <span style="margin-right:3px;color:#1893dd;">&#8627;</span>
                                            <span bo-text="level3.name" class="menu-text"></span>
                                        </a>
                                    </div>
                                </div>
                                <a bo-if="!level2.children || level2.children.length<1"
                                   ng-click="addTab(level2.name,level2.url,level2,level2.id)">
                                    <span bo-text="level2.name" class="menu-text"></span>
                                </a>
                            </li>
                        </ul>
                    </li>
                </ul>
            </div>
            <div id="colbar" class="dn">
                <div id="arrow" ng-click="toggle()">
                    <i class="arrow-left" id="fold" title="收起"></i>
                    <i class="arrow-right" id="expand" title="展开" style="display: none;"></i>
                </div>
            </div>
            <div class="content-iframe">
                <iframe id="iframe" style="display: none;" name="iframe" frameborder="0"></iframe>
                <div id="tab" style="height: 100%;width: 100%;overflow: hidden;display: none;"></div>
            </div>
        </div>
    </div>
</div>
</body>
<script src="<%=contextPath%>/app/main/js/main.js" type="text/javascript"></script>
</html>