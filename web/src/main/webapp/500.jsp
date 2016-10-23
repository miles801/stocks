<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %>
<%@page import="java.io.PrintWriter"%>
<!DOCTYPE html>
<html lang="en" >
<head >
    <base href="<%=request.getContextPath()%>/" />
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" >
    <link rel="stylesheet" href="style/standard/css/page-error.css" />
</head >
<body >
<div class="wrappper" >
    <h2 >Internal Server Error</h2 >

    <p >服务器内部错误!</p >

    <div class="error-content">
        <% exception.printStackTrace(new PrintWriter(out)); %>
    </div >

    <p >请您将错误信息反馈给管理员，谢谢!</p >
</div >
</body >
</html >