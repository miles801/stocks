/**
 * Created by shenbb on 2014-03-05 18:22:04.
 */
(function (angular) {
    var app = angular.module("eccrm.tool.note", [
        'ngResource',
        'eccrm.angular',
        'eccrm.angularstrap'
    ]);
    // 便签服务
    app.service('NoteService', function ($resource, CommonUtils) {
        return $resource(CommonUtils.contextPathURL('/tools/note/:method'), {}, {
            //保存
            save: {method: 'POST', params: {method: 'save'}, isArray: false},
            //更新
            update: {method: 'PUT', params: {method: 'update'}, isArray: false},
            //根据id查询便签本信息
            get: {method: 'GET', params: {method: 'get', id: '@id'}, isArray: false},

            //根据id字符串（使用逗号分隔多个值），删除对应的便签本，成功则返回{success:true}
            deleteByIds: {method: 'DELETE', params: {method: 'delete', ids: '@ids'}, isArray: false},

            //分页查询，返回{total:,data:''}
            pageQuery: {method: 'GET', params: {method: 'query', start: '@start', limit: '@limit'}, isArray: false}
        })
    });
    // 便签弹出层服务
    app.service('NoteModal', function ($modal, NoteService, CommonUtils) {
        return {
            // 打开弹出层新增便签
            // 参数（可选）：便签添加成功后的回调（接收新增的便签对象）
            add: function (callback) {
                var modal = $modal({template: CommonUtils.contextPathURL('app/tools/note/note-tpl.html')});
                var $scope = modal.$scope;
                $scope.bean = {};
                $scope.pageType = 'add';
                $scope.save = function () {
                    var result = NoteService.save($scope.bean);
                    CommonUtils.loading(result, '正在保存便签...', function (data) {
                            var id = data.data || '';
                            $scope.bean.id = id;
                            if (typeof callback === 'function') {
                                callback($scope.bean);
                            }
                            modal.hide();
                        }
                    )
                    ;
                };
            },
            // 打开弹出层更新便签
            // 参数（必须）：id 便签的id
            // 参数（可选）：callback 更新成功后的回调（接收更新后的便签对象）
            update: function (id, callback) {
                var modal = $modal({template: CommonUtils.contextPathURL('app/tools/note/note-tpl.html')});
                var $scope = modal.$scope;
                $scope.bean = {};
                $scope.pageType = 'modify';
                // 加载数据
                var load = function () {
                    var result = NoteService.get({id: id});
                    CommonUtils.loading(result, '正在加载便签数据...', function (data) {
                        data = data.data || {};
                        $scope.bean = data;
                    });
                };
                $scope.update = function () {
                    var result = NoteService.update($scope.bean);
                    CommonUtils.loading(result, '正在更新便签...', function (data) {
                            if (typeof callback === 'function') {
                                callback($scope.bean);
                            }
                            modal.hide();
                        }
                    )
                };
                load();
            },
            // 查看便签
            // 参数（必须）：id 便签id
            view: function (id) {
                var modal = $modal({
                    template: CommonUtils.contextPathURL('app/tools/note/note-tpl.html'),
                    backdrop: true
                });
                var $scope = modal.$scope;
                $scope.bean = {};
                $scope.pageType = 'view';
                // 加载数据
                var load = function () {
                    var result = NoteService.get({id: id});
                    CommonUtils.loading(result, '正在加载便签数据...', function (data) {
                        data = data.data || {};
                        $scope.bean = data;
                        $('input,textarea', '.modal .modal-body').attr('disabled', 'disabled');
                    });
                };
                load();
            }

        }
    });
    // 便签业务
    app.controller('NoteCtrl', function ($scope, NoteService, NoteModal, CommonUtils) {
        $scope.notes = [];
        $scope.addNote = function () {
            NoteModal.add(function (note) {
                $scope.notes.unshift(note);
            });
        };

        $scope.modify = function (id) {
            NoteModal.update(id, function (note) {
                for (var i = 0; i < $scope.notes.length; i++) {
                    if ($scope.notes[i].id === id) {
                        $scope.notes[i] = note;
                    }
                }
            });
        };

        $scope.removeNote = function (index, id) {
            var result = NoteService.deleteByIds({ids: id});
            CommonUtils.loading(result, '正在删除便签...', function () {
                $scope.notes.splice(index, 1);
            });
        };
        $scope.view = NoteModal.view;
        // 每次加载的条数
        $scope.start = 0;
        $scope.limit = 100;
        $scope.total = 0;
        $scope.query = function () {
            var result = NoteService.pageQuery({start: $scope.start, limit: $scope.limit}, function (data) {
                data = data || {};
                $scope.total = data.total || 0;
                $scope.notes = data.data || [];
            });
            CommonUtils.loading(result, '正在努力加载...');
        };
        // 加载更多
        $scope.queryMore = function () {
            if ($scope.total <= $scope.start) {
                return;
            }
            $scope.start = $scope.limit + 1;
            $scope.limit += 10;
            $scope.query();
        };

        $scope.query();
    });
})(angular);

