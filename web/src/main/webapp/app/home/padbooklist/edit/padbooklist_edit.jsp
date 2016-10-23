<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String contextPath = request.getContextPath();
%>
<!DOCTYPE html >
<html >
<head >
    <title>编辑便签</title>
    <meta http-equiv="X-UA-Compatible" content="IE=EmulateIE8" />
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <link rel="stylesheet" type="text/css" href="<%=contextPath%>/vendor/artDialog/artDialog.css" />
    <link rel="stylesheet" type="text/css" href="<%=contextPath%>/vendor/bootstrap-v3.0/css/bootstrap.min.css" >
    <link rel="stylesheet" type="text/css" href="<%=contextPath%>/style/standard/css/eccrm-common-new.css" >
    <link rel="stylesheet" type="text/css" href="<%=contextPath%>/vendor/angular-motion-v0.3.2/angular-motion.css" >
    <link rel="stylesheet" type="text/css" href="<%=contextPath%>/vendor/zTree/css/ztree.css" >
    <script type="text/javascript" src="<%=contextPath%>/static/ycrl/javascript/jquery-all.js" ></script >
    <script type="text/javascript" src="<%=contextPath%>/static/ycrl/javascript/angular-all.js" ></script >
    <script type="text/javascript" src="<%=contextPath%>/static/ycrl/javascript/angular-strap-all.js" ></script >

    <script type="text/javascript" src="<%=contextPath%>/app/home/padbooklist/notebook-modal.js"></script>
    <script type="text/javascript" src="<%=contextPath%>/app/home/padbooklist/padbooklist.js"></script>
    <script type="text/javascript" src="<%=contextPath%>/app/home/padbooklist/edit/padbooklist_edit.js"></script>
    <script type="text/javascript">
        window.angular.contextPathURL = '<%=contextPath%>';
    </script>
</head>
<style>
div.padbooklist {
   height: 106px;
    padding:5px;
    float:left;
    overflow:hidden;
width:180px;
margin-left:25px;
text-align:center;
margin-top:13px
}

</style>
<body>

	<div class="main" ng-app="eccrm.home.padbooklist.edit" ng-controller="PadbooklistEditController">
	 <div eccrm-alert="alert" ></div >
    <div class="list-result no-pagination" >
        <div class="block" >
            <div class="block-header">
                <div class="header-text">
                    <span class="glyphicons list"></span>
                    <span>便签</span>
                </div>
                <span class="header-button">
                        <button type="button" class="btn btn-green btn-min"
                           ng-click="add();">
                            <span class="glyphicons plus"></span>
                            增加
                        </button>
                        <button type="button" class="btn btn-green btn-min" ng-click="remove(notebooks.data);">
                        <span class="glyphicons remove"></span>
                            清空
                        </button>
                </span>
            </div>

            <div class="block-content" ng-cloak>
                <div class="content-wrap">
                    <div class="table-responsive panel panel-table">
                <div class="padbooklist" ng-repeat="foo in notebooks.data">
                    <a ng-click="edit(foo.id);" style="cursor: pointer">
                        <div class="row">
                        <div.row  ng-bind-template="{{foo.createdDatetime|date:'yyyy-MM-dd HH:mm:ss'}}"></div.row>
                    </div>
                    <div class="row">  
                       <div class="row" ><img src="app/home/padbooklist/1.jpg" /></div>
                    </div>
                    <div class="row">  
                       <div class="row" >
                       		<div style="font-size:14px;font-weight:bold;" >{{foo.title|limitTo:8}}...</div >
                       </div>
                    </div>
                    <div class="row">  
                       <div class="row"><div>{{foo.content|limitTo:8}}...</div ></div>
                    </div>
                    </a>
                </div>
                   </div>
                </div>
                </div >
            </div>
    </div >
    </div >
   </div>
	</div>
</body>

</html>