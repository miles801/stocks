<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
    <%
    String contextPath = request.getContextPath();
%>
<html lang="en">
<head>
    <title>岗位列表</title>
    <meta http-equiv="X-UA-Compatible" content="IE=EmulateIE8"/>
    <link rel="stylesheet" type="text/css" href="<%=contextPath%>/vendor/bootstrap-v3.0/css/bootstrap.min.css">
    <link rel="stylesheet" type="text/css" href="<%=contextPath%>/style/standard/css/eccrm-common-new.css">
    <link rel="stylesheet" type="text/css" href="<%=contextPath%>/vendor/angular-motion-v0.3.2/angular-motion.css">
    <link rel="stylesheet" type="text/css" href="<%=contextPath%>/vendor/zTree/css/ztree.css">
    <script type="text/javascript" src="<%=contextPath%>/static/ycrl/javascript/jquery-all.js"></script>
    <script type="text/javascript" src="<%=contextPath%>/static/ycrl/javascript/angular-all.js"></script>
    <script type="text/javascript" src="<%=contextPath%>/static/ycrl/javascript/angular-strap-all.js"></script>
    <script type="text/javascript" src="<%=contextPath%>/static/ycrl/javascript/angular-ztree-all.js"></script>
    <script>
        window.angular.contextPathURL = '<%=contextPath%>';
    </script>
</head>
<body id="ng-app">
<div class="main" ng-app="base.position.list" ng-controller="Ctrl">
    <div class="panel panel-tree">
        <div class="tree" style="border: 1px solid #B7C1CB;width: 300px">
            <div class="tree-content">
                <ul id="positionTree" class="ztree"></ul>
            </div>
        </div>
        <div class="content " style="padding-left: 310px">
            <div class="block">
                <div class="block-header">
                    <div class="header-text">
                    </div>
                    <div class="header-button">
                        <a type="button" class="btn btn-green btn-min" ng-click="add()">
                            添加
                        </a>
                        <a type="button" class="btn btn-green btn-min" ng-if="beans.id && !beans.deleted"
                           ng-click="disable()" ng-cloak>
                            禁用
                        </a>
                        <a type="button" class="btn btn-green btn-min" ng-if="beans.id && beans.deleted"
                           ng-click="enable()" ng-cloak>
                            启用
                        </a>
                    </div>
                </div>
                <div class="block-content">
                    <div class="content-wrap">
                        <form name="form" class="form-horizontal" role="form" ng-show="beans" ng-cloak>
                            <div class="row">
                                <div class="form-label col-2">
                                    <label validate-error="form.name">名称:</label>
                                </div>
                                <input class="col-4" type="text" ng-model="beans.name" name="name"
                                       validate validate-required maxlength="20"/>
                                <div class="form-label col-1-half">
                                    <label>编号:</label>
                                </div>
                                <input class="col-4" type="text" ng-model="beans.code"
                                       placeholder="由英文字母，下划线，数字组成，不可重复" validate validate-naming maxlength="20"/>

                            </div>
                            <div class="row">
                                <div class="form-label col-2">
                                    <label>上级岗位:</label>
                                </div>
                                <div class="pr col-4">
                                    <input class="col-12" ztree-single="parentOptions" ng-model="beans.parentName"
                                           placeholder="点击选择" readonly/>
                                    <span class="add-on">
                                           <i class="icons circle_fork icon" ng-click="clearParent()" title="清除"></i>
                                    </span>
                                </div>

                                <div class="form-label col-1-half">
                                    <label>排序:</label>
                                </div>
                                <input class="col-4" type="number" ng-model="beans.sequenceNo"
                                       validate validate-int validate-max-length="4"/>
                            </div>


                            <div class="row">
                                <div class="form-label col-2">
                                    <label>备注:</label>
                                </div>
                            <textarea class="col-9-half" rows="6"
                                      validate validate-max-length="1000" ng-model="beans.description"></textarea>

                            </div>
                            <div class="row">
                                <div class="form-label col-2">
                                    <label>状态:</label>
                                </div>
                                <span class="col-4" ng-cloak>{{beans.deleted?'禁用':'启用'}}</span>
                            </div>
                            <div class="row button-row">
                                <a type="button" class="btn btn-green btn-min" ng-if="beans && !beans.id"
                                   ng-click="save()"
                                   ng-disabled="form.$invalid" ng-cloak>
                                    保存
                                </a>
                                <a type="button" class="btn btn-green btn-min" ng-if="beans && !beans.id"
                                   ng-click="save(true)" style="width: 160px;"
                                   ng-disabled="form.$invalid" ng-cloak>
                                    保存并新建
                                </a>
                                <a type="button" class="btn btn-green btn-min" ng-if="beans.id && !beans.deleted"
                                   ng-click="update()"
                                   ng-disabled="form.$invalid" ng-cloak>
                                    更新
                                </a>
                                <a type="button" class="btn btn-green btn-min"
                                   ng-click="cancel()" ng-cloak>
                                    取消
                                </a>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript" src="<%=contextPath%>/app/base/position/position.js"></script>
<script type="text/javascript" src="<%=contextPath%>/app/base/position/list/position_list.js"></script>
</body>
</html>