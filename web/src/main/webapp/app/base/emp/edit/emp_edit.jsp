<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String contextPath = request.getContextPath();
%>
<!DOCTYPE html>
<html lang="en">

<head>
    <title>编辑员工</title>
    <meta content="text/html" charset="utf-8">
    <link rel="stylesheet" type="text/css" href="<%=contextPath%>/vendor/bootstrap-v3.0/css/bootstrap.min.css">
    <link rel="stylesheet" type="text/css" href="<%=contextPath%>/style/standard/css/eccrm-common-new.css">
    <link rel="stylesheet" type="text/css" href="<%=contextPath%>/vendor/zTree/css/ztree.css">
    <script type="text/javascript" src="<%=contextPath%>/static/ycrl/javascript/jquery-all.js"></script>
    <script type="text/javascript" src="<%=contextPath%>/static/ycrl/javascript/angular-all.js"></script>
    <script type="text/javascript" src="<%=contextPath%>/static/ycrl/javascript/angular-strap-all.js"></script>
    <script type="text/javascript" src="<%=contextPath%>/static/ycrl/javascript/angular-ztree-all.js"></script>
    <script type="text/javascript" src="<%=contextPath%>/static/ycrl/javascript/angular-upload.js"></script>
    <script type="text/javascript" src="<%=contextPath%>/vendor/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript" src="<%=contextPath%>/app/base/org/org.js"></script>
    <script type="text/javascript">
        window.angular.contextPathURL = "<%=contextPath%>";
    </script>
</head>
<body>
<div class="main" ng-app="base.emp.edit" ng-controller="Ctrl">
    <div class="block">
        <div class="block-header">
                <span class="header-text">
                    <span class="glyphicons info-sign"></span>
                </span>
                <span class="header-button">
                    <c:if test="${pageType eq 'add'}">
                    <button type="button" class="btn btn-green btn-min" ng-click="save()" ng-disabled="form.$invalid">
                        <span class="glyphicons disk_save"></span> 保存
                    </button>
                        <button type="button" class="btn btn-green btn-min" ng-click="save(true)"
                                ng-disabled="form.$invalid">
                            <span class="glyphicons disk_open"></span> 保存并新建
                        </button>
                    </c:if>
                    <c:if test="${pageType eq 'modify'}">
                    <button type="button" class="btn btn-green btn-min" ng-click="update()" ng-disabled="form.$invalid">
                        <span class="glyphicons claw_hammer"></span> 更新
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
                            <label>姓名:</label>
                        </div>
                        <input class="col-2-half" type="text" ng-model="beans.name" validate validate-required
                               placeholder="真实姓名"
                               maxlength="20"/>
                        <div class="form-label col-1-half">
                            <label>登录名:</label>
                        </div>
                        <input class="col-2-half" type="text" ng-model="beans.loginName" validate validate-required
                               maxlength="20" placeholder="字母和数字组成，用于系统登录..." ng-disabled="beans.id"/>
                        <div class="form-label col-1-half">
                            <label>拼音:</label>
                        </div>
                        <input class="col-2-half" type="text" ng-model="beans.pinyin" maxlength="20"
                               placeholder="如果为空则由系统自动生成"/>
                    </div>
                    <div class="row">
                        <div class="form-label col-1-half">
                            <label>性别:</label>
                        </div>
                        <select ng-model="beans.sex" class="col-2-half"
                                ng-options="foo.value as foo.name for foo in sex"></select>
                        <div class="form-label col-1-half">
                            <label>电话号码:</label>
                        </div>
                        <input class="col-2-half" type="text" ng-model="beans.mobile" maxlength="20"
                               validate validate-int validate-msg="手机号码格式不正确"/>
                        <div class="form-label col-1-half">
                            <label>Email:</label>
                        </div>
                        <input class="col-2-half" type="text" ng-model="beans.email" maxlength="100" validate
                               validate-email/>
                    </div>
                    <div class="row">
                        <div class="form-label col-1-half">
                            <label>所属机构:</label>
                        </div>
                        <div class="col-2-half">
                            <input class="col-12" type="text" ng-model="beans.orgName" validate validate-required
                                   readonly ztree-single="orgTree"/>
                            <span class="add-on"><i class="icons icon cp fork" ng-click="clearOrg();"
                                                    title="清除"></i></span>
                        </div>
                        <div class="form-label col-1-half">
                            <label>所属岗位:</label>
                        </div>
                        <div class="col-2-half">
                            <c:if test="${sessionScope.get('OP_EMP_POSITION_MODIFY') eq true}">
                                <input class="col-12" type="text" ng-model="beans.roleNames" validate validate-required
                                       readonly ng-click="pickPosition();" placeholder="点击选择岗位（可多选）"/>
                                <span class="add-on">
                                    <i class="icons icon cp fork" ng-click="clearPosition();" title="清除"></i>
                                </span>
                            </c:if>
                            <c:if test="${sessionScope.get('OP_EMP_POSITION_MODIFY') eq null}">
                                <input class="col-12" type="text" ng-model="beans.roleNames" validate validate-required
                                       disabled/>
                            </c:if>
                        </div>
                    </div>
                    <div class="row">
                        <div class="form-label col-1-half">
                            <label>账号状态:</label>
                        </div>
                        <div class="col" ng-cloak style="margin-top: 4px;">{{beans.lockedName}}</div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
</body>
<script type="text/javascript" src="<%=contextPath%>/app/base/emp/emp.js"></script>
<script type="text/javascript" src="<%=contextPath%>/app/base/position/position.js"></script>
<script type="text/javascript" src="<%=contextPath%>/app/base/emp/edit/emp_edit.js"></script>
</html>