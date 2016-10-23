/**
 * Created by miles on 13-11-25.
 */
(function (angular) {
    var app = angular.module("eccrm.accredit.menu", [
        'ngResource',
        'eccrm.angular'
    ]);
    // 授权菜单
    app.service('AccreditMenu', function ($resource, CommonUtils) {
        return $resource(CommonUtils.contextPathURL("auth/accreditMenu/:method"), {}, {
            save: {method: 'POST', params: {method: 'save', deptId: '@deptId'}, isArray: false},//保存

            // 批量删除/注销
            // 返回：{success:true}
            deleteByIds: {method: 'DELETE', params: {method: 'delete', ids: '@ids'}, isArray: false},

            // 根据ID查询对象的信息
            // 返回{data:{}}
            get: {method: 'GET', params: {method: 'get', id: '@id'}, isArray: false},//根据id查询对象

            // 查询指定部门/岗位下的所有权限
            // 返回：{data:[{}]},data中每一个元素都是一个菜单对象
            queryByDept: {method: 'GET', params: {method: 'queryByDept', deptId: '@deptId'}, isArray: false},

            // 查询指定部门/岗位下的所有权限
            // 返回：{data:[]},data中的每一个元素都是一个菜单的id
            queryIdsByDept: {method: 'GET', params: {method: 'queryIdsByDept', deptId: '@deptId'}, isArray: false},

            // 查询所有有效的功能菜单（包括界面元素等），并组装成树
            // 授权用
            // 返回：{data:[]}
            queryValid: {method: 'GET', params: {method: 'queryValid'}, isArray: false}
        })
    });

    // 授权操作
    app.service('AccreditFunc', function ($resource, CommonUtils) {
        return $resource(CommonUtils.contextPathURL("auth/accreditFunc/:method"), {}, {
            // 给部门进行授权
            accreditToDept: {method: 'POST', params: {method: 'accreditToDept', deptId: '@deptId'}, isArray: false},//保存

            // 查询指定部门/岗位下的所有权限
            // 返回：{data:[{}]},data中每一个元素都是一个菜单对象
            queryResourceCodeByDept: {
                method: 'GET',
                params: {method: 'queryResourceCodeByDept', deptId: '@deptId'},
                isArray: false
            },

            // 查询指定部门/岗位下的所有权限
            // 返回：{data:[]},data中的每一个元素都是一个菜单的id
            queryIdsByDept: {method: 'GET', params: {method: 'queryIdsByDept', deptId: '@deptId'}, isArray: false}

        })
    });

    // 授权数据
    app.service('AccreditData', function ($resource, CommonUtils) {
        return $resource(CommonUtils.contextPathURL("auth/accreditData/:method"), {}, {
            accreditToDept: {method: 'POST', params: {method: 'accreditToDept'}, isArray: false},

            // 查询指定部门/岗位下的所有权限
            // 返回：{data:[{}]},data中每一个元素都是一个菜单对象
            queryResourceCodeByDept: {
                method: 'GET',
                params: {method: 'queryResourceCodeByDept', deptId: '@deptId'},
                isArray: false
            },

            // 查询指定岗位被授予的某个数据权限的明细
            // 返回：{data:{}}
            // 参数deptId（必须）<string>：岗位id
            // 参数resourceCode(必须)<string>：资源（数据权限）编号
            queryAccreditResource: {
                method: 'GET',
                params: {method: 'queryAccreditResource', deptId: '@deptId', resourceCode: '@resourceCode'},
                isArray: false
            }

        })
    });
})(angular);
