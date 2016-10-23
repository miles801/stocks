(function (window, angular, $) {
    //获取模块
    var app = angular.module("home.panel", [
        'eccrm.angular',
        'eccrm.angularstrap',
        'itsm.bug.bug'
    ]);


    //
    app.controller('Ctrl', function ($scope, CommonUtils, AlertFactory, BugService) {

        // 系统风险分布
        var appBar = echarts.init(document.getElementById('appBar'));
        var riskBar = echarts.init(document.getElementById('riskBar'));
        var label = {normal: {show: true, position: 'inside'}};

        // 使用刚指定的配置项和数据显示图表。
        var appBarOption = {
            title: {
                text: '系统风险分布',
                left: '45%'
            },
            color: ['#FF0000', '#FF6600', '#FFFF00', '#92D050', '#3fbff0'],
            tooltip: {
                trigger: 'axis',
                axisPointer: {            // 坐标轴指示器，坐标轴触发有效
                    type: 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                }
            },
            legend: {
                data: ['极高', '高', '中', '低', '总'],
                bottom: true
            },
            xAxis: {
                type: 'category',
                data: []
            },
            yAxis: {
                type: 'value'
            },
            series: [
                {name: '极高', type: 'bar', label: label, data: []},
                {name: '高', type: 'bar', label: label, data: []},
                {name: '中', type: 'bar', label: label, data: []},
                {name: '低', type: 'bar', label: label, data: []},
                {name: '总', type: 'bar', label: label, data: []}
            ]
        };

        $scope.queryAppRisk = function () {
            var promise = BugService.analysisRisk(function (o) {
                var title = [];
                angular.forEach(o.data || [], function (bean) {
                    bean.total = bean.risk2 + bean.risk3 + bean.risk4 + bean.risk5;
                    title.push(bean.appName);
                    var series = appBarOption.series;
                    series[0].data.push(bean.risk5);
                    series[1].data.push(bean.risk4);
                    series[2].data.push(bean.risk3);
                    series[3].data.push(bean.risk2);
                    series[4].data.push(bean.total);
                });
                appBarOption.xAxis.data = title;
                appBar.setOption(appBarOption);
            });
            CommonUtils.loading(promise);
        };


        var riskBarOption = {
            color: ['#FF0000', '#FF6600', '#FFFF00', '#92D050', '#3fbff0'],
            title: {
                text: '高风险设备 Top10',
                left: '45%'
            },
            tooltip: {
                trigger: 'axis',
                axisPointer: {            // 坐标轴指示器，坐标轴触发有效
                    type: 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                }
            },
            legend: {
                data: ['极高', '高'],
                bottom: true
            },
            xAxis: {
                type: 'category',
                data: []
            },
            yAxis: {
                type: 'value'
            },
            series: [
                {name: '极高', type: 'bar', label: label, data: []},
                {name: '高', type: 'bar', label: label, data: []}
            ]
        };

        // 高危设备Top 10
        $scope.queryRisk = function () {
            var promise = BugService.analysisHighRiskIP(function (o) {
                var title = [];
                angular.forEach(o.data, function (bean) {
                    title.push(bean.assetName);
                    var series = riskBarOption.series;
                    series[0].data.push(bean.risk5);
                    series[1].data.push(bean.risk4);
                });
                riskBarOption.xAxis.data = title;
                riskBar.setOption(riskBarOption);
            });
            CommonUtils.loading(promise);
        };

        $scope.queryBugs = function () {
            var promise = BugService.analysisTechnology(function (o) {
                $scope.bugs = o.data || [];
            });
            CommonUtils.loading(promise);
        };

        $scope.queryBugCounts = function () {
            var bugBar = echarts.init(document.getElementById('bugBar'));
            // 指定图表的配置项和数据
            var bugBarOption = {
                title: {
                    text: '资产-漏洞数量趋势',
                    left: '45%'
                },
                tooltip: {
                    trigger: 'axis'
                },
                legend: {
                    bottom: true,
                    data: ['资产', '漏洞']
                },
                xAxis: {
                    type: 'category',
                    data: ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月']
                },
                yAxis: {
                    type: 'value'
                },
                series: [
                    {name: '资产', type: 'line', data: [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]},
                    {name: '漏洞', type: 'line', data: [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]}
                ]
            };

            // 使用刚指定的配置项和数据显示图表。
            var promise = BugService.analysisYear({year: new Date().getFullYear()}, function (o) {
                var beans = [
                    {name: '漏洞数'},
                    {name: '资产数'}
                ];
                angular.forEach(o.data || [], function (bean) {
                    var series = bugBarOption.series;
                    var key = 'm' + bean.bugMonth;
                    beans[0][key] = bean.bugCount;
                    beans[1][key] = bean.assetCount;
                    series[0].data[bean.bugMonth - 1] = bean.assetCount;
                    series[1].data[bean.bugMonth - 1] = bean.bugCount;
                });
                bugBar.setOption(bugBarOption);
            });
            CommonUtils.loading(promise);

        };

        // 漏洞排行
        $scope.queryBugs();
        // 系统风险
        $scope.queryAppRisk();
        // 设备漏洞排行
        $scope.queryRisk();
        // 资产漏洞趋势
        $scope.queryBugCounts();


        // 防止页面容器未加载完的时候图表无法正确展示的问题
        $(window).on('resize', function () {
            CommonUtils.delay(function () {
                appBar.resize();
                riskBar.resize();
                $scope.queryBugCounts();
            }, 100);
        });
    });


})(window, angular, jQuery);
