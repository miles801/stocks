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
            type: '1',
            days: 10
            // fnDateGe: '2011-12-1',
            // fnDateLt: '2011-12-30'
        };

        var char1 = echarts.init(document.getElementById('char1'));
        var charOption = {
            title: {
                text: '集团数分布'
            },
            tooltip: {
                trigger: 'axis',
                axisPointer: {            // 坐标轴指示器，坐标轴触发有效
                    type: 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                },
                formatter: function (params, ticket, callback) {
                    var o = params[0];
                    var str = o.name;
                    var count = o.data.value;
                    str += '<br /> 集团数：' + count;
                    if (count > 0) {
                        str += '<br/>日期对：';
                        for (var i = 0; i < o.data.dates.length; i++) {
                            str += '<br /> ' + o.data.dates[i];
                        }
                    }
                    return str;
                }
            },
            xAxis: {
                type: 'category',
                data: []
            },
            yAxis: {
                type: 'value'
            },
            series: [
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
        $scope.beans1 = []; // fn列表
        $scope.query = function () {
            $scope.beans1 = [];
            var promise = DBService.query($scope.condition, function (o) {
                $scope.dates = o.data || [];
                if ($scope.form.$invalid) {
                    return;
                }
                var xAxis = [];     // x横坐标，值为日期范围
                var series = [];    // 图表的具体数据
                var promise = [];
                angular.forEach($scope.dates, function (d) {
                    var condition = angular.extend({originDate: d.dbDate}, $scope.condition);
                    // 查询每一个日期计算出来的Fn日期集合
                    var defer = FnDBService.query(condition, function (foo) {
                        d.data = foo.data || [];
                        // fnDate
                        angular.forEach(d.data || [], function (tmp) {
                            $scope.beans1.push(tmp);
                        });
                    });
                    promise.push(defer);
                });
                $q.all(promise).then(function () {
                    CommonUtils.delay(function () {
                        // 计算集团数：从日期范围中取出日期，然后+-日期范围指标，得到日期范围
                        // 利用日期范围和计算出的fnDate进行比较，如果在这个范围内，则表示是集团数
                        var min = moment($scope.condition.fnDateGe).valueOf();
                        var max = moment($scope.condition.fnDateLt).valueOf();
                        var aDay = 86400000;
                        var date = min;
                        var range = $scope.condition.days;
                        for (; date <= max;) {
                            xAxis.push(moment(date).format('YYYYMMDD'));  // x坐标
                            var minDate = date.valueOf() - 86400000 * range;
                            var maxDate = date.valueOf() + 86400000 * range;
                            var count = 0;
                            var dates = [];
                            angular.forEach($scope.beans1 || [], function (tmp) {
                                var t = tmp.fnDate;
                                if (minDate <= t && t <= maxDate) {
                                    count++;
                                    dates.push(moment(t).format('YYYYMMDD'));
                                }
                            });
                            series.push({
                                value: count,
                                dates: dates
                            });
                            date += aDay;
                        }
                        // 这里已经获取到所有的
                        charOption.xAxis.data = xAxis;
                        charOption.series[0].data = series;
                        char1.setOption(charOption);
                    }, 300);
                });
            });
            CommonUtils.loading(promise);
        };

        $scope.query();
    });
})(window, angular, jQuery);