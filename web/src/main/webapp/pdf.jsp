<%--
  Created by IntelliJ IDEA.
  User: miles
  Date: 2014/7/1
  Time: 23:49
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String path = request.getContextPath();
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <base href="<%=path%>/">
    <title>title</title>
</head>
<body marginwidth="0" marginheight="0"
      style="background-color: rgb(38,38,38);height: 100%;width: 100%;overflow: hidden;">
<embed width="100%" height="100%" name="plugin" src="attachment/view?id=${param.id}" type="application/pdf">
</body>
</html>