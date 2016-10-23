<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String contextPath = request.getContextPath();
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>编辑资源</title>
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
    <script type="text/javascript" src="<%=contextPath%>/static/ycrl/javascript/angular-upload.js"></script>
    <script>
        window.angular.contextPathURL = "<%=contextPath%>";
    </script>
</head>
<body>
<div class="main" ng-app="base.resource.edit" ng-controller="Ctrl">
    <div class="block">
        <div class="block-header">
                <span class="header-text">
                    <span class="glyphicons circle_info"></span>
                </span>
                <span class="header-button">
                    <c:if test="${pageType eq 'add'}">
                        <button type="button" class="btn btn-green btn-min"
                                ng-click="save()" ng-disabled="form.$invalid" ng-cloak>
                            <span class="glyphicons floppy_saved"></span> 保存
                        </button>
                        <button type="button" class="btn btn-green btn-min"
                                ng-click="save(true)" ng-disabled="form.$invalid" ng-cloak>
                            <span class="glyphicons floppy_saved"></span> 保存并新建
                        </button>
                    </c:if>
                    <c:if test="${pageType eq 'modify'}">
                        <button type="button" class="btn btn-green btn-min"
                                ng-click="update()" ng-disabled="form.$invalid" ng-cloak>
                            <span class="glyphicons claw_hammer"></span> 更新
                        </button>
                    </c:if>
                    <a type="button" class="btn btn-green btn-min"
                       ng-click="back();">
                        <span class="glyphicons message_forward"></span> 返回
                    </a>
                </span>
        </div>
        <div class="block-content">
            <div class="content-wrap">
                <form name="form" class="form-horizontal" role="form">
                    <div style="display: none;">
                        <input type="hidden" id="pageType" value="${pageType}"/>
                        <input type="hidden" id="type" value="${type}"/>
                        <input type="hidden" id="id" value="${id}"/>
                    </div>
                    <div class="row">
                        <div class="form-label col-1-half">
                            <label>资源名称:</label>
                        </div>
                        <input type="text" name="name" class="col-2-half" placeholder="请输入资源名称" ng-model="beans.name"
                               validate validate-required validate-min-length="2" validate-max-length="20"
                               maxlength="20" data-placement="right"/>

                        <div class="form-label col-1-half">
                            <label>资源编号:</label>
                        </div>
                        <input type="text" name="code" class="col-2-half" ng-model="beans.code"
                               validate validate-required validate-max-length="40" maxlength="40"
                               placeholder="由字母、数字、下划线组成"/>

                        <div class="form-label col-1-half">
                            <label validate-error="form.type">资源类型:</label>
                        </div>
                        <select ng-model="beans.type" name="type" validate validate-required class="col-2"
                                ng-options="foo.value as foo.name for foo in type" ng-change="typeChanged();"> </select>
                    </div>
                    <div class="row">
                        <div class="form-label col-1-half">
                            <label>显示顺序:</label>
                        </div>
                        <input type="number" name="sequenceNo" class="col-2-half" placeholder="请输入一个正整数..."
                               ng-model="beans.sequenceNo"
                               validate validate-int validate-min-value="0" validate-max-value="999"/>


                        <div class="form-label col-1-half">
                            <label for="authorization">需要授权:</label>
                        </div>
                        <input type="checkbox" class="col" id="authorization" ng-model="beans.authorization"
                               value="true"/>
                        <span class="col" style="margin-left:15px;font-size: 10px;color: #999292;">注意：如果勾选，则表示该资源需要授权了才可以访问，如果不勾选，则表示该资源任何人都可以访问!</span>
                    </div>
                    <div class="row">
                        <div class="form-label col-1-half">
                            <label>上级资源:</label>
                        </div>
                        <div class="col-2-half">
                            <input type="text" class="col-12" ztree-single="ztreeOptions"
                                   ng-model="beans.parentName"/>
                            <span class="add-on">
                                <i class="icons circle_fork icon" ng-click="clearParent()" title="清除"></i>
                            </span>
                        </div>
                        <div class="form-label col-1-half" ng-cloak ng-if="beans.type==='MENU'">
                            <label for="url">URL:</label>
                        </div>
                        <div class="col-6" ng-cloak ng-if="beans.type==='MENU'">
                            <input type="text" id="url" class="col-12" ng-model="beans.url" maxlength="100"/>
                        </div>
                    </div>
                    <div class="row" ng-cloak ng-show="beans.type=='MENU' && !beans.parentId">
                        <div eccrm-upload="uploadOptions"></div>
                        <div id="icon" class="col" ng-show="beans.icon"
                             style="border: 1px dashed #DAF3F5;padding: 5px 10px;"></div>
                        <i class="icons icon fork cp col" ng-show="beans.icon" ng-click="removePicture()"
                           ng-cloak ng-show="pageType!=='detail' && beans.icon" style="margin-left: 8px;"></i>
                        <span>&nbsp;</span>
                    </div>
                    <div class="row">
                        <div class="form-label col-1-half">
                            <label>备注:</label>
                        </div>
                        <textarea rows="6" class="col-10" ng-model="beans.description" maxlength="2000"></textarea>
                    </div>
                    <div class="row" style="margin-top: 5px;">

                        <div class="form-label col-1-half">
                            <label>状态:</label>
                        </div>
                        <span class="col-2-half" ng-cloak>{{foo.deleted?'禁用':'启用'}}</span>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
</body>

<script type="text/javascript" src="<%=contextPath%>/app/base/resource/resource.js"></script>
<script type="text/javascript" src="<%=contextPath%>/app/base/resource/resource_edit.js"></script>
</html>