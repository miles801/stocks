<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String contextPath = request.getContextPath();
%>
<!DOCTYPE html>
<html lang="en" >
<head >
    <title >附件管理</title >
    <meta content="text/html" charset="utf-8" >
    <meta http-equiv="X-UA-Compatible" content="IE=EmulateIE8" />
    <link rel="stylesheet" type="text/css" href="<%=contextPath%>/vendor/artDialog/artDialog.css" />
    <link rel="stylesheet" type="text/css" href="<%=contextPath%>/vendor/bootstrap-v3.0/css/bootstrap.min.css" >
    <link rel="stylesheet" type="text/css" href="<%=contextPath%>/style/standard/css/eccrm-common-new.css" >
    <link rel="stylesheet" type="text/css" href="<%=contextPath%>/vendor/angular-motion-v0.3.2/angular-motion.css" >
    <script type="text/javascript" src="<%=contextPath%>/static/ycrl/javascript/jquery-all.js" ></script >
    <script type="text/javascript" src="<%=contextPath%>/static/ycrl/javascript/angular-all.js" ></script >
    <script type="text/javascript" src="<%=contextPath%>/static/ycrl/javascript/angular-strap-all.js" ></script >
    <script type="text/javascript" src="<%=contextPath%>/vendor/My97DatePicker/WdatePicker.js" ></script >
    <script type="text/javascript" >
        window.angular.contextPathURL = "<%=contextPath%>";
    </script >
</head >
<body >
<div class="main condition-row-2" ng-app="base.attachment.list" ng-controller="Ctrl" >
    <div class="list-condition" >
        <div class="block" >
            <div class="block-header" >
                <span class="header-text" >
                        <span >附件管理</span >
                </span >
                <span class="header-button" >
                        <button type="button" class="btn btn-green btn-min" ng-click="query();" >
                            <span class="glyphicons search" ></span >
                            查询
                        </button >
                        <button type="button" class="btn btn-green btn-min" ng-click="reset();" >
                            <span class="glyphicons repeat" ></span >
                            重置
                        </button >
                </span >
            </div >
            <div class="block-content" >
                <div class="row" >
                    <div class="form-label col-1-half" >
                        <label >文件名称:</label >
                    </div >
                    <input class="col-2-half" type="text" ng-model="condition.fileName" />

                    <div class="form-label col-1-half" >
                        <label >文件大于:</label >
                    </div >
                    <select ng-model="condition.lowSize" class="col-2-half" ng-options="foo.value as foo.name for foo in size" > </select >

                    <div class="form-label col-1-half" >
                        <label >文件小于:</label >
                    </div >
                    <select ng-model="condition.highSize" class="col-2-half" ng-options="foo.value as foo.name for foo in size" > </select >

                </div >
                <div class="row" >
                    <div class="form-label col-1-half" >
                        <label >生效时间:</label >
                    </div >
                    <div class="col-2-half" >
                        <input type="text" class="col-12" ng-model="condition.uploadTimeMin" readonly
                               eccrm-my97="{el:'startTime',dateFmt:'yyyy-MM-dd HH:mm:ss',maxDate:'#F{$dp.$D(\'endTime\')}'}" id="startTime" />
                                <span class="add-on" >
                                    <i class="icons clock icon" title="清除时间" ng-click="condition.uploadTimeMin=undefined;" ></i >
                                </span >
                    </div >

                    <div class="form-label col-1-half" >
                        <label >截止时间:</label >
                    </div >
                    <div class="col-2-half" >
                        <input class="col-12" type="text" ng-model="condition.uploadTimeMax"
                               readonly eccrm-my97="{el:'endTime',dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'#F{$dp.$D(\'startTime\')}'}" id="endTime" />
                        <span class="add-on" >
                            <i class="icons clock icon" title="清除时间" ng-click="condition.uploadTimeMax=undefined;" ></i >
                        </span >
                    </div >
                    <div class="form-label col-1-half" >
                        <label >状态:</label >
                    </div >
                    <select ng-model="condition.status" class="col-2-half" ng-options="foo.value as foo.name for foo in status" > </select >

                </div >
            </div >
        </div >
    </div >
    <div class="list-result " >
        <div class="block" >
            <div class="block-header" >
                <div class="header-text" >
                    <span class="glyphicons list" ></span >
                    <span >附件列表</span >
                </div >
                <span class="header-button" >
                        <button type="button" class="btn btn-green btn-min" ng-click="remove();" ng-disabled="!anyone" >
                            <span class="glyphicons remove" ></span >
                            删除
                        </button >
                </span >
            </div >
            <div class="block-content" >
                <div class="content-wrap" >
                    <div class="table-responsive panel panel-table first-min" >
                        <table class="table table-striped table-hover ta-c" >
                            <thead class="table-header" >
                            <tr >
                                <td >
                                    <div select-all-checkbox checkboxes="beans.data" selected-items="items" anyone-selected="anyone" ></div >
                                </td >
                                <td >文件名称</td >
                                <td >路径</td >
                                <td >业务类型</td >
                                <td >业务ID</td >
                                <td >文件大小</td >
                                <td >上传人</td >
                                <td >上传时间</td >
                                <td >状态</td >
                                <td >操作</td >
                            </tr >
                            </thead >
                            <tbody class="table-body" >
                            <tr ng-show="!beans || !beans.total" >
                                <td colspan="10" class="text-center" >没有符合条件的记录！</td >
                            </tr >
                            <tr bindonce ng-repeat="foo in beans.data" ng-cloak >
                                <td >
                                    <input type="checkbox" ng-model="foo.isSelected" />
                                </td >

                                <td bo-text="foo.fileName" class="text-left" ></td >
                                <td bo-text="foo.path | substr:100" class="text-left" bo-title="foo.path" ></td >
                                <td bo-text="foo.businessType | substr:40" ></td >
                                <td bo-text="foo.businessId" ></td >
                                <td bo-text="foo.size | fileSize" ></td >
                                <td bo-text="foo.creatorName" ></td >
                                <td bo-text="foo.uploadTime | eccrmDatetime" ></td >
                                <td bo-text="foo.statusName" ></td >
                                <td >
                                    <a title="删除附件" ng-click="remove(foo.id)" style="cursor: pointer;" ><i class="icons fork" ></i ></a >
                                    <a title="下载" ng-click="download(at.id)" style="cursor: pointer;margin-left: 8px;" ><i class="icons download" ></i ></a >
                                </td >
                            </tr >
                            </tbody >
                        </table >
                    </div >
                </div >
            </div >
        </div >
    </div >
    <div class="list-pagination" eccrm-page="pager" ></div >
</div >
</div >

</body >
<script type="text/javascript" src="<%=contextPath%>/app/base/attachment/attachment.js" ></script >
<script type="text/javascript" src="<%=contextPath%>/app/base/attachment/attachment_list.js" ></script >
</html >