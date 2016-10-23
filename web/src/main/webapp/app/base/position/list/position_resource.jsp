<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
    <%
    String contextPath = request.getContextPath();
%>
<html lang="en">
<head>
    <title>岗位资源（权限）</title>
    <meta http-equiv="X-UA-Compatible" content="IE=EmulateIE8"/>
    <link rel="stylesheet" type="text/css" href="<%=contextPath%>/vendor/bootstrap-v3.0/css/bootstrap.min.css">
    <link rel="stylesheet" type="text/css" href="<%=contextPath%>/style/standard/css/eccrm-common-new.css">
    <link rel="stylesheet" type="text/css" href="<%=contextPath%>/vendor/angular-motion-v0.3.2/angular-motion.css">
    <link rel="stylesheet" type="text/css" href="<%=contextPath%>/vendor/zTree/css/ztree.css">
    <script type="text/javascript" src="<%=contextPath%>/static/ycrl/javascript/jquery-all.js"></script>
    <script type="text/javascript" src="<%=contextPath%>/static/ycrl/javascript/angular-all.js"></script>
    <script type="text/javascript" src="<%=contextPath%>/static/ycrl/javascript/angular-strap-all.js"></script>
    <script type="text/javascript" src="<%=contextPath%>/static/ycrl/javascript/angular-ztree-all.js"></script>
    <script>
        window.angular.contextPathURL = '<%=contextPath%>';
    </script>
    <style>

        .tree-button {
            position: absolute;
            z-index: 9999;
            padding: 6px 20px;
            height: 40px
        }

        .tree-button > .btn {
            cursor: pointer;
            margin-left: 8px;
        }

        .tree-container {
            height: 100%;
            padding-top: 40px;
            position: relative;
        }

        .tree-container > .tree {
            position: relative !important;
            border: 0 !important;
            margin: 0 !important;
            width: 100% !important;
            height: 100% !important;
        }
    </style>
</head>
<body id="ng-app">
<div class="main" ng-app="base.position.resource" ng-controller="Ctrl">
    <div class="panel panel-tree">
        <div class="tree" style="border: 1px solid #B7C1CB;width: 300px;z-index: 9999;">
            <div class="tree-content">
                <ul id="tree" class="ztree"></ul>
            </div>
        </div>
        <div class="content " style="padding-left: 310px;overflow: auto;">
            <div class="float">
                <div style="width:350px;height:600px;margin-left:20px;">
                    <div class="block">
                        <div class="block-header">
                            <div class="header-text">
                                <span class="glyphicons list"></span> <span>菜单权限</span>
                            </div>
                            <div class="header-button">
                                <a ng-click="saveMenu()" class="btn btn-blue-dark" ng-disabled="!position.id">保存</a>
                            </div>
                        </div>
                        <div class="block-content pr">
                            <div class="tree-button">
                                <a ng-click="checkAllMenu();" class="btn btn-blue-dark">全选</a>
                                <a ng-click="clearAllMenu()" class="btn btn-blue-dark">清空</a>
                            </div>
                            <div class="tree-container">
                                <div class="tree">
                                    <div class="tree-content">
                                        <ul id="menuTree" class="ztree"></ul>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="dn" style="width:350px;height:600px;margin-left:20px;">
                    <div class="block">
                        <div class="block-header">
                            <div class="header-text">
                                <span class="glyphicons list"></span> <span>数据权限</span>
                            </div>
                            <div class="header-button">
                                <a ng-click="saveMenu()" class="btn btn-blue-dark">保存</a>
                            </div>
                        </div>
                        <div class="block-content pr">
                            <div class="tree-button">
                                <a ng-click="checkAll(menus);" class="btn btn-blue-dark">全选</a>
                                <a ng-click="clearAll(menus)" class="btn btn-blue-dark">清空</a>
                            </div>
                            <div class="tree-container">
                                <div class="tree">
                                    <div class="tree-content">
                                        <ul id="dataTree" class="ztree"></ul>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div style="width:350px;height:600px;margin-left:20px;">
                    <div class="block">
                        <div class="block-header">
                            <div class="header-text">
                                <span class="glyphicons list"></span> <span>操作权限</span>
                            </div>
                            <div class="header-button">
                                <a ng-click="saveElement()" class="btn btn-blue-dark">保存</a>
                            </div>
                        </div>
                        <div class="block-content pr">
                            <div class="tree-button">
                                <a ng-click="checkAllElement();" class="btn btn-blue-dark">全选</a>
                                <a ng-click="clearAllElement()" class="btn btn-blue-dark">清空</a>
                            </div>
                            <div class="tree-container">
                                <div class="tree">
                                    <div class="tree-content">
                                        <ul id="elementTree" class="ztree"></ul>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript" src="<%=contextPath%>/app/base/position/position.js"></script>
<script type="text/javascript" src="<%=contextPath%>/app/base/resource/resource.js"></script>
<script type="text/javascript" src="<%=contextPath%>/app/base/position/list/position_resource.js"></script>
</body>
</html>