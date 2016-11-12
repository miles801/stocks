$(function () {
    // 加载顶部，左侧条
    $('#header').slideDown(500, function () {
        $('#main').show().animate({
            width: '100%'
        }, 500);
    });
    var socket = new WebSocket('ws://' + window.location.host + '/ws');

    // 打开Socket
    socket.onopen = function (event) {

        // 发送一个初始化消息
        socket.send('我发送了一个测试消息!');

        // 监听消息
        socket.onmessage = function (event) {
            notify('新消息', {
                requireInteraction: true,
                icon: '/app/main/images/clock.jpg',
                body: event.data
            });
        };

    }
});
(function (angular, $, window) {

    var app = angular.module('eccrm.main', [
        'eccrm.angular',
        'eccrm.angularstrap',
        'base.emp'  // 员工
    ]);

    app.controller('MainController', function ($scope, $http, $timeout, $modal, CommonUtils, AlertFactory, EmpModal) {
        $scope.menus = []; // 菜单

        $scope.subMenus = [];// 子菜单


        // 收缩
        $scope.$on('collapse', function () {

        });

        // 折叠
        $scope.$on('expand', function () {

        });
        var $iframe = $('#iframe');
        var $tab = $('#tab');
        $scope.addTab = function (title, url, data, currentId) {
            $scope.currentId = currentId;
            if (!url) {
                return;
            }
            if (data && data.fullScreen == true) {
                $scope.expand();
                $tab.hide();
                $iframe.attr('src', CommonUtils.contextPathURL(url)).show();
                return;
            }
            if ($iframe.is(":visible")) {
                $iframe.hide();
            }
            if ($tab.is(":hidden")) {
                $tab.show();
            }

            CommonUtils.addTab({
                title: title,
                url: url,
                isRoot: true,
                canClose: false,
                targetObj: window
            });
        };

        // 查询权限菜单
        // 首先查询根菜单
        // 当点击根菜单时，动态加载子菜单
        CommonUtils.promise(function (defer) {
            CommonUtils.loading(defer, '加载个人权限菜单...');
            $http.get(CommonUtils.contextPathURL('/base/resource/mine/menu'))
                .success(function (data) {
                    // 包含子节点的根元素
                    data = data.data || [];
                    $scope.menus = CommonUtils.array2Tree(data, "id", "parentId", "children");
                    $scope.showHome();  // 显示首页
                    defer.resolve(data);
                })
                .error(function (data) {
                    AlertFactory.error('权限加载失败!请刷新后重试!');
                    defer.reject(data);
                });
        });


        var $colbar = $('#colbar');
        var $expand = $('#expand');
        var $fold = $('#fold');
        var $menu = $('#accordian');
        var speed = 250;
        // 收起
        $scope.fold = function () {
            $menu.animate({width: 0}, speed, function () {
                $menu.hide();
            });
            $('.content-iframe').css({'margin-left': 0, 'padding-left': 0});
            $expand.show();
            $fold.hide();
        };

        // 展开
        $scope.expand = function () {
            if ($colbar.is(":hidden")) {
                $colbar.show();
            }
            $('.content-iframe').css({'margin-left': -130, 'padding-left': 130});
            if ($menu.is(':hidden')) {
                $expand.hide();
                $fold.show();
                $menu.width(0);
                $menu.show();
                $menu.animate({width: 120}, speed);
            }
        };

        $scope.toggle = function () {
            if ($fold.is(':hidden')) {
                $scope.expand();
            } else {
                $scope.fold();
            }
        };

        // 显示首页：隐藏tab
        $scope.showHome = function () {
            $scope.fold();
            $colbar.hide();
            $iframe.attr('src', CommonUtils.contextPathURL('/app/stock/stock/stockDay/stockDay_result.jsp')).show();
        };


        // 切换显示子菜单
        $scope.showChildren = function (menu) {
            if (menu.children) {
                $iframe.hide();
                $scope.subMenus = menu.children || [];
                $scope.expand();
            }
        };
        // 便签
        $scope.showNote = function () {

            hideColbar();
            // 显示便签
            $('#iframe').show().attr('src', 'tools/note')
        };


        $scope.updatePwd = function () {
            EmpModal.updatePwd(function () {
                AlertFactory.success('密码更新成功!请重新登录!');
                CommonUtils.delay(function () {
                    window.location.href = CommonUtils.contextPathURL("/logout");
                }, 2000);
            });
        };


        // 当菜单渲染完毕后要执行的操作
        $scope.$on('ngRepeatFinish', function () {
            // 初始化菜单翻动
            //leftbar禁止双击选中
            $(".leftbar").attr("onselectstart", "return false");

            //leftBar翻动
            var $leftBarItem = $(".leftbar .LB_container > a"),
                leftbarBtnT = $(".leftbar .btnT"),
                leftbarBtnB = $(".leftbar .btnB");
            leftbarBtnT.click(function () {
                if (parseInt($leftBarItem.css("top")) < 0) {
                    $leftBarItem.animate({"top": parseInt($leftBarItem.css("top")) + 51}, 200);
                }
            });
            leftbarBtnB.click(function () {
                var leftbarItemHeight = $leftBarItem.length * 51;
                var leftbarContainerHeight = $(".leftbar .LB_container").height();
                if (parseInt(leftbarItemHeight) + parseInt($leftBarItem.css("top")) - parseInt(leftbarContainerHeight) > 0) {
                    $leftBarItem.animate({"top": parseInt($leftBarItem.css("top")) - 51}, 200);
                }
            });

            //leftBar选中切换
            $leftBarItem.click(function () {
                var obj = $(this);
                obj.siblings("a").removeClass("current");
                obj.addClass("current")
            });

            // 渲染完毕后再触发一次窗口大小改变事件
            $(document).trigger('resize');
        });
    });


    // 单击时切换状态，允许传递要被显示的（与使用当前指令）同辈元素的jquery 选择器
    app.directive('navClickSlide', function () {
        var speed = 200;
        return {
            link: function (scope, element, attr) {

                element.bind('click', function (e) {

                    var selector = attr['navClickSlide'];// 实际存放导航菜单的

                    var next = element.nextAll(); //存放下级元素的根元素
                    var elms = element.parent().siblings().find(selector + ':visible');
                    var showNext = function () {
//                        next.not(":visible") ? next.show('fast') : null; // 点击当前元素不折叠
                        next.slideToggle(speed);// 点击当前元素折叠
                    };
                    // 当前节点的兄弟节点没有有下级
                    if (next.children().length === 0) {
                        // 收起当前节点所有同级节点下被显示的元素
                        elms.length > 0 && elms.slideUp(speed);
                    } else {
                        // 当前节点的兄弟节点有下级
                        // 收起已被展开的,并展开现在的
                        elms.length > 0 ? elms.slideUp(speed, showNext) : showNext();

                        // 禁用默认事件（防止处理时导致的停顿现象）
                        e.preventDefault();
                    }
                });
            }
        }
    });

    // 当ng-repeat渲染完成后要执行的函数
    app.directive('ngRepeatFinish', ['$timeout', function ($timeout) {
        var defaultName = "ngRepeatFinish";
        return {
            restrict: 'A',
            link: function ($scope, element, attr) {
                var eventName = attr['ngRepeatFinish'];
                eventName = eventName || defaultName;
                if ($scope.$last === true) {
                    $timeout(function () {
                        $scope.$emit(eventName);
                    });
                }
            }
        }
    }]);

})(angular, $, window);

// 页面关闭/刷新时进行提示
window.onbeforeunload = function () {
    if ($.browser.msie) {// ie关闭
        // 第一个条件：点击右上角的关闭按钮
        // 第二个条件：点击页签的关闭按钮
        // 第三个条件：按下了ALT键等导致页面跳转的都视为关闭
        if (event.screenY - 20 < 0 || event.screenX > 440 || event.altKey) {// alt按键
            event.returnValue = "确定退出系统?"
        }
    } else if ($.browser.chrome) {
        event.returnValue = "确定退出系统?"
    }
};