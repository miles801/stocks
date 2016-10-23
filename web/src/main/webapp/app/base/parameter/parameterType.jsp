<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String contextPath = request.getContextPath();
%>
<!DOCTYPE html>
<html lang="en" >
<title >参数类型</title >
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE8" />
<link rel="stylesheet" type="text/css" href="<%=contextPath%>/vendor/artDialog/artDialog.css" />
<link rel="stylesheet" type="text/css" href="<%=contextPath%>/vendor/bootstrap-v3.0/css/bootstrap.min.css" >
<link rel="stylesheet" type="text/css" href="<%=contextPath%>/style/standard/css/eccrm-common-new.css" >
<link rel="stylesheet" type="text/css" href="<%=contextPath%>/vendor/zTree/css/ztree.css" >
<script type="text/javascript" src="<%=contextPath%>/static/ycrl/javascript/jquery-all.js" ></script >
<script type="text/javascript" src="<%=contextPath%>/static/ycrl/javascript/angular-all.js" ></script >
<script type="text/javascript" src="<%=contextPath%>/static/ycrl/javascript/angular-strap-all.js" ></script >
<script type="text/javascript" src="<%=contextPath%>/static/ycrl/javascript/angular-ztree-all.js" ></script >
<script >
    window.angular.contextPathURL = '<%=contextPath%>';
</script >

</head >

<body >
<div class="main" ng-app="base.parameter.type" ng-controller="ParameterTypeCtrl"
     style="height: 100%!important; ;">
    <input type="hidden" id="contextPath" value="<%=contextPath%>/" />

    <div eccrm-route="routeOptions" style="height: 100%;" ></div >
</div >
</body >
<script type="text/javascript" src="<%=contextPath%>/app/base/parameter/parameter-modal.js" ></script >
<script type="text/javascript" src="<%=contextPath%>/app/base/parameter/parameterType.js" ></script >
</html >