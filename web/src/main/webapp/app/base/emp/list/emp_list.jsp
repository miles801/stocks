<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String contextPath = request.getContextPath();
%>
<html lang="en">
<head>
    <title>员工</title>
    <meta http-equiv="X-UA-Compatible" content="IE=EmulateIE8"/>
    <link rel="stylesheet" type="text/css" href="<%=contextPath%>/vendor/bootstrap-v3.0/css/bootstrap.min.css">
    <link rel="stylesheet" type="text/css" href="<%=contextPath%>/style/standard/css/eccrm-common-new.css">
    <script type="text/javascript" src="<%=contextPath%>/static/ycrl/javascript/jquery-all.js"></script>
    <script type="text/javascript" src="<%=contextPath%>/static/ycrl/javascript/angular-all.js"></script>
    <script type="text/javascript" src="<%=contextPath%>/static/ycrl/javascript/angular-strap-all.js"></script>
    <script>
        window.angular.contextPathURL = '<%=contextPath%>';
    </script>
</head>
<body>
<div class="main condition-row-1" ng-app="base.emp.list" ng-controller="Ctrl">
    <div class="list-condition">
        <div class="block">
            <div class="block-header">
                <span class="header-text">
                    <span class="glyphicons search"></span>
                </span>
                <span class="header-button">
                    <a type="button" class="btn btn-green btn-min" ng-click="query();">
                            <span class="glyphicons plus"></span> 查询
                        </a>
                </span>
            </div>
            <div class="block-content">
                <div class="content-wrap">
                    <div class="row">
                        <div class="form-label col-1-half">
                            <label>姓名:</label>
                        </div>
                        <input class="col-2-half" type="text" ng-model="condition.name"/>
                        <div class="form-label col-1-half">
                            <label>工号:</label>
                        </div>
                        <input class="col-2-half" type="text" ng-model="condition.code"/>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="list-result ">
        <div class="block">
            <div class="block-header">
                <div class="header-text">
                    <span class="glyphicons list"></span>
                    <span>员工</span>
                </div>
                <span class="header-button">
                    <a type="button" class="btn btn-green btn-min" ng-click="add();">
                        <span class="glyphicons plus"></span> 新建
                    </a>
                    <a type="button" class="btn btn-green btn-min" ng-click="remove();">
                        <span class="glyphicons plus"></span> 禁用
                    </a>
                </span>
            </div>
            <div class="block-content">
                <div class="content-wrap">
                    <div class="table-responsive panel panel-table">
                        <table class="table table-striped table-hover">
                            <thead class="table-header">
                            <tr>
                                <td class="width-min">
                                    <div select-all-checkbox checkboxes="beans.data" selected-items="items"
                                         anyone-selected="anyone"></div>
                                </td>
                                <td>姓名</td>
                                <td>登录名</td>
                                <td>电话</td>
                                <td>性别</td>
                                <td>岗位</td>
                                <td>账号状态</td>
                                <td>组织机构</td>
                                <td>操作</td>
                            </tr>
                            </thead>
                            <tbody class="table-body">
                            <tr ng-show="!beans || !beans.total">
                                <td colspan="9" class="text-center">没有查询到数据！</td>
                            </tr>
                            <tr bindonce ng-repeat="foo in beans.data" ng-cloak>
                                <td><input type="checkbox" ng-model="foo.isSelected"/></td>
                                <td title="点击查询明细！" style="cursor: pointer;">
                                    <a ng-click="view(foo.id)" bo-text="foo.name"></a>
                                </td>
                                <td bo-text="foo.loginName"></td>
                                <td bo-text="foo.mobile"></td>
                                <td bo-text="foo.sexName"></td>
                                <td bo-text="foo.roleNames"></td>
                                <td bo-text="foo.lockedName"></td>
                                <td bo-text="foo.orgName"></td>
                                <td>
                                    <a ng-disabled="foo.locked!==0" ng-click="foo.locked!==0||modify(foo.id);" class="btn-op blue">编辑</a>
                                    <a ng-disabled="foo.locked!==0" ng-click="foo.locked!==0||remove(foo.id);"
                                       class="btn-op red">禁用</a>
                                    <a ng-disabled="foo.locked!==1" ng-click="foo.locked!==1 || start(foo.id);"
                                       class="btn-op green">启用</a>
                                </td>
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
<script type="text/javascript" src="<%=contextPath%>/app/base/emp/emp.js"></script>
<script type="text/javascript" src="<%=contextPath%>/app/base/emp/list/emp_list.js"></script>
</html>