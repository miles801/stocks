<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String contextPath = request.getContextPath();
%>
<!DOCTYPE html>
<html lang="en" >
<head >
    <title >岗位授权</title >
    <meta content="text/html" charset="utf-8" >
    <meta http-equiv="X-UA-Compatible" content="IE=EmulateIE8" />
    <!--[if IE]>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" >
    <![endif]-->
    <link rel="stylesheet" type="text/css" href="<%=contextPath%>/vendor/bootstrap-v3.0/css/bootstrap.min.css" >
    <link rel="stylesheet" type="text/css" href="<%=contextPath%>/style/standard/css/eccrm-common-new.css" >
    <link rel="stylesheet" type="text/css" href="<%=contextPath%>/vendor/angular-motion-v0.3.2/angular-motion.css" >
    <link rel="stylesheet" type="text/css" href="<%=contextPath%>/vendor/zTree/css/ztree.css" >
    <script type="text/javascript" src="<%=contextPath%>/static/ycrl/javascript/jquery-all.js" ></script >
    <script type="text/javascript" src="<%=contextPath%>/static/ycrl/javascript/angular-all.js" ></script >
    <script type="text/javascript" src="<%=contextPath%>/static/ycrl/javascript/angular-strap-all.js" ></script >
    <script type="text/javascript" src="<%=contextPath%>/static/ycrl/javascript/angular-ztree-all.js" ></script >
    <script type="text/javascript" >
        window.angular.contextPathURL = "<%=contextPath%>";
    </script >
</head >
<body id="ng-app" >
<div class="main" ng-app="eccrm.accredit.position" ng-controller="AccreditPositionCtrl" >
    <input type="hidden" id="contextPath" value="<%=contextPath%>/" />

    <div class="row panel panel-tree" >
        <div class="tree" style="width: 180px;" >
            <div class="tree-content" >
                <ul id="treeDemo" class="ztree" ></ul >
            </div >
        </div >
        <div class="content" style="padding-left: 185px;" >
            <div eccrm-route="routeOptions" style="height: 100%;" ></div >
        </div >
    </div >
</div >
</div >

<script type="text/javascript" src="<%=contextPath%>/app/position/position.js" ></script >
<script type="text/javascript" src="<%=contextPath%>/app/base/resource/menu/menu.js" ></script >
<script type="text/javascript" src="<%=contextPath%>/app/base/resource/resource.js" ></script >
<script type="text/javascript" src="<%=contextPath%>/app/org/org.js" ></script >
<script type="text/javascript" src="<%=contextPath%>/app/base/accredit/menu/accreditMenu.js" ></script >
<script type="text/javascript" src="<%=contextPath%>/app/base/accredit/accreditPosition.js" ></script >
</body >

</html >