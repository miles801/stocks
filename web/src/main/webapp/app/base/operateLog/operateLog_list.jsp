<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String contextPath = request.getContextPath();
%>
<!DOCTYPE html>
<html lang="en" >
<head >
    <title >操作日志</title >
    <meta content="text/html" charset="utf-8" >
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
<div class="main condition-row-2" ng-app="com.michael.base.operatelog" ng-controller="OperateLogCtrl">
    <div class="list-condition" >
        <div class="block" >
            <div class="block-header" >
                <span class="header-text" >
                        <span >操作日志</span >
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
                        <label >操作人:</label >
                    </div >
                    <input class="col-2-half" type="text" ng-model="condition.operator" />

                    <div class="form-label col-1-half" >
                        <label >内容关键字:</label >
                    </div >
                    <input class="col-2-half" type="text" ng-model="condition.description" />

                    <div class="form-label col-1-half" >
                        <label >操作类型:</label >
                    </div >
                    <select ng-model="condition.operateType" class="col-2" ng-options="foo.value as foo.name for foo in operateTypes" > </select >
                </div >
                <div class="row" >
                    <div class="form-label col-1-half" >
                        <label >操作起始时间:</label >
                    </div >
                    <div class="col-2-half" >
                        <input class="col-12" type="text" ng-model="condition.startDate" id="startDate"
                               eccrm-my97="{el:'startDate',dateFmt:'yyyy-MM-dd HH:mm:ss',maxDate:'#F{$dp.$D(\'endDate\')}'}" />
                        <span class="add-on" >
                            <i class="icons clock icon" ng-click="condition.startDate=null;" title="清除" > </i >
                        </span >
                    </div >

                    <div class="form-label col-1-half" >
                        <label >操作截止时间:</label >
                    </div >
                    <div class="col-2-half" >
                        <input class="col-12" type="text" ng-model="condition.endDate" id="endDate"
                               eccrm-my97="{el:'endDate',dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'#F{$dp.$D(\'startDate\')}'}" />
                        <span class="add-on" >
                            <i class="icons clock icon" ng-click="condition.endDate=null;" title="清除" > </i >
                        </span >
                    </div >
                </div >
            </div >
        </div >
    </div >
    <div class="list-result " >
        <div class="block" >
            <div class="block-header" >
                <div class="header-text" >
                    <span class="glyphicons list" ></span >
                    <span >日志记录</span >
                </div >
            </div >
            <div class="block-content" >
                <div class="content-wrap" >
                    <div class="table-responsive panel panel-table first-min" >
                        <table class="table table-striped table-hover" >
                            <thead class="table-header" >
                            <tr >
                                <td >操作人</td >
                                <td >操作时间</td >
                                <td >操作类型</td >
                                <td >描述</td >
                                <td >操作内容</td >
                            </tr >
                            </thead >
                            <tbody class="table-body" >
                            <tr ng-show="!beans.total" >
                                <td colspan="5" class="text-center" >没有符合条件的记录！</td >
                            </tr >
                            <tr bindonce ng-repeat="foo in beans.data" ng-cloak >

                                <td bo-text="foo.creatorName" ></td >
                                <td bo-text="foo.createdDatetime | eccrmDatetime" ></td >
                                <td bo-text="foo.operateType | uppercase" ></td >
                                <td bo-text="foo.description" ></td >
                                <td bo-text="foo.content | substr:300" ></td >
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
<script type="text/javascript" src="<%=contextPath%>/app/base/operateLog/operateLog.js" ></script >
</html >