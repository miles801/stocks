/**
 * 资源
 * Created by miles on 13-11-25.
 */
(function (angular, $) {
    var app = angular.module('base.resource.edit', [
        'base.resource',
        'eccrm.angular',
        'eccrm.angularstrap',
        'eccrm.angular.ztree'
    ]);
    app.controller('Ctrl', function ($scope, CommonUtils, $timeout, AlertFactory, ResourceService, ResourceTree) {
        // 定义变量
        $scope.beans = {authorization: true};
        var pageType = $('#pageType').val();
        var id = $scope.beans.id = $('#id').val();

        var type = $('#type').val();
        $scope.type = [
            {name: '菜单', value: 'MENU'},
            {name: '页面元素', value: 'ELEMENT'},
            {name: '数据', value: 'DATA'}
        ];

        var menuTree = [];
        var dataTree = [];
        var elementTree = [];

        // 用于选择上级菜单
        $scope.ztreeOptions = ResourceTree.pickMenu(function (o) {
            $scope.beans.parentId = o.id;
            $scope.beans.parentName = o.name;
        });

        $scope.clearParent = function () {
            $scope.beans.parentId = null;
            $scope.beans.parentName = null;
        };

        $scope.typeChanged = function () {
            $scope.clearParent();
            var o = null;
            if ($scope.beans.type == 'MENU') {
                o = ResourceTree.pickMenu(function (o) {
                    $scope.beans.parentId = o.id;
                    $scope.beans.parentName = o.name;
                });
                o = ResourceTree.pickMenu(function (o) {
                    $scope.beans.parentId = o.id;
                    $scope.beans.parentName = o.name;
                });
            } else if ($scope.beans.type == 'ELEMENT') {
                o = ResourceTree.pickElement(function (o) {
                    $scope.beans.parentId = o.id;
                    $scope.beans.parentName = o.name;
                });
            } else if ($scope.beans.type == 'DATA') {
                o = ResourceTree.pickData(function (o) {
                    $scope.beans.parentId = o.id;
                    $scope.beans.parentName = o.name;
                });
            }
            $scope.ztreeOptions.reload(o);
        };

        // 附件上传
        $scope.uploadOptions = {
            labelText: '图标',
            maxFile: 1,
            btype: 'menuIcon',
            // thumb: true,
            thumbWidth: 48,
            thumbHeight: 100,
            showTable: false,
            onSuccess: function (o) {
                var id = o.id;
                $('#icon').html('<img style="height: 100px;width: 48px;" src="' + CommonUtils.contextPathURL('/attachment/temp/view?id=' + id) + '"/>');
                $scope.$apply(function () {
                    $scope.beans.attachmentIds = id;
                    $scope.beans.icon = id;
                });
            },

            onDelete: function () {
                $('#icon').html('');
                $scope.beans.attachmentIds = null;
                $scope.beans.icon = null;
            },
            bid: id,
            swfOption: {
                buttonText: '上传图标',
                fileSizeLimit: 10 * 1000 * 1000,
                fileTypeExts: "*.png;*.jpg"
            }
        };

        $scope.removePicture = function () {
            $scope.beans.icon = null;
            $scope.uploadOptions.removeAll();
            $('#icon').html('');
        };

        //定义方法
        $scope.back = CommonUtils.back;

        // 根据ID加载资源
        var load = function (id, callback) {
            var result = ResourceService.get({id: id}, function (data) {
                data = data.data;
                $scope.beans = data;
                if ($scope.beans.icon) {
                    $('#icon').html('<img style="height: 100px;width: 48px;" src="' + CommonUtils.contextPathURL('/attachment/download?id=' + $scope.beans.icon) + '"/>');
                }
                if (angular.isFunction(callback)) {
                    callback(data.data);
                }
            });
            CommonUtils.loading(result, '加载资源信息...');
        };

        // 保存
        $scope.save = function (addNew) {
            if ($scope.beans.type == 'MENU' && !$scope.beans.parentId && !$scope.beans.icon) {
                alert('请上传图标!');
                return;
            }
            var promise = ResourceService.save($scope.beans, function () {
                AlertFactory.success('保存成功!');
                CommonUtils.addTab('update');
                if (addNew) {
                    $scope.beans.name = null;
                    $scope.beans.code = null;
                    $scope.beans.sequenceNo = ($scope.beans.sequenceNo || 0) + 1;
                    $scope.beans.url = null;
                    $scope.beans.description = null;
                } else {
                    CommonUtils.delay(function () {
                        CommonUtils.back();
                    }, 2000);
                }

            });
            CommonUtils.loading(promise, '保存资源...');
        };

        // 更新
        $scope.update = function () {
            if ($scope.beans.type == 'MENU' && !$scope.beans.parentId && !$scope.beans.icon) {
                alert('请上传图标!');
                return;
            }

            var promise = ResourceService.update($scope.beans, function () {
                AlertFactory.success('更新成功!');
                CommonUtils.addTab('update');
                CommonUtils.delay(function () {
                    CommonUtils.back();
                }, 2000);
            });
            CommonUtils.loading(promise, '更新资源...');
        };

        //执行
        if (pageType == 'modify') {//更新页面
            load(id);
        } else if (pageType == 'detail') {//查看页面
            load(id, function () {
                $('input,textarea,select', 'form').attr('disabled', 'disabled');
                $('i.icons', 'form').hide();
            });
        } else if (pageType == 'add') {
            $scope.beans = {
                type: type
            };
        }
    });

})(angular, jQuery);