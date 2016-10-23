/**
 * Created by shenbb on 2014-03-05 18:22:04.
 */
(function (window, angular, $) {
    var app = angular.module('eccrm.home.padbooklist.edit', ['eccrm.home.padbooklist'
        ,'eccrm.home.modal','eccrm.angular', 'eccrm.angularstrap']);
    app.controller('PadbooklistEditController', function ($scope, $window, NoteBookService,NoteBookModal,AlertFactory,ModalFactory) {
    	// //删除
        $scope.remove = function (_data) {
            //_data是页面显示的所有的便签的数据，取出所有数据的id进行组装然后传到后台进行删除
        	// 或者取出userId进行批量删除
        	var ids = "";
        	for(var i =0 ;i<_data.length;i++){
        		ids+= _data[i].id+",";
        	}
//        	var userId ="";//根據用戶刪除便籤
//        	if(_data.length>0 && _data[0]!=null){
//        		userId = _data.createdUserId;
//        	}

            ModalFactory.remove($scope, function (data) {
                NoteBookService.deleteByIds({ids:ids}, function (data) {
                if (data&&data.success) {
                    //后台删除成功后从前台删除数组中的元素
                    AlertFactory.success($scope, null, '清除数据成功!');

                } else {
                	 AlertFactory.error($scope, '[' + (data && data.error || data.fail || '') + ']', '清除数据失败!');
            }
                $scope.query();
            });
        	});
        }
    	// 添加
    	 $scope.add = function () {
    		 NoteBookModal.add({scope: $scope});
         }
    	 // 编辑
    	 $scope.edit=function(id){
    		 NoteBookModal.modify({scope:$scope,id:id});
    	 }

        var defaults = {
            orderBy: 'createdDatetime',
            reverse: 'false'
        };
        $scope.reset = function () {
            $scope.condition = angular.extend({}, defaults);
        }
        $scope.reset();
        //初始化分页信息
        $scope.pager = {
            fetch: function () {
                var param = angular.extend({}, $scope.condition, {start: this.start, limit: this.limit});
                $scope.notebooks = NoteBookService.pageQuery(param);
                ;
                AlertFactory.handle($scope, $scope.notebooks);
                return $scope.notebooks;
            }
        }

        $scope.query = function () {
            ($scope.pager.query || $scope.pager.fetch)();
        }
        $scope.query();
        var destroy = $scope.$watch('condition', function (value, oldvalue) {
            if (value === oldvalue) return;
            Debounce.delay($scope.query, 400);
        }, true);

    });
})(window, angular, jQuery);
