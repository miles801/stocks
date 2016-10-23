<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String contextPath = request.getContextPath();
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>登录日志编辑</title>
    <meta http-equiv="X-UA-Compatible" content="IE=EmulateIE8"/>
    <link rel="stylesheet" type="text/css" href="<%=contextPath%>/vendor/bootstrap-v3.0/css/bootstrap.min.css">
    <link rel="stylesheet" type="text/css" href="<%=contextPath%>/style/standard/css/eccrm-common-new.css">
    <script type="text/javascript" src="<%=contextPath%>/static/ycrl/javascript/jquery-all.js"></script>
    <script type="text/javascript" src="<%=contextPath%>/static/ycrl/javascript/angular-all.js"></script>
    <script type="text/javascript" src="<%=contextPath%>/static/ycrl/javascript/angular-strap-all.js"></script>
    <script type="text/javascript" src="<%=contextPath%>/vendor/My97DatePicker/WdatePicker.js"></script>
    <script>
        window.angular.contextPathURL = '<%=contextPath%>';
    </script>
</head>
<body>
<div class="main" ng-app="base.log.loginLog.edit" ng-controller="Ctrl" style="overflow: auto;">
    <div class="block">
        <div class="block-header">
                <span class="header-button">
                    <c:if test="${pageType eq 'add'}">
                        <a class="btn btn-green btn-min" ng-click="save()" ng-disabled="!form.$valid">保存 </a>
                        <a class="btn btn-green btn-min" ng-click="save(true)" ng-disabled="!form.$valid">保存并新建 </a>
                    </c:if>
                    <c:if test="${pageType eq 'modify'}">
                        <a type="button" class="btn btn-green btn-min" ng-click="update()" ng-disabled="!form.$valid"> 更新 </a>
                    </c:if>
                    <a class="btn btn-green btn-min" ng-click="back();">返回 </a>
                </span>
        </div>
        <div class="block-content">
            <div class="content-wrap">
                <form name="form" class="form-horizontal" role="form">
                    <div style="display: none;">
                        <input type="hidden" id="pageType" value="${pageType}"/>
                        <input type="hidden" id="id" value="${id}"/>
                    </div>
                    <div class="row float">
                        <div class="item w300">
                            <div class="form-label w100">
                                <label>登录IP:</label>
                            </div>
                            <input type="text" class="w200" ng-model="beans.ip" validate validate-required
                                   maxlength="20"/>
                        </div>
                        <div class="item w300">
                            <div class="form-label w100">
                                <label>登录时间:</label>
                            </div>
                            <div class="w200 pr">
                                <input type="text" class="w200" ng-model="beans.loginTime"
                                       eccrm-my97="{dateFmt:'yyyy-MM-dd HH:mm:ss'}" readonly placeholder="点击选择时间"/>
                                <span class="add-on"><i class="icons icon clock" ng-click="beans.loginTime=null"
                                                        title="点击清除"></i></span>
                            </div>
                        </div>
                        <div class="item w300">
                            <div class="form-label w100">
                                <label>退出时间:</label>
                            </div>
                            <div class="w200 pr">
                                <input type="text" class="w200" ng-model="beans.logoutTime"
                                       eccrm-my97="{dateFmt:'yyyy-MM-dd HH:mm:ss'}" readonly placeholder="点击选择时间"/>
                                <span class="add-on"><i class="icons icon clock" ng-click="beans.logoutTime=null"
                                                        title="点击清除"></i></span>
                            </div>
                        </div>
                        <div class="item w300">
                            <div class="form-label w100">
                                <label validate-error="form.type">退出方式:</label>
                            </div>
                            <select class="w200" ng-model="beans.type" name="type"
                                    ng-options="foo.value as foo.name for foo in types">
                            </select>
                        </div>
                        <div class="item w900 break">
                            <div class="form-label w100">
                                <label>备注:</label>
                            </div>
                            <textarea class="w800" rows="4" ng-model="beans.description" maxlength="1000"></textarea>
                        </div>
                    </div>
                    <c:if test="${pageType ne 'add'}">
                        <div class="row float break" ng-cloak>
                            <div class="item w300">
                                <div class="form-label w100">
                                    <label>创建人:</label>
                                </div>
                                <span class="w200 sl">{{beans.creatorName}}</span>
                            </div>
                            <div class="item w300">
                                <div class="form-label w100">
                                    <label>创建时间:</label>
                                </div>
                                <div class="w200 sl">{{beans.createdDatetime|eccrmDatetime}}</div>
                            </div>
                            <div class="item w300">
                                <div class="form-label w100">
                                    <label>最后更新人:</label>
                                </div>
                                <span class="w200 sl">{{beans.modifierName}}</span>
                            </div>
                            <div class="item w300">
                                <div class="form-label w100">
                                    <label>最后时间:</label>
                                </div>
                                <div class="w200 sl">{{beans.modifiedDatetime|eccrmDatetime}}</div>
                            </div>
                        </div>
                    </c:if>
                </form>
            </div>
        </div>
    </div>
</div>
</body>
<script type="text/javascript" src="<%=contextPath%>/app/base/log/loginLog/loginLog.js"></script>
<script type="text/javascript" src="<%=contextPath%>/app/base/log/loginLog/loginLog_edit.js"></script>
</html>