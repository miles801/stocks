<%--
Created by IntelliJ IDEA.
User: wangsd
Date: 2014-01-14 11:23:09
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="isAdd" value="${pageType eq null or pageType eq 'add'}" ></c:set >
<c:set var="isModify" value="${pageType eq null or pageType eq 'modify'}" ></c:set >
<c:set var="isDetail" value="${pageType ne null and pageType eq 'detail'}" ></c:set >
<%
    String contextPath = request.getContextPath();
%>
<!DOCTYPE html >
<html >
<head >
    <title>编辑公司公告</title>
    <meta http-equiv="X-UA-Compatible" content="IE=EmulateIE8" />
    <link rel="stylesheet" type="text/css" href="<%=contextPath%>/vendor/bootstrap-v3.0/css/bootstrap.min.css" >
    <link rel="stylesheet" type="text/css" href="<%=contextPath%>/style/standard/css/eccrm-common-new.css" >
    <link rel="stylesheet" type="text/css" href="<%=contextPath%>/vendor/zTree/css/ztree.css" >

    <script type="text/javascript" src="<%=contextPath%>/static/ycrl/javascript/jquery-all.js" ></script >
    <script type="text/javascript" src="<%=contextPath%>/static/ycrl/javascript/angular-all.js" ></script >
    <script type="text/javascript" src="<%=contextPath%>/static/ycrl/javascript/angular-strap-all.js" ></script >
    <script type="text/javascript" src="<%=contextPath%>/static/ycrl/javascript/angular-ztree-all.js" ></script >
    <script type="text/javascript" src="<%=contextPath%>/vendor/My97DatePicker/WdatePicker.js" ></script >

    <%-- 富文本 --%>
    <script type="text/javascript" src="<%=contextPath%>/vendor/kindeditor-4.1.10/kindeditor-min.js"></script>
    <script type="text/javascript" src="<%=contextPath%>/vendor/kindeditor-4.1.10/lang/zh_CN.js"></script>

    <script type="text/javascript" src="<%=contextPath%>/app/org/org.js" charset="utf-8" ></script >
    <script type="text/javascript" src="<%=contextPath%>/app/org/orgPosition.js" charset="utf-8" ></script >

    <%-- 附件 --%>
    <script type="text/javascript" src="<%=contextPath%>/static/ycrl/javascript/angular-upload.js" ></script >
    <script type="text/javascript" src="<%=contextPath%>/app/base/im/news/news.js" ></script >
    <script type="text/javascript" >
        window.angular.contextPathURL = '<%=contextPath%>';
    </script >
</head >
<body id="ng-app" >
<div class="main" ng-app="eccrm.im.news.edit" ng-controller="Ctrl" >
    <div class="row-10 block" >
        <div class="block-header" >
                <span class="header-text" >
                    <span class="glyphicons circle_info" ></span >
                        <c:if test="${isAdd}" ><span >新增公告</span ></c:if >
                </span >
                <span class="header-button" >
                	<c:if test="${isAdd}" >
                        <button type="button" class="btn btn-sm btn-green btn-min" ng-click="save()"
                                ng-disabled="!form.$valid" ng-cloak >
                            <span class="glyphicons message_out" ></span > 保存
                        </button >
                    </c:if >
                	<c:if test="${isModify}" >
                        <button type="button" class="btn btn-sm btn-green btn-min" ng-click="update()"
                                ng-disabled="!form.$valid" ng-cloak >
                            <span class="glyphicons message_out" ></span > 更新
                        </button >
                    </c:if >
                    <button type="button" class="btn btn-sm btn-green btn-min" ng-click="back()" >
                        <span class="glyphicons message_out" ></span > 返回
                    </button >
                </span >
        </div >
        <div class="block-content" >
            <div class="content-wrap" >
                <form name="form" class="form-horizontal" role="form" >
                    <div style="display: none;" >
                        <input type="hidden" id="pageType" value="${pageType}" />
                        <input type="hidden" id="id" value="${id}" />
                    </div >
                    <div class="row" >
                        <div class="form-label col-1-half" >
                            <label >标题:</label >
                        </div >
                        <input class="col-6-half" type="text" ng-model="beans.title" validate
                               validate-required autofocus validate-max-length="80" />

                        <div class="form-label col-1-half" >
                            <label >公告类型:</label >
                        </div >
                        <select ng-model="beans.category" class="col-2-half" ng-options="foo.value as foo.name for foo in newsTypes" validate validate-required >
                        </select >
                    </div >
                    <div class="row" >
                        <div class="form-label col-1-half" >
                            <label >生效时间:</label >
                        </div >
                        <div class="col-2-half" >
                            <input type="text" class="col-12"
                                   id="startTime"
                                   ng-model="beans.startTime" eccrm-my97="{el:'startTime',autoPickDate:false,dateFmt:'yyyy-MM-dd',maxDate:'#F{$dp.$D(\'endTime\')}'}" />
                            <span class="add-on" >
                                 <i class="icons clock icon" ng-click="beans.startTime=null" > </i >
                            </span >
                        </div >

                        <div class="form-label col-1-half" >
                            <label >失效时间:</label >
                        </div >
                        <div class="col-2-half" >
                            <input type="text" class="col-12"
                                   id="endTime"
                                   ng-model="beans.endTime" eccrm-my97="{el:'endTime',autoPickDate:false,dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'startTime\')}'}" />
                            <span class="add-on" >
                                 <i class="icons clock icon" ng-click="beans.startTime=null" > </i >
                            </span >
                        </div >
                        <div class="col-1-half" >&nbsp;</div >
                        <div class="col-2-half " >
                            <label >
                                <input type="checkbox" ng-model="beans.isTop" >&nbsp;置顶
                            </label >
                            <label style="margin-left: 10px" >
                                <input type="checkbox" ng-model="beans.recordRead" >&nbsp;记录已阅
                            </label >
                        </div >
                    </div >
                    <div class="row" >
                        <div class="form-label col-1-half" >
                            <label >公告内容:</label >
                        </div >
                        <div class="col-10-half" >
                            <textarea id="content" class="col-12" rows="20" ></textarea >
                        </div >
                    </div >
                    <div class="row" eccrm-upload="options" >
                    </div >

                    <%-- 只有在新增和编辑页面才可以看到配置的接收人信息 --%>
                    <c:if test="${isDetail ne true}" >
                        <%-- 收件人 --%>
                        <div class="row" >
                            <div class="form-label col-1-half" >
                                <label >接收人类型:</label >
                            </div >
                            <label style="margin-left:10px;" ><input type="radio" name="receiver" value="ALL" ng-model="beans.receiverType" />全部</label >
                            <label style="margin-left:10px;" ><input type="radio" name="receiver" value="ORG" ng-model="beans.receiverType" />机构</label >
                            <label style="margin-left:10px;" ><input type="radio" name="receiver" value="ORG_POSITION" ng-model="beans.receiverType" />机构岗位</label >
                            <label style="margin-left:10px;" ><input type="radio" name="receiver" value="PARAM" ng-model="beans.receiverType" />业态</label >
                        </div >

                        <div class="row" >
                            <div class="form-label col-1-half" >
                                <label >接收人:</label >
                            </div >
                            <div class="col" ng-if="beans.receiverType=='ALL'" >
                                <span >全部</span >
                            </div >
                                <%-- 选择机构 --%>
                            <div class="col-10-half" ng-show="beans.receiverType=='ORG'" >
                                <div class="col-6" style="padding-right: 10px;" >
                                    <div class="tree" style="border: 1px solid #DDDDDD;height: 400px;overflow: auto;" >
                                        <div class="tree-content" >
                                            <ul id="orgTree" class="ztree" ></ul >
                                        </div >
                                    </div >
                                </div >
                                <div class="col-6" >
                                    <div class="table-responsive panel panel-table" >
                                        <table class="table table-striped table-hover" style="border: 1px solid #b4d0e8" >
                                            <thead class="table-header" >
                                            <tr >
                                                <td ></td >
                                                <td >机构名称</td >
                                                <td class="width-min" >操作</td >
                                            </tr >
                                            </thead >
                                            <tbody class="table-body" ng-cloak >
                                            <tr ng-show="!orgs || orgs.length<1" >
                                                <td colspan="3" class="text-center" >未选择机构！</td >
                                            </tr >
                                            <tr ng-repeat="foo in orgs" >
                                                <td class="width-min" ng-bind-template="{{ $index+1 }}" ></td >
                                                <td ng-bind-template="{{ foo.longName }}" ></td >
                                                <td >
                                                    <a ng-click="removeOrg($index);" class="btn btn-tiny" title="删除！" >
                                                        <i class="icons fork" ></i >
                                                    </a >
                                                </td >
                                            </tr >
                                            </tbody >
                                        </table >
                                    </div >
                                </div >
                            </div >
                                <%-- 选择机构岗位 --%>
                            <div class="col-10-half" ng-show="beans.receiverType=='ORG_POSITION'" >
                                <div class="col-6" style="padding-right: 10px;" >
                                    <div class="tree" style="border: 1px solid #DDDDDD;height: 400px;overflow: auto;" >
                                        <div class="tree-content" >
                                            <ul id="orgPositionTree" class="ztree" ></ul >
                                        </div >
                                    </div >
                                </div >
                                <div class="col-6" >
                                    <div class="table-responsive panel panel-table" >
                                        <table class="table table-striped table-hover" style="border: 1px solid #b4d0e8" >
                                            <thead class="table-header" >
                                            <tr >
                                                <td ></td >
                                                <td >机构名称</td >
                                                <td >岗位名称</td >
                                                <td class="width-min" >操作</td >
                                            </tr >
                                            </thead >
                                            <tbody class="table-body" ng-cloak >
                                            <tr ng-show="!positions || positions.length<1" >
                                                <td colspan="4" class="text-center" >未选择岗位！</td >
                                            </tr >
                                            <tr ng-repeat="foo in positions" >
                                                <td class="width-min" ng-bind-template="{{ $index+1 }}" ></td >
                                                <td ng-bind-template="{{ foo.orgName }}" ></td >
                                                <td ng-bind-template="{{ foo.name }}" ></td >
                                                <td >
                                                    <a ng-click="removeOrg($index);" class="btn btn-tiny" title="删除！" >
                                                        <i class="icons fork" ></i >
                                                    </a >
                                                </td >
                                            </tr >
                                            </tbody >
                                        </table >
                                    </div >
                                </div >
                            </div >
                                <%-- 选择业态 --%>
                            <div class="col-10-half" ng-show="beans.receiverType=='PARAM'" >
                            <span ng-repeat="foo in params" style="margin:10px 15px;" >
                                <label >
                                    <input type="checkbox" ng-model="foo.selected" />
                                    <span ng-bind-template="{{foo.name}}" ></span >
                                </label >
                            </span >
                            </div >
                        </div >
                    </c:if >
                </form >
            </div >
        </div >
    </div >
</div >
<script type="text/javascript" src="<%=contextPath%>/app/base/im/news/news_edit.js" ></script >
</body >

</html >