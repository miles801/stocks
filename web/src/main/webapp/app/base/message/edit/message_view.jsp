<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String contextPath = request.getContextPath();
%>
<!DOCTYPE html>
<html lang="en">

<head>
    <title>消息阅览</title>
    <meta content="text/html" charset="utf-8">
    <link rel="stylesheet" type="text/css" href="<%=contextPath%>/vendor/bootstrap-v3.0/css/bootstrap.min.css">
    <link rel="stylesheet" type="text/css" href="<%=contextPath%>/style/standard/css/eccrm-common-new.css">
</head>
<body>
<div class="main">
    <div>
        <h3 class="text-center" style="margin: 0;padding: 50px 20px 20px">${data.title}</h3>
        <div style="padding: 0 30px;height: 20px;">
            <span style="float: left;margin-left: 20px;font-size: 14px;font-weight: 700;">${data.typeName}</span>
            <span style="float: right;margin-right: 20px;color: #C2BBBB;">${data.sendTime}</span>
            <span style="float: right;margin-right: 20px;font-weight: 700">${data.senderName}</span>
        </div>
        <div class="ycrl split" style="margin-bottom: 0;"></div>
    </div>
    <div style="height:100%;width: 1100px;margin: 0 auto;padding: 0 20px;border:1px solid #ddd;border-top:0;border-bottom: 0;">
        <div id="content" style="overflow: auto;">${data.content}</div>
    </div>
</div>
</body>
</html>