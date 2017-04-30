var $tbody = $("#sample-table-1 tbody");
// var allService = [];
var allServiceType = [];
//getAllStation();
getAllServiceType();
// getAllService(phoneNo);
// 初始化table
getAllStation().done(function () {
    $("#servicePersonal_table").bootstrapTable({
        'url': '../serviceProvider/query',
        'queryParams': function (params) {
            params["name"] = $("#serviceName").val();
            params["phoneNo"] = $("#servicephoneNo").val();
            params["identityCardNo"] = $("#inputStation3").val();
            params["stationId"] = $("#serviceProviderSel").val();
            return params;
        },
        'toolbar': $('div[data-name="btnCtl"]'),
        'toolbarAlign': 'right',
        'striped': true,
        'sidePagination': 'server',
        'sortable': true, //是否启用排序
        'sortOrder': "asc", //排序方式
        'pagination': true,
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
            if ($("#servicePersonal_table").bootstrapTable('getSelections')[0]) {
                $("#servicePersonal_table").bootstrapTable('uncheckBy', {
                    field: 'serviceProviderId',
                    values: [$("#servicePersonal_table").bootstrapTable('getSelections')[0].serviceProviderId]
                });
            }

        },
        'columns': [
            {
                'checkbox': true,
                // 'class': 'hide'
            },
            {
                'field': 'serviceProviderId',
                'title': 'ID',
                'align': 'center',
                'class': 'hide'
            },
            {
                'field': 'name',
                'title': '姓名',
                'align': 'center'

            },
            {
                'field': 'account',
                'title': '账号',
                'align': 'center'

            },
            {
                'field': 'identityCardNo',
                'title': '身份证号',
                'align': 'center'
            },
            {
                'field': 'serviceTypes',
                'title': '服务类型',
                'align': 'center',
                'formatter': function (serviceTypes) {
                    var text = "";
                    $.each(serviceTypes, function (i, value) {
                        if (i > 0) {
                            text = text + ",";
                        }
                        text = text + value.name;
                    });
                    return text;
                }
            },
            {
                'field': 'stationId',
                'title': '车站',
                'align': 'center',
                'formatter': function (stationId) {
                    if (allStation != null) {
                        if (typeof(allStation.data) != 'undefined') {
                            for (var i = 0; i < allStation.data.length; i++) {
                                if (stationId == allStation.data[i].stationId) {
                                    return allStation.data[i].stationName;
                                }
                            }
                        }
                    }
                    return "";
                }

            },
            {
                'field': 'phoneNo',
                'title': '联系方式',
                'align': 'center'

            },
            {
                'field': 'age',
                'title': '年龄',
                'align': 'center',
                'sortable': true
            },
            {
                'field': 'gender',
                'title': '性别',
                'width': 100,
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
                'field': 'status',
                'title': '状态',
                'width': 100,
                'align': 'center',
                'formatter': function (status) {
                    if (status == '5001') {
                        status = '启用';
                    } else if (status == '5002') {
                        status = '停用';
                    }
                    ;
                    return status === null ? '未知' : status;
                }
            },
            {
                'field': 'onlineNum',
                'title': '在线人数',
                'align': 'center',
                'formatter': function (onlineNum, row) {
                    if (row.status == '5001') {
                        return onlineNum;
                    } else if (row.status == '5002') {
                        return 0;
                    }
                    ;
                    return status === null ? '未知' : status;
                }
                /*,
                 'formatter': function (value, row) {
                 debugger;
                 var num = 0;
                 $.ajax({
                 type: "POST",
                 data: {phoneNo: row.phoneNo},
                 url: '../serviceProvider/queryOnlineNum',
                 error: function (request) {
                 num = 0;
                 },
                 success: function (resb) {
                 if (resb.success) {
                 num = resb.data;
                 } else {
                 num = 0;
                 }
                 }
                 });
                 return num;
                 }*/
            }
        ]
    });
});


/**
 * 服务人员查询ajax
 * @param name 请求查询的服务人员名称
 */
function serviceQuery(name, phoneNo, identityCardNo, stationId) {
    if ((($("#serviceName").val()) && ($("#servicephoneNo").val()) && ($("#inputStation3").val())) == '') {
        $('#servicePersonal_table').bootstrapTable('refreshOptions', {pageNumber: 1, pageSize: globalPageSize});
    } else {
        $.ajax({
            type: "POST",
            url: "../serviceProvider/query",
            data: {
                "name": name,
                "phoneNo": phoneNo,
                "identityCardNo": identityCardNo,
                "stationId": stationId,
                "offset": 0,
                "limit": globalPageSize
            },
            dataType: 'json',
            //sidePagination: 'server',

            success: function (resb) {
                if (resb.data.totalElements != 0) {
                    $("#servicePersonal_table").bootstrapTable('load', {
                        'total': resb.data.total,
                        'rows': resb.data.rows
                    });
                } else {
                    $("#servicePersonal_table").bootstrapTable('removeAll');
                }
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                alert(textStatus);
            }
        });
    }
    return false;
};


/**
 * 角色操作(新增，更新，删除)
 * @param action [add/update/remove]
 * @param data
 *               [add/update 数据结构 {name: string, description: string, listAuthorityId: array} ]
 *               [delete 数据结构 {roleId: int}]
 * @param async/scync [default async]
 */
function serviceManagement(action, data, async) {
    var url = null;
    (async === false) ? async = false : async = true;
    if (action === 'add') {
        url = '../serviceProvider/add';
    } else if (action === 'update') {
        url = '../serviceProvider/update';
    } else {
        url = '../serviceProvider/delete';
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
 * 查询所有车站
 */
function getAllStation() {
    return $.ajax({
        type: "POST",
        url: '../railwayStation/getOnLineStation',
        error: function (request) {
            alert(request);
        },
        success: function (resb) {
            var rs = eval("(" + resb + ")");
            if (rs.success) {
                if (rs.data.length > 1) {
                    $(".selectAllStation").append("<option value=''>所有</option>");
                }
                ;
                for (var i = 0; i < rs.data.length; i++) {
                    $(".selectAllStation").append("<option value=" + rs.data[i].stationId + ">" + rs.data[i].stationName + "</option>");
                }
                ;
                allStation = rs;
            }
            ;
        }
    });
};

/*
 * 查询所有服务类型
 */
function getAllServiceType() {
    return $.ajax({
        type: "POST",
        url: '../serviceType/findAll',
        error: function (request) {
            alert(request);
        },
        success: function (data) {
            var rs = eval("(" + data + ")");
            if (rs.success) {
                for (var i = 0; i < rs.data.length; i++) {
                    $(".serviceTypecheckboxs").append("<input type='checkbox' name='serviceType' value='" + rs.data[i].typeId + "' ><span style='vertical-align:top;margin-right: 5px;margin-left:5px;'>" + rs.data[i].name + "</span></input>");
                }
                ;
                allServiceType = rs.data;
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
    return serviceQuery($("#serviceName").val(), $("#servicephoneNo").val(), $("#inputStation3").val(), $("#serviceProviderSel").val());
});

//$("#stationFormId").submit(function() {
//    return serviceQueryByStationId($("#serviceProviderSel").val());
//});
/*$("select").change(function(){$(this).submit()});*/
/**
 * 新增模态框显示
 */
$("#addModal").on('show.bs.modal', function () {
    /* autoRenderAuth($(this).find('div#perms'));*/
    $(this).find("input[type=password]").val("");
    $(this).find("input[type=text]").val("");
})

/**
 * 删除模态框显示
 */
$("#deleteModal").on('show.bs.modal', function () {
    var name = $("#servicePersonal_table").bootstrapTable('getSelections')[0].name,
        html = '你确定要删除服务人员: <span>' + name + '</span>?';
    $(this).find('div.modal-body').empty().append(html);
});

/**
 * 更新模态框显示
 */

$("#updateModal").on('show.bs.modal', function () {
    var $that = $(this),
        $inputs = $that.find("input[type=text]"),
        $sels = $(this).find('select'),
        $checks = $(this).find("input[type=checkbox]"),
        tableData = $("#servicePersonal_table").bootstrapTable("getSelections")[0],
        roleAuth = {};

    var $stationId = $(this).find('select[name="stationId"]');
    var $status = $(this).find('select[name="status"]');

    // 设置ID,姓名,账号,电话,年龄
    $inputs[0].value = tableData.serviceProviderId;
    $inputs[1].value = tableData.name;
    $inputs[2].value = tableData.account;
    $inputs[3].value = tableData.identityCardNo;
    $inputs[4].value = tableData.phoneNo;
    $inputs[5].value = tableData.age;
    $stationId.val(tableData.stationId);
    $status.val(tableData.status);

    //$that.find("input[name='serviceType']:checked").attr("checked",false);
    //$that.find("input[name='serviceType']:checked").removeAttr("checked");

    $that.find("[name='serviceType']").prop("checked", false);
    $.each(tableData.serviceTypes, function (i, value) {
        $that.find("input[name='serviceType'][value='" + value.typeId + "']").prop('checked', true);
    });

    $that.find("input[type=radio]:checked").prop("checked", false);
    if (tableData.gender != null) {
        $that.find("input[type=radio][value='" + tableData.gender + "']").prop("checked", true);
    }
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
        $status = $(this).find('select[name="status"]'),
        //$checkboxs = $(this).find('input[type="checkbox"]').prop('checked',true),
        tableData = $("#servicePersonal_table").bootstrapTable("getSelections")[0],
        data = null,
        $that = $(this);

    var serviceType = [];
    // 模式选择,设置不同的data和url
    if (type === 'delete') {
        data = {
            'serviceProviderId': tableData.serviceProviderId
        };
    } else if (type === 'add') {
        var obj = jQuery("#addForm").validationEngine('validate', {
            promptPosition: "centerRight",
            autoPositionUpdate: true
        });
        if (!obj) {
            return false;
        }
        ;
        //console.log(trim($pass.val()));
        //console.log(trim($conPass.val()));
        if ($pass.val().trim() != $conPass.val().trim()) {
            alert("两次输入的密码不一致，请重新输入");
            return false;
        }
        ;
        var $stationId = $(this).find('select[name="stationId"]'),
            $status = $(this).find('select[name="status"]'),
            $pass = $(this).find('input[name="password"]'),
            $conPass = $(this).find('input[name="confirmPassword"]')
        $(this).find("input[name='serviceType']:checked").each(function (i, n) {
            serviceType.push(n.value);
        });

        data = {
            'name': $inputs[0].value,
            'account': $inputs[1].value,
            'identityCardNo': $inputs[2].value,
            'password': $pass.val().trim(),
            'typeId': serviceType,
            'stationId': $stationId.val(),
            'phoneNo': $inputs[3].value,
            'age': $inputs[4].value,
            'gender': $('input:radio[name="gender"]:checked').val(),
            'status': $status.val()
        };
    } else if (type === 'update') {
        var $stationId = $(this).find('select[name="stationId"]');
        var $status = $(this).find('select[name="status"]');
        $(this).find("input[name='serviceType']:checked").each(function (i, n) {
            serviceType.push(n.value);
        });
        data = {
            'serviceProviderId': $inputs[0].value,
            'name': $inputs[1].value,
            'account': $inputs[2].value,
            'identityCardNo': $inputs[3].value,
            'typeId': serviceType,
            'stationId': $stationId.val(),
            'phoneNo': $inputs[4].value,
            'age': $inputs[5].value,
            'gender': $('input:radio[name="gender"]:checked').val(),
            'status': $status.val()
        };
        data['serviceProviderId'] = tableData.serviceProviderId;
    }
    ;


    $modal.modal('hide');
    // 完成ajax后刷新表格数据
    serviceManagement(type, data).done(function () {
        $("#servicePersonal_table").bootstrapTable("refresh");
    });

    // 阻止表格冒泡事件
    return false;
});
