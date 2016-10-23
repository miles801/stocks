<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String contextPath = request.getContextPath();
%>
<!DOCTYPE HTML >
<html >
<head >
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" >
    <link rel="stylesheet" type="text/css" href="<%=contextPath%>/vendor/bootstrap-v3.0/css/bootstrap.min.css" >
    <link rel="stylesheet" type="text/css" href="<%=contextPath%>/style/standard/css/eccrm-common-new.css" >
    <link rel="stylesheet" type="text/css" href="<%=contextPath%>/app/tools/note/note.css" >
    <script type="text/javascript" src="<%=contextPath%>/static/ycrl/javascript/jquery-all.js" ></script >
    <script type="text/javascript" src="<%=contextPath%>/static/ycrl/javascript/angular-all.js" ></script >
    <script type="text/javascript" src="<%=contextPath%>/static/ycrl/javascript/angular-strap-all.js" ></script >
    <script type="text/javascript" >
        window.angular.contextPathURL = "<%=contextPath%>";
    </script >
</head >

<body >
<div class="main" ng-app="eccrm.tool.note" ng-controller="NoteCtrl" style="overflow: auto;" >
    <div class="note" >
        <ul >
            <a class="addnote" href="#" title="新增便签" ng-click="addNote();" >
                <img src="<%=contextPath%>/app/tools/note/images/addnote.jpg" />
            </a >
            <li ng-repeat="note in notes" >
                <span ng-bind-template="{{note.title | substr:8}}" ></span >

                <p ng-bind-template="{{note.content | substr:100 }}" ng-click="view(note.id)" ></p >、
                <a title="删除" class="del" ng-click="removeNote($index,note.id);" style="cursor: pointer" >&times;</a >
                <%--<a href="#" class="del" title="删除" ng-click="removeNote($index,note.id);" ></a >--%>
            </li >
        </ul >
    </div >
    <%--<div class="pagectrl" ng-show="total && (start+limit)<=total" >
        <a href="#" ng-click="queryMore();" >加载更多</a >
    </div >--%>
</div >
<script type="text/javascript" src="<%=contextPath%>/app/tools/note/note.js" ></script >
</body >
</html >
