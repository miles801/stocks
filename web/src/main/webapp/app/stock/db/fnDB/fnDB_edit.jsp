<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%String contextPath = request.getContextPath();%><!DOCTYPE html>
<html lang="en">
<head>
    <title>Fn数据库编辑</title>
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
<div class="main" ng-app="stock.db.fnDB.edit" ng-controller="Ctrl" style="overflow: auto;">
    <div class="block">
        <div class="block-header">
                <span class="header-button">
                    <c:if test="${pageType eq 'add'}">
                        <a class="btn btn-green btn-min" ng-click="save()" ng-disabled="!form.$valid">保存 </a>
                        <a class="btn btn-green btn-min" ng-click="save(true)" ng-disabled="!form.$valid">保存并新建 </a> </c:if>
                    <c:if test="${pageType eq 'modify'}">
                        <a type="button" class="btn btn-green btn-min" ng-click="update()" ng-disabled="!form.$valid"> 更新 </a> </c:if>
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
                                <label>所属数据库:</label>
                            </div>
                            <input type="text" class="w200" ng-model="beans.type" validate validate-required maxlength="40"/>
                        </div>
                        <div class="item w300">
                            <div class="form-label w100">
                                <label>日期:</label>
                            </div>
                            <div class="w200 pr">
                                <input type="text" class="w200" ng-model="beans.fnDate" eccrm-my97="{}" readonly placeholder="点击选择日期"/>
                                <span class="add-on"><i class="icons icon clock" ng-click="beans.fnDate=null" title="点击清除日期"></i></span>
                            </div>
                        </div>
                        <div class="item w300">
                            <div class="form-label w100">
                                <label>fn系数:</label>
                            </div>
                            <input type="text" class="w200" ng-model="beans.fn" validate validate-required maxlength=""/>
                        </div>
                        <div class="item w300">
                            <div class="form-label w100">
                                <label>原始日期:</label>
                            </div>
                            <div class="w200 pr">
                                <input type="text" class="w200" ng-model="beans.originDate" eccrm-my97="{}" readonly placeholder="点击选择日期"/>
                                <span class="add-on"><i class="icons icon clock" ng-click="beans.originDate=null" title="点击清除日期"></i></span>
                            </div>
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
<script type="text/javascript" src="<%=contextPath%>/app/stock/db/fnDB/fnDB.js"></script>
<script type="text/javascript" src="<%=contextPath%>/app/stock/db/fnDB/fnDB_edit.js"></script>
</html>