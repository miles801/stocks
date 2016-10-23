<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String contextPath = request.getContextPath();
%>
<!DOCTYPE html>
<html lang="en">

<head>
    <title>编辑消息</title>
    <meta content="text/html" charset="utf-8">
    <link rel="stylesheet" type="text/css" href="<%=contextPath%>/vendor/bootstrap-v3.0/css/bootstrap.min.css">
    <link rel="stylesheet" type="text/css" href="<%=contextPath%>/style/standard/css/eccrm-common-new.css">
    <link rel="stylesheet" type="text/css" href="<%=contextPath%>/vendor/zTree/css/ztree.css">
    <script type="text/javascript" src="<%=contextPath%>/static/ycrl/javascript/jquery-all.js"></script>
    <script type="text/javascript" src="<%=contextPath%>/static/ycrl/javascript/angular-all.js"></script>
    <script type="text/javascript" src="<%=contextPath%>/static/ycrl/javascript/angular-strap-all.js"></script>
    <script type="text/javascript" src="<%=contextPath%>/static/ycrl/javascript/angular-ztree-all.js"></script>
    <script type="text/javascript" src="<%=contextPath%>/vendor/kindeditor-4.1.10/kindeditor-min.js"></script>
    <script type="text/javascript" src="<%=contextPath%>/vendor/kindeditor-4.1.10/lang/zh_CN.js"></script>
    <script type="text/javascript">
        window.angular.contextPathURL = "<%=contextPath%>";
    </script>
</head>
<body>
<div class="main" ng-app="base.message.edit" ng-controller="Ctrl">
    <div class="block">
        <div class="block-header">
                <span class="header-text">
                    <span class="glyphicons info-sign"></span>
                </span>
                <span class="header-button">
                    <c:if test="${pageType eq 'add'}">
                    <button type="button" class="btn btn-green btn-min" ng-click="save()" ng-disabled="form.$invalid">
                        <span class="glyphicons disk_save"></span> 发送
                    </button>
                        <button type="button" class="btn btn-green btn-min" ng-click="save(true)"
                                ng-disabled="form.$invalid">
                            <span class="glyphicons disk_open"></span> 发送并再次创建
                        </button>
                    </c:if>
                    <a type="button" class="btn btn-green btn-min" ng-click="back();">
                        <span class="glyphicons message_forward"></span> 返回
                    </a>
                </span>
        </div>
        <div class="block-content">
            <div class="content-wrap">
                <form name="form" class="form-horizontal" role="form">
                    <div style="display: none;">
                        <input type="hidden" id="pageType" value="${pageType}"/>
                        <input type="hidden" id="id" value="${id}"/>
                    </div>
                    <div class="row">
                        <div class="form-label col-1-half">
                            <label>主题:</label>
                        </div>
                        <input class="col-6-half" type="text" ng-model="beans.title" validate validate-required/>
                        <div class="form-label col-1-half">
                            <label validate-error="form.type">消息类型:</label>
                        </div>
                        <select name="type" class="col-2-half" ng-model="beans.type" validate validate-required
                                ng-options="foo.value as foo.name for foo in type"></select>
                    </div>
                    <div class="row">
                        <div class="form-label col-1-half">
                            <label validate-error="form.type">接收人:</label>
                        </div>
                        <div class="col-10-half">
                            <input class="col-12" type="text" ng-model="beans.receiverName" validate validate-required
                                   readonly ng-click="pickEmp();" placeholder="点击选择接收人"/>
                            <span class="add-on"><i class="icons icon user cp" ng-click="clearEmp();"
                                                    title="清除"></i></span>
                        </div>
                    </div>
                    <div class="row">
                        <div class="form-label col-1-half">
                            <label>内容:</label>
                        </div>
                        <textarea id="content" rows="35" class="col-10-half" ng-model="beans.content"></textarea>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
</body>
<script type="text/javascript" src="<%=contextPath%>/app/base/message/message.js"></script>
<script type="text/javascript" src="<%=contextPath%>/app/base/emp/emp.js"></script>
<script type="text/javascript" src="<%=contextPath%>/app/base/message/edit/message_edit.js"></script>
</html>