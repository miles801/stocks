<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String contextPath = request.getContextPath();
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>资源列表</title>
    <meta content="text/html" charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=EmulateIE8"/>
    <!--[if IE]>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <![endif]-->
    <link rel="stylesheet" type="text/css" href="<%=contextPath%>/vendor/bootstrap-v3.0/css/bootstrap.min.css">
    <link rel="stylesheet" type="text/css" href="<%=contextPath%>/style/standard/css/eccrm-common-new.css">
    <link rel="stylesheet" type="text/css" href="<%=contextPath%>/vendor/angular-motion-v0.3.2/angular-motion.css">
    <link rel="stylesheet" type="text/css" href="<%=contextPath%>/vendor/zTree/css/ztree.css">
    <script type="text/javascript" src="<%=contextPath%>/static/ycrl/javascript/jquery-all.js"></script>
    <script type="text/javascript" src="<%=contextPath%>/static/ycrl/javascript/angular-all.js"></script>
    <script type="text/javascript" src="<%=contextPath%>/static/ycrl/javascript/angular-strap-all.js"></script>
    <script type="text/javascript" src="<%=contextPath%>/static/ycrl/javascript/angular-ztree-all.js"></script>
    <script type="text/javascript">
        window.angular.contextPathURL = "<%=contextPath%>";
    </script>
</head>
<body>
<div class="main" ng-app="base.resource.list" ng-controller="Ctrl">
    <div class="dn">
        <input type="hidden" id="type" value="${type}">
    </div>
    <div class="row panel panel-tree">
        <div class="tree" style="width: 200px;">
            <ul id="treeDemo" class="ztree"></ul>
        </div>
        <div class="content" style="padding-left: 205px;">
            <div class="list-result no-pagination">
                <div class="block">
                    <div class="block-header">
                        <span class="header-text">
                            <span>资源列表</span>
                        </span>
                        <span class="header-button">
                            <a class="btn btn-green btn-min" ng-click="add();">新建 </a>
                        </span>
                    </div>
                    <div class="block-content">
                        <div class="table-responsive panel panel-table">
                            <table class="table table-striped table-hover">
                                <thead class="table-header">
                                <tr>
                                    <td>序号</td>
                                    <td>名称</td>
                                    <td>编号</td>
                                    <td>显示顺序</td>
                                    <td>URL</td>
                                    <td>上级功能</td>
                                    <td>是否禁用</td>
                                    <td>操作</td>
                                </tr>
                                </thead>
                                <tbody class="table-body" ng-cloak>
                                <tr ng-show="!beans.length">
                                    <td colspan="8" class="text-center">没有符合条件的记录！</td>
                                </tr>
                                <tr bindonce ng-repeat="foo in beans">
                                    <td bo-text="$index+1"></td>
                                    <td title="点击查询明细！" style="cursor: pointer;">
                                        <a ng-click="view(foo.id);" bo-text="foo.name"></a>
                                    </td>
                                    <td bo-text="foo.code"></td>
                                    <td bo-text="foo.sequenceNo"></td>
                                    <td bo-text="foo.url"></td>
                                    <td bo-text="foo.parentName"></td>
                                    <td>
                                        <span class="btn-op" style="padding: 5px 20px;" bo-text="foo.deleted?'禁用':'启用'"
                                              ng-class="{'red':foo.deleted,'green':!foo.deleted}"></span>
                                    </td>
                                    <td>
                                        <a class="btn-op blue" ng-disabled="foo.deleted"
                                           ng-click="foo.deleted||modify(foo.id);">编辑</a>
                                        <a class="btn-op red" ng-disabled="foo.deleted"
                                           ng-click="foo.deleted||remove(foo.id,foo.$index);">关闭</a>
                                        <a class="btn-op green" ng-disabled="!foo.deleted"
                                           ng-click="!foo.deleted||enable(foo.id);">启用</a>
                                    </td>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript" src="<%=contextPath%>/app/base/resource/resource.js"></script>
<script type="text/javascript" src="<%=contextPath%>/app/base/resource/resource_list.js"></script>
</body>

</html>