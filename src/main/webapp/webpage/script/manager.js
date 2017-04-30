var $tbody = $("#sample-table-1 tbody");
var allStation = [];
var allAdminRoles = [];

getAllAdminRole();
getAllStation().done(function () {
// 初始化table
    $("#manager_table").bootstrapTable({
        'url': '../admin/queryAdmin',
        'queryParams': function (params) {
            params["name"] = $("#inputStation").val();
            return params;
        },
        'toolbar': $('div[data-name="btnCtl"]'),
        // 'toolbarAlign': 'right',
        'striped': true,
        'sidePagination': 'server',
        'pagination': true,
        'sortable': true, //是否启用排序
        'sortOrder': "asc", //排序方式
        'pageSize': globalPageSize,
        'pageList': [],
        'clickToSelect': true,
        'singleSelect': true,
        'smartDisplay': true,
        'responseHandler': function (resb) {
            return {
                'total': resb.data.total,
                'rows': resb.data.rows
            }
        },
        'onCheck': function () {
            $("button[data-target='#updateModal'], button[data-target='#deleteModal']").removeAttr("disabled");
        },
        'onUncheck': function () {
            $("button[data-target='#updateModal'], button[data-target='#deleteModal']").attr("disabled", 'true');
        },
        'onPageChange': function () {
            if ($("#manager_table").bootstrapTable('getSelections')[0]) {
                $("#manager_table").bootstrapTable('uncheckBy', {
                    field: 'adminId',
                    values: [$("#manager_table").bootstrapTable('getSelections')[0].adminId]
                });
            }

        },
        'columns': [{
            'checkbox': true,

        },
            {
                'field': 'adminId',
                'title': 'ID',
                'align': 'center',
                'class': 'hide'
            },
            {
                'field': 'name',
                'title': '姓名',
                'width':120,
                'align': 'center'

            },
            {
                'field': 'account',
                'title': '账号',
                'width':120,
                'align': 'center'

            },
            {
                'field': 'belongId',
                'title': '所属车站',
                'width':150,
                'align': 'center',
                'formatter': function (belongId) {
                    if (allStation != null) {
                        if (typeof(allStation.data) != 'undefined') {
                            for (var i = 0; i < allStation.data.length; i++) {
                                if (belongId == allStation.data[i].stationId) {
                                    return allStation.data[i].stationName;
                                }
                            }
                        }
                    }
                    return "";
                }

            },
            {
                'field': 'roles',
                'title': '角色',
                'align': 'center',
                'formatter': function (roles) {
                    var text = "";
                    $.each(roles, function (i, value) {
                        if (i > 0) {
                            text = text + ",";
                        }
                        text = text + value.name;
                    });
                    return text;
                }
            },
            {
                'field': 'adminState',
                'title': '状态',
                'width': 100,
                'align': 'center',
                'formatter': function (adminState) {
                    if (adminState == '3001') {
                        adminState = '正常';
                    } else if (adminState == '3002') {
                        adminState = '锁定';
                    }
                    ;
                    return adminState === null ? '未知' : adminState;
                }

            },
            {
                'field': 'gender',
                'title': '性别',
                'width':100,
                'align': 'center',
                'formatter': function (gender) {
                    if (gender == '1001') {
                        gender = '男';
                    } else if (gender == '1002') {
                        gender = '女';
                    }
                    ;
                    return gender === null ? '保密' : gender;
                }
            },
            {
                'field': 'createDate',
                'title': '新增日期',
                'width':150,
                'align': 'center',
                'sortable': true

            }
        ]
    });
});

//自定义参数
/*function postQueryParams(params) {
 params.cname = $("#inputStation").val();
 return params;
 }*/
/**
 * 管理人员查询ajax
 * @param name 请求查询的服务人员
 */
function adminQuery(name) {
	if(($("#inputStation").val()== '')){
		$('#manager_table').bootstrapTable('refreshOptions',{pageNumber:1,pageSize:globalPageSize});
	}else{
    $.ajax({
        type: "POST",
        url: "../admin/queryAdmin",
        data: {
            'name': name,
            'offset': 0,
            'limit':globalPageSize
        },
        dataType: 'json',
        'sidePagination': 'server',

        success: function (resb) {
            if (resb.data.totalElements != 0) {
                $("#manager_table").bootstrapTable('load', {'total': resb.data.total, 'rows': resb.data.rows});
            } else {
                $("#manager_table").bootstrapTable('removeAll');
            }
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            alert(textStatus);
        }
    });
	}
    return false;
}

/**
 * 角色操作(新增，更新，删除)
 * @param action [add/update/remove]
 * @param data
 *               [add/update 数据结构 {name: string, description: string, listAuthorityId: array} ]
 *               [delete 数据结构 {adminId: int}]
 * @param async/scync [default async]
 */
function adminManagement(action, data, async) {
    var url = null;
    (async === false) ? async = false : async = true;
    if (action === 'addAdmin') {
        url = '../admin/addAdmin';
    } else if (action === 'updateAdmin') {
        url = '../admin/updateAdmin';
    } else {
        url = '../admin/deleteAdmin';
    }
    ;
    return $.ajax({
        type: "POST",
        url: url,
        data: data,
        dataType: 'json',
        async: async,
        success: function (resb) {
            alert(resb.message);
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            // console.log(XMLHttpRequest, textStatus, errorThrown);
            alert(textStatus);
        }
    });
}

/*
 * 查询所有角色
 */
function getAllAdminRole() {
    var $that = $(this);
    return $.ajax({
        type: "POST",
        url: '../role/query',
        error: function (request) {
            alert(request);
        },
        success: function (data) {
            var rs = eval("(" + data + ")");
            if (rs.success) {
                for (var i = 0; i < rs.data.length; i++) {
                    $(".roleCheckBoxs").append("<input type='checkbox' name='adminRoles' value='" + rs.data[i].roleId + "'><span style='vertical-align:top;margin-right: 5px;margin-left:5px;'>" + rs.data[i].name + "</span></input>");
                    //$that.find("input[type=checkbox][name='adminRoles']").prop("checked",true);
                    $("input[type=checkbox][name='adminRoles']").get(0).checked = true;

                }
                ;
                allAdminRoles = rs;
            }
            ;
        }
    });
};

/*
 * 查询所有车站
 */

function getAllStation() {
    return $.ajax({
        type: "POST",
        url: '../railwayStation/queryStationsByUser',
        error: function (request) {
            alert(request);
        },
        success: function (data) {
            var rs = eval("(" + data + ")");
            if (rs.success) {
                if (rs.data.length > 1) {
                    $(".selectAllStation").append("<option value=''>所有</option>");
                }
                for (var i = 0; i < rs.data.length; i++) {
                    $(".selectAllStation").append("<option value=" + rs.data[i].stationId + ">" + rs.data[i].stationName + "</option>");
                }
                ;
                allStation = rs;
                //	authorityQuery();
            }
            ;
        }
    });
};

/**
 * 查询表单提交
 */
$("#stationFormId").submit(function () {
    return adminQuery($("#inputStation").val());
});

/**
 * 新增模态框显示
 */
$("#addModal").on('show.bs.modal', function () {
    $(this).find('input[type=password]').val("");
    $(this).find('input[type=text]').val("");
});

/**
 * 删除模态框显示
 */
$("#deleteModal").on('show.bs.modal', function () {
    var name = $("#manager_table").bootstrapTable('getSelections')[0].name,
        html = '你确定要删除管理人员: <span>' + name + '</span>?';
    $(this).find('div.modal-body').empty().append(html);
});

/**
 * 更新模态框显示
 */
$("#updateModal").on('show.bs.modal', function () {
    var $that = $(this),
        $inputs = $that.find("input[type=text]"),
        $belongId = $(this).find('select[name="belongId"]');
    tableData = $("#manager_table").bootstrapTable("getSelections")[0],
        roleAuth = {};

    var $belongId = $(this).find('select[name="belongId"]');
    var $adminState = $(this).find('select[name="$adminState"]');
    // 设置ID,姓名,账号,电话,年龄

    $inputs[0].value = tableData.adminId;
    $inputs[1].value = tableData.name;
    $inputs[2].value = tableData.account;
    $belongId.val(tableData.belongId);
    $adminState.val(tableData.adminState);
    //   $inputs[5].value = tableData.gender;


    $that.find("[name='adminRoles']").prop("checked", false);
    $.each(tableData.roles, function (i, value) {
        $that.find("input[name='adminRoles'][value='" + value.roleId + "']").prop('checked', true);
    });
    //$inputs[5].value = tableData.gender.value;
    //$that.find("input[type=radio]").removeAttr("checked");
    //$that.find("input[type=radio]").attr("checked",false);
    $that.find("input[type=radio]:checked").prop("checked", false);
    if (tableData.gender != null) {
        $that.find("input[type=radio][value='" + tableData.gender + "']").prop("checked", true);
    }
    ;
    $('#updateModal').modal('hide');

});

/**
 * 新增,更新,删除角色表单提交
 */
$("#addForm,#updateForm,#deleteForm").submit(function () {
    var type = $(this).data("type"),
        $modal = $("#" + type + "Modal"),
        $inputs = $(this).find('input[type="text"]'),
        $pass = $(this).find('input[name="password"]'),
        $conPass = $(this).find('input[name="confirmPassword"]'),
        $radios = $(this).find('input[name="gender"]'),
        $checkbox = $(this).find('input[type="checkbox"]'),

        //  $inputps = $(this).find('input[type="passowrd"]'),
        tableData = $("#manager_table").bootstrapTable("getSelections")[0],
        data = null,
        $that = $(this);
    var adminRoles = [];
    // 模式选择,设置不同的data和url
    if (type === 'deleteAdmin') {
        data = {
            'adminId': tableData.adminId
        };
    } else if (type === 'addAdmin') {
        var obj = jQuery("#addForm").validationEngine('validate', {
            promptPosition: "centerRight",
            autoPositionUpdate: true
        });
        if (!obj) {
            return false;
        }
        ;
        if ($pass.val().trim() != $conPass.val().trim()) {
            alert("两次输入的密码不一致，请重新输入");
            return false;
        }
        ;
        var $belongId = $(this).find('select[name="belongId"]');
        var $adminStates = $(this).find('select[name="adminState"]');
        var $radioss = $(this).find('input[name="gender"]');
        var $checks = $(this).find('input[name="adminRoles"]');
        $(this).find("input[name='adminRoles']:checked").each(function (i, n) {
            adminRoles.push(n.value);
        });
        data = {
            'name': $inputs[0].value.trim(),
            'password': $pass.val().trim(),
            'account': $inputs[1].value,
            'belongId': $belongId.val(),
            //'phoneNo':$inputs[3].value,
            //'age':$inputs[4].value,
            'roleId': adminRoles,
            'gender': $('input:radio[name="gender"]:checked').val(),
            'adminState': $adminStates.val()

        };
    } else if (type === 'updateAdmin') {
        var $that = $(this);
        var $belongId = $(this).find('select[name="belongId"]');
        var $adminStates = $(this).find('select[name="adminState"]');
        var $radios = $that.find('input[name="gender"]');
        $(this).find("input[name='adminRoles']:checked").each(function (i, n) {
            adminRoles.push(n.value);
        });
        data = {
            'adminId': $inputs[0].value,
            'name': $inputs[1].value.trim(),
            'account': $inputs[2].value.trim(),
            'belongId': $belongId.val().trim(),
            'roleId': adminRoles,
            'gender': $('input:radio[name="gender"]:checked').val(),
            'adminState': $adminStates.val()
            //'gender':$("input[name='gender'][checked]").val()
        };
        data['adminId'] = tableData.adminId;
    }
    ;
    $('#addModal').modal('hide');
    $('#updateModal').modal('hide');
    $('#deleteModal').modal('hide');
    //$modal.modal('hide');
    // 完成ajax后刷新表格数据
    adminManagement(type, data).done(function () {
        $("#manager_table").bootstrapTable("refresh");
    });

    // 阻止表格冒泡事件
    return false;
});
