/**
 * 数据库列表
 * Created by Michael .
 */
(function (window, angular, $) {
    var app = angular.module('stock.db.dB.calculate', [
        'eccrm.angular',
        'eccrm.angularstrap',
        'stock.db.fnDB',
        'stock.db.dB'
    ]);
    app.controller('Ctrl', function ($scope, $q, CommonUtils, AlertFactory, ModalFactory, DBService, DBParam, FnDBService) {

        $scope.height = $('body').height() - 40;
        $scope.condition = {
            type    : '1',
            days    : 10,
            fnDateGe: moment().add(-5, 'y').format('YYYY-MM-DD'),
            fnDateLt: moment().format('YYYY-MM-DD')
        };

        var char1 = echarts.init(document.getElementById('char1'));
        var charOption = {
            title  : {
                text: '集团数分布'
            },
            tooltip: {
                trigger    : 'axis',
                axisPointer: {            // 坐标轴指示器，坐标轴触发有效
                    type: 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                }
            },
            xAxis  : {
                type: 'category',
                data: []
            },
            yAxis  : {
                type: 'value'
            },
            series : [
                {name: '集团数', type: 'bar', data: [0], label: {normal: {show: true, position: 'inside'}}}
            ]
        };

        $scope.groupCount = 0;    // 集团数

        // 参数：类型
        $scope.types = [{name: '全部'}];
        DBParam.type(function (o) {
            $scope.types.push.apply($scope.types, o);
        });

        // 查询数据
        $scope.query = function () {
            if ($scope.form.$invalid) {
                return;
            }
            $scope.load({});
            var promise = DBService.query($scope.condition, function (o) {
                var xAxis = [];     // x横坐标，值为时间
                var series = [];    // 图表的具体数据
                $scope.dates = o.data || [];
                var promise = [];
                angular.forEach($scope.dates, function (d) {
                    var x = moment(d.dbDate).format('YYYY-MM-DD');
                    xAxis.push(x);
                    var condition = angular.extend({originDate: d.dbDate}, $scope.condition);
                    // 查询每一个日期计算出来的Fn日期集合
                    var defer = FnDBService.query(condition, function (foo) {
                        d.data = foo.data || [];
                        // 计算集团数
                        var date = null;
                        var count = 0;
                        angular.forEach(d.data || [], function (tmp) {
                            var tmpTime = moment(tmp.fnDate);
                            if (date != null) {
                                if (tmpTime.diff(date, 'days', true) <= $scope.condition.days) {
                                    count++;
                                }
                            }
                            date = tmpTime;
                        });
                        series.push(count);
                        d.count = count;
                    });
                    promise.push(defer);
                });
                $q.all(promise).then(function () {
                    CommonUtils.delay(function () {
                        // 这里已经获取到所有的
                        charOption.xAxis.data = xAxis;
                        charOption.series[0].data = series;
                        char1.setOption(charOption);
                    }, 300);
                });
            });
            CommonUtils.loading(promise);
        };

        $scope.checked = null;
        $scope.load = function (foo) {
            $scope.checked = foo.id;        // 用于区分是哪一个对象被选择
            $scope.groupCount = foo.count;  // 集团数
            $scope.beans1 = foo.data;       // Fn数据列表
        };

    });
})(window, angular, jQuery);