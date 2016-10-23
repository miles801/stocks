<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String contextPath = request.getContextPath();
%>
<!DOCTYPE html>
<html lang="en">
<title>行政区域列表</title>
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE8"/>
<link rel="stylesheet" type="text/css" href="<%=contextPath%>/vendor/bootstrap-v3.0/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="<%=contextPath%>/style/standard/css/eccrm-common-new.css">
<link rel="stylesheet" type="text/css" href="<%=contextPath%>/vendor/angular-motion-v0.3.2/angular-motion.css">
<link rel="stylesheet" type="text/css" href="<%=contextPath%>/vendor/zTree/css/ztree.css">
<script type="text/javascript" src="<%=contextPath%>/static/ycrl/javascript/jquery-all.js"></script>
<script type="text/javascript" src="<%=contextPath%>/static/ycrl/javascript/angular-all.js"></script>
<script type="text/javascript" src="<%=contextPath%>/static/ycrl/javascript/angular-strap-all.js"></script>
<script type="text/javascript" src="<%=contextPath%>/static/ycrl/javascript/angular-ztree-all.js"></script>
<script type="text/javascript" src="<%=contextPath%>/app/base/emp/emp.js"></script>
<script>
    window.angular.contextPathURL = '<%=contextPath%>';
</script>
</head >
<body>
<div class="main" ng-app="com.michael.base.region.list" ng-controller="RegionListController">
    <div class="row panel panel-tree">
        <div class="tree" style="width: 380px;">
            <ul id="treeDemo" class="ztree"></ul>
        </div>
        <div class="content" style="padding-left: 385px;">
            <div class="list-result">
                <div class="row block table-list">
                    <div class="block-header">
                        <div class="header-text">
                            <span class="glyphicons list"></span>
                            <span>行政区域</span>
                        </div>
                         <span class="header-button">
                            <a class="btn btn-green btn-min" ng-click="add();">
                                <span class="glyphicons plus"></span> 新建
                            </a>

                            <a class="btn btn-green btn-min" ng-click="remove();" ng-disabled="!anyOne">
                                <span class="glyphicons remove_2"></span> 删除
                            </a>
	                    </span>

                    </div>
                    <div class="block-content">
                        <div class="content-wrap">
                            <div class="table-responsive panel panel-table first-min">
                                <table class="table table-striped table-hover">
                                    <thead class="table-header">
                                    <tr>
                                        <td>
                                            <div select-all-checkbox checkboxes="regions.data" selected-items="items"
                                                 anyone-selected="anyOne"></div>
                                        </td>
                                        <td>名称</td>
                                        <td style="width: 120px;">类型</td>
                                        <td style="width: 120px;">负责人</td>
                                        <td>拼音</td>
                                        <td>区号</td>
                                        <td>邮政编码</td>
                                        <td>上级区域</td>
                                        <td class="length-min">显示顺序</td>
                                        <td class="length-min">操作</td>
                                    </tr>
                                    </thead>
                                    <tbody class="table-body" ng-cloak>
                                    <tr ng-show="!regions.data.length">
                                        <td colspan="10" class="text-center">没有符合条件的记录！</td>
                                    </tr>
                                    <tr bindonce ng-repeat="foo in regions.data">
                                        <td align="center">
                                            <input type="checkbox" ng-model="foo.isSelected"/>
                                        </td>
                                        <td title="点击查询明细！"
                                            style="cursor: pointer;text-decoration: underline;color:#0000ff"
                                            ng-click="view(foo.id)" bo-text="foo.name"></td>
                                        <td class="text-center" bo-text="foo.type | regionType"></td>
                                        <td bo-text="foo.masterName"></td>
                                        <td bo-text="foo.pinyin"></td>
                                        <td bo-text="foo.code"></td>
                                        <td bo-text="foo.zipcode"></td>
                                        <td bo-text="foo.parentName"></td>
                                        <td class="text-center" bo-text="foo.sequenceNo"></td>
                                        <td>
                                            <a ng-click="modify(foo.id);" class="btn-op blue">编辑 </a>
                                            <a ng-click="remove(foo.id);" class="btn-op red">删除 </a>
                                        </td>
                                    </tr>
                                    </tbody>
                                </table>
                            </div>
                        </div>

                    </div>
                </div>
            </div>
            <div class="list-condition" eccrm-page="pager"></div>
        </div>
    </div>
</div>

</div >

</body>
<script type="text/javascript" src="<%=contextPath%>/app/base/region/region.js"></script>
<script type="text/javascript" src="<%=contextPath%>/app/base/region/region-modal.js"></script>
<script type="text/javascript" src="<%=contextPath%>/app/base/region/list/region_list.js"></script>
</html>