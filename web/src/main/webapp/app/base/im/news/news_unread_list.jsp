<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String contextPath = request.getContextPath();
%>
<!DOCTYPE html >
<html >
<head >
    <title>未读公司公告</title>
    <meta http-equiv="X-UA-Compatible" content="IE=EmulateIE8" />
    <link rel="stylesheet" type="text/css" href="<%=contextPath%>/vendor/bootstrap-v3.0/css/bootstrap.min.css" >
    <link rel="stylesheet" type="text/css" href="<%=contextPath%>/style/standard/css/eccrm-common-new.css" >
    <script type="text/javascript" src="<%=contextPath%>/static/ycrl/javascript/jquery-all.js" ></script >
    <script type="text/javascript" src="<%=contextPath%>/static/ycrl/javascript/angular-all.js" ></script >
    <script type="text/javascript" src="<%=contextPath%>/static/ycrl/javascript/angular-strap-all.js" ></script >

    <script type="text/javascript" src="<%=contextPath%>/app/base/im/news/news.js" ></script >

    <script type="text/javascript" >
        window.angular.contextPathURL = '<%=contextPath%>';
    </script >
</head >
<body >
<div class="main" ng-app="eccrm.im.news.unread" ng-controller="Ctrl" >
    <div class="block" >
        <div class="block-header" >
            <span class="header-button" >
                <button type="button" class="btn btn-green btn-min" ng-click="query();" >
                    刷新
                </button >
            </span >
        </div >
        <div class="block-content" >
            <div class="content-wrap" >
                <div class="table-responsive panel panel-table first-min" >
                    <table class="table table-striped table-hover" >
                        <thead class="table-header" >
                        <tr >
                            <td >主题</td >
                            <td >类型</td >
                            <td >开始时间</td >
                            <td >失效时间</td >
                            <td >发布人</td >
                            <td >发布时间</td >
                            <td >操作</td >
                        </tr >
                        </thead >
                        <tbody class="table-body" >
                        <tr ng-show="!beans || !beans.length" >
                            <td colspan="7" class="text-center" >没有符合条件的记录！</td >
                        </tr >

                        <tr bindonce ng-repeat="foo in beans" >
                            <td title="点击查询明细！" style="cursor: pointer;width: 30%;" >
                                <a ng-href="<%=contextPath%>/base/news/detail?id={{foo.id}}" target="_parent" bo-text="foo.title |limitTo:30" ></a >
                            </td >
                            <td bo-text="foo.categoryName" ></td >
                            <td bo-text="foo.startTime | eccrmDate" ></td >
                            <td bo-text="foo.endTime | eccrmDate" ></td >
                            <td bo-text="foo.publisherName" ></td >
                            <td bo-text="foo.publishTime | eccrmDate" ></td >
                            <td >
                                <a class="btn btn-tiny" title="收藏" ng-click="star(foo)" ng-disabled="foo.hasStar==true" >
                                    <i class="icons book" ></i >
                                </a >
                            </td >
                        </tr >
                        </tbody >
                    </table >
                </div >
            </div >
        </div >
    </div >
    <script type="text/javascript" src="<%=contextPath%>/app/base/im/news/news_unread_list.js" ></script >
</body >
</html >