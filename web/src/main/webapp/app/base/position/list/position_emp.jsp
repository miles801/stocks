<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<%
    String contextPath = request.getContextPath();
%>
<html lang="en">
<head>
    <title>岗位员工列表</title>
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
</head>
<body id="ng-app">
<div class="main" ng-app="base.position.emp" ng-controller="Ctrl">
    <div class="panel panel-tree">
        <div class="tree" style="border: 1px solid #B7C1CB;width: 300px">
            <div class="tree-content">
                <ul id="tree" class="ztree"></ul>
            </div>
        </div>
        <div class="content " style="padding-left: 310px">
            <div class="block list-result">
                <div class="block-header">
                    <div class="header-text">
                        <span class="glyphicons list"></span> <span>岗位员工列表</span>
                    </div>
                    <div class="header-button">
                        <a type="button" class="btn btn-green btn-min" ng-if="position.id" ng-click="addEmp()" ng-cloak>
                            添加员工
                        </a>
                        <a type="button" class="btn btn-green btn-min" ng-disabled="!anyone" ng-click="removeEmp()"
                           ng-cloak>
                            移除员工
                        </a>
                    </div>
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
                                    <td>工号</td>
                                    <td>登录名</td>
                                    <td>姓名的拼音（全拼）</td>
                                    <td>性别</td>
                                    <td>入职时间</td>
                                    <td>移除</td>
                                </tr>
                                </thead>
                                <tbody class="table-body">
                                <tr ng-show="!beans || !beans.total">
                                    <td colspan="8" class="text-center">没有查询到数据！</td>
                                </tr>
                                <tr bindonce ng-repeat="foo in beans.data" ng-cloak>
                                    <td><input type="checkbox" ng-model="foo.isSelected"/></td>
                                    <td title="点击查询明细！" style="cursor: pointer;">
                                        <a ng-click="view(foo.id)" bo-text="foo.name"></a>
                                    </td>
                                    <td bo-text="foo.code"></td>
                                    <td bo-text="foo.loginName"></td>
                                    <td bo-text="foo.pinyin"></td>
                                    <td bo-text="foo.sexName"></td>
                                    <td bo-text="foo.joinDate|eccrmDate"></td>
                                    <td>
                                        <a ng-click="removeEmp(foo.id);" class="btn btn-tiny ph0" title="删除！">
                                            <i class="icons fork"></i>
                                        </a>
                                    </td>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
            <div class="list-pagination" eccrm-page="pager"></div>
        </div>
    </div>
</div>
<script type="text/javascript" src="<%=contextPath%>/app/base/position/position.js"></script>
<script type="text/javascript" src="<%=contextPath%>/app/base/emp/emp.js"></script>
<script type="text/javascript" src="<%=contextPath%>/app/base/position/list/position_emp.js"></script>
</body>
</html>
</html>