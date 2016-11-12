(function (window, angular, $) {
    var app = angular.module('settle.report', [
        'eccrm.angular',
        'eccrm.angularstrap'
    ]);
    app.controller('Ctrl', function ($scope, CommonUtils, AlertFactory, ModalFactory) {
        $scope.initTab = function () {
            // 删除之前的
            $('#tab').html('');
            $(window.parent.document.body).find('ul.nav-tabs').hide();
            var data = [
                {
                    title: '6日',
                    url: 'app/stock/stock/stockDay/stockDay_result.jsp'
                },
                {
                    title: '3日',
                    url: 'app/stock/stock/stockDay/stockDay_result3.jsp'
                }
            ];
            angular.forEach(data, function (o, i) {
                CommonUtils.addTab({
                    isRoot: i == 0,
                    title: o.title,
                    canClose: false,
                    active: i == 0,
                    targetObj: window.self,
                    targetElm: '#tab',
                    url: o.url
                });
            });
        };

        $scope.initTab();
    });

})(window, angular, jQuery);