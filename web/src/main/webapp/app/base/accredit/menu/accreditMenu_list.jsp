<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String contextPath = request.getContextPath();
%>
<!DOCTYPE html>
<html lang="en" >
<head >
    <title >授权菜单列表</title >
    <meta content="text/html" charset="utf-8" >
    <link rel="stylesheet" type="text/css" href="<%=contextPath%>/vendor/bootstrap-v3.0/css/bootstrap.min.css" >
    <link rel="stylesheet" type="text/css" href="<%=contextPath%>/style/standard/css/eccrm-common-new.css" >
    <link rel="stylesheet" type="text/css" href="<%=contextPath%>/vendor/angular-motion-v0.3.2/angular-motion.css" >
    <link rel="stylesheet" type="text/css" href="<%=contextPath%>/vendor/zTree/css/ztree.css" >
    <script type="text/javascript" src="<%=contextPath%>/static/ycrl/javascript/jquery-all.js" ></script >
    <script type="text/javascript" src="<%=contextPath%>/static/ycrl/javascript/angular-all.js" ></script >
    <script type="text/javascript" src="<%=contextPath%>/static/ycrl/javascript/angular-strap-all.js" ></script >
    <script type="text/javascript" src="<%=contextPath%>/static/ycrl/javascript/angular-ztree-all.js" ></script >
    <script type="text/javascript" >
        window.angular.contextPathURL = "<%=contextPath%>";
    </script >
</head >
<body >
<div class="main" ng-app="eccrm.accredit.menu.list" ng-controller="AccreditMenuCtrl" >
    <div class="row panel panel-tree" >
        <div class="tree" style="width: 180px;" >
            <div class="tree-content" >
                <ul id="treeDemo" class="ztree" ></ul >
            </div >
        </div >
        <div class="content" style="padding-left: 185px;" >
            <div class="block" >
                <div class="block-header" >
                        <span class="header-text" >
                            <span >授权菜单</span >
                        </span >
                        <span class="header-button" >
                            <a class="btn btn-green btn-min" ng-disabled="!currentId" ng-click="grantMenu();" >菜单授权 </a >
                            <a class="btn btn-green btn-min" ng-disabled="!currentId" ng-click="grantData();" >数据授权 </a >
                        </span >
                </div >
                <div class="block-content" >
                    <div class="table-responsive panel panel-table" >
                        <table class="table table-striped table-hover" >
                            <thead class="table-header" >
                            <tr sortable="sortable" sortable-reverse="reverse" >
                                <td >名称</td >
                                <td >编号</td >
                                <td >URL</td >
                                <td >上级功能</td >
                                <td >操作</td >
                            </tr >
                            </thead >
                            <tbody class="table-body" ng-cloak >
                            <tr ng-show="menus.total == 0" >
                                <td colspan="10" class="text-center" >没有符合条件的记录！</td >
                            </tr >
                            <tr ng-repeat="menu in menus" >
                                <td title="点击查询明细！" style="cursor: pointer;" >
                                    <a ng-click="view(menu.id);" ng-bind-template="{{ menu.name }}" ></a >
                                </td >
                                <td ng-bind-template="{{ menu.code }}" ></td >
                                <td ng-bind-template="{{ menu.url }}" ></td >
                                <td ng-bind-template="{{ menu.parentName }}" ></td >
                                <td >
                                    <a ng-click="modify(menu.id)" class="btn btn-tiny" title="编辑" >
                                        <i class="icons edit" ></i >
                                    </a >
                                    <a ng-click="remove(menu.id,$index);" class="btn btn-tiny" title="删除！" >
                                        <i class="icons fork" ></i >
                                    </a >
                                </td >
                            </tr >
                            </tbody >
                        </table >
                    </div >
                </div >
            </div >
        </div >
    </div >
</div >
</div >

<script type="text/javascript" src="<%=contextPath%>/app/position/position.js" ></script >
<script type="text/javascript" src="<%=contextPath%>/app/base/resource/menu/menu.js" ></script >
<script type="text/javascript" src="<%=contextPath%>/app/base/resource/menu/menu-modal.js" ></script >
<script type="text/javascript" src="<%=contextPath%>/app/base/accredit/menu/accreditMenu.js" ></script >
<script type="text/javascript" src="<%=contextPath%>/app/base/accredit/menu/accreditMenu_list.js" ></script >
</body >

</html >