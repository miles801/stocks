<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String contextPath = request.getContextPath();
%>
<!DOCTYPE html>
<html lang="en" >
<head >
    <title >数据字典列表</title >
    <meta content="text/html" charset="utf-8" >
    <link rel="stylesheet" type="text/css" href="<%=contextPath%>/vendor/bootstrap-v3.0/css/bootstrap.min.css" >
    <link rel="stylesheet" type="text/css" href="<%=contextPath%>/style/standard/css/eccrm-common-new.css" >
    <link rel="stylesheet" type="text/css" href="<%=contextPath%>/vendor/angular-motion-v0.3.2/angular-motion.css" >
    <script type="text/javascript" src="<%=contextPath%>/static/ycrl/javascript/jquery-all.js" ></script >
    <script type="text/javascript" src="<%=contextPath%>/static/ycrl/javascript/angular-all.js" ></script >
    <script type="text/javascript" src="<%=contextPath%>/static/ycrl/javascript/angular-strap-all.js" ></script >
    <script type="text/javascript" >
        window.contextPathURL = "<%=contextPath%>";
    </script >
</head >
<body >
<div class="main condition-row-1" ng-app="com.michael.base.datadic.list" ng-controller="MainCtrl">
    <div class="block" >
        <div class="list-condition" >
            <div class="block" >
                <div class="block-header" >
                    <span class="header-button" >
                        <a class="btn btn-green btn-min" ng-click="query();" >查询 </a >
                    </span >
                </div >
                <div class="block-content" >
                    <div class="row" >
                        <div class="form-label col-1-half" >
                            <label >字典名称:</label >
                        </div >
                        <div class="col-2-half" >
                            <input type="text" class="col-12" ng-model="condition.name" />
                        </div >
                        <div class="form-label col-1-half" >
                            <label >字典编号:</label >
                        </div >
                        <div class="col-2-half" >
                            <input type="text" class="col-12" ng-model="condition.code" />
                        </div >
                    </div >
                </div >
            </div >
        </div >
        <div class="list-result" >
            <div class="block-header" >
                            <span class="header-text" >
                                <span >数据字典</span >
                            </span >
                            <span class="header-button" >
                                <a class="btn btn-green btn-min" ng-click="add();" >新建 </a >
                            </span >
            </div >
            <div class="block-content" >
                <div class="table-responsive panel panel-table" >
                    <table class="table table-striped table-hover" >
                        <thead class="table-header" >
                        <tr >
                            <td >字典名称</td >
                            <td >编号</td >
                            <td >类</td >
                            <td >描述</td >
                            <td >操作</td >
                        </tr >
                        </thead >
                        <tbody class="table-body" ng-cloak >
                        <tr ng-show="beans.length == 0" >
                            <td colspan="5" class="text-center" >没有符合条件的记录！</td >
                        </tr >
                        <tr bindonce ng-repeat="bean in beans" >
                            <td title="点击查询明细！" style="cursor: pointer;" >
                                <a ng-click="detail(bean.id);" bo-text="bean.name" ></a >
                            </td >
                            <td bo-text="bean.code" ></td >
                            <td bo-text="bean.className" ></td >
                            <td bo-text="bean.description | substr" ></td >
                            <td >
                                <a ng-click="modify(bean.id)" class="btn btn-tiny ph0" title="编辑" >
                                    <i class="icons edit" ></i >
                                </a >
                                <a ng-click="remove(bean.id,$index);" class="btn btn-tiny ph0" title="删除！" >
                                    <i class="icons fork" ></i >
                                </a >
                            </td >
                        </tr >
                        </tbody >
                    </table >
                </div >
            </div >
        </div >
        <div class="list-pagination" eccrm-page="pager" ></div >
    </div >
</div >

<script type="text/javascript" src="<%=contextPath%>/app/base/datadic/datadic.js" ></script >
<script type="text/javascript" src="<%=contextPath%>/app/base/datadic/datadic_list.js" ></script >
</body >

</html >