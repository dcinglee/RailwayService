var $tbody = $("#sample-table-1 tbody");
var allStation = [];

var nowdate = new Date(); 
var oneweekdate = new Date(nowdate-7*24*3600*1000);  
$("#startdate").val(beginDate(oneweekdate));
$("#enddate").val(endDate(nowdate));

function beginDate(date) {
    return endDate(date);
};

function endDate(date) {
    return date.getFullYear() + "-" + ("0" + (date.getMonth() + 1)).slice(-2) + "-" + ("0" + date.getDate()).slice(-2);
};


getAllStation().done(function () {
// 初始化table
    $("#servicePersonalFinance_table").bootstrapTable({
        'url': '../finance/findServiceProvider',
        'queryParams': function (params) {
            params["stationId"] = $("#stationSelects").val();
            params["name"] = $("#inputStation").val();
            params["beginDate"] = $("#startdate").val();
            params["endDate"] = $("#enddate").val();
            return params;
        },
        'toolbar': $('div[data-name="btnCtl"]'),
        'toolbarAlign': 'right',
        'striped': true,
        'sidePagination': 'server',
        'pagination': true,
        'pageSize': globalPageSize,
        'pageList': [],
        'clickToSelect': true,
        'singleSelect': true,
        'smartDisplay': true,
        'responseHandler': function (resb) {
            return {
                'total': resb.data.length,
                'rows': resb.data
            }
        },
        'onCheck': function () {
            $("button[data-target='#updateModal'], button[data-target='#deleteModal']").removeAttr("disabled");
        },
        'onUncheck': function () {
            $("button[data-target='#updateModal'], button[data-target='#deleteModal']").attr("disabled", 'true');
        },
        'onPageChange': function () {
            if ($("#servicePersonalFinance_table").bootstrapTable('getSelections')[0]) {
                $("#servicePersonalFinance_table").bootstrapTable('uncheckBy', {
                    field: 'serviceProviderId',
                    values: [$("#servicePersonalFinance_table").bootstrapTable('getSelections')[0].serviceProviderId]
                });
            }

        },
        'columns': [{
            'checkbox': true,
            'class': 'hide'
        },
            {
                'field': 'serviceProviderId',
                'title': 'ID',
                'align': 'center',
                'class': 'hide'
            },
            {
                'field': 'stationName',
                'title': '车站',
                'width': 120,
                'align': 'center',
                /*   'formatter': function(stationId) {
                 if( allStation != null ){
                 if( typeof(allStation.data) != 'undefined'){
                 for(var i=0; i < allStation.data.length;i++){
                 if(stationId==allStation.data[i].stationId){
                 return allStation.data[i].stationName;
                 }
                 }
                 }
                 }
                 return  "";
                 }*/

            },
            {
                'field': 'serviceProviderName',
                'title': '服务人员姓名',
                'width': 200,
                'align': 'center'

            },
            {
                'field': 'totalCost',
                'title': '总配送费(元)',
                'align': 'center',
                'formatter': function (totalCost) {
                    if (totalCost == null) {
                        return totalCost = '无';
                    }
                    return totalCost.toFixed(2);
                }

            },
            {
                'field': 'totalCount',
                'title': '总订单数',
                'width': 150,
                'align': 'center',


            },

            {
                'field': 'createDate',
                'title': '日期',
                'width': 150,
                'align': 'center',
                'sortable': true

            }
        ]
    });
});
/**
 * 服务人员查询ajax
 * @param name 请求查询的服务人员名称
 */
function financeQuery(stationId, name, beginDate, endDate) {
    if (($("#inputStation").val() == '')) {
        $('#servicePersonalFinance_table').bootstrapTable('refreshOptions', {pageNumber: 1, pageSize: globalPageSize});
    } else {
        $.ajax({
            type: "POST",
            url: "../finance/findServiceProvider",
            data: {
                "name": name,
                "stationId": stationId,
                'beginDate': beginDate,
                'endDate': endDate,
                "offset": 0,
                "limit": globalPageSize
            },
            dataType: 'json',
            //sidePagination: 'server',

            success: function (resb) {
                if (resb.data.totalElements != 0) {
                    $("#servicePersonalFinance_table").bootstrapTable('load', {
                        'total': resb.data.length,
                        'rows': resb.data
                    });
                } else {
                    $("#servicePersonalFinance_table").bootstrapTable('removeAll');
                }
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                alert(textStatus);
            }
        });
    }
    return false;
};

function exportExcel() {
    var $that = $(this),
        $inputs = $that.find("input[type=text]"),
        $stations = $(this).find('select[name="stationName"]');
    stationId = $("#stationSelects").val(),
        name = $("#inputStation").val(),
        beginDate = $("#startdate").val(),
        endDate = $("#enddate").val(),
        console.log(stationId + beginDate);
    var url = '../finance/downloadServiceProvider.action?' + 'stationId=' + stationId + '&name=' + name +
        '&beginDate=' + beginDate + '&endDate=' + endDate;
    if ($("#downloadcsv")[0]) {
        $('#downloadcsv').attr('src', url);
    } else {
        $('body').append("<iframe id=\"downloadcsv\" style=\"display:none\"></iframe>");
        $('#downloadcsv').attr('src', url);
    }

};
$("#derive").on('click', function () {
    exportExcel()
});


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

/**
 * 查询表单提交
 */
$("#stationFormId").submit(function () {
    return financeQuery($("#stationSelects").val(), $("#inputStation").val(), $("#beginDate").val(), $("#endDate").val());
});


jQuery(function ($) {
    $('#id-disable-check').on('click', function () {
        var inp = $('#form-input-readonly').get(0);
        if (inp.hasAttribute('disabled')) {
            inp.setAttribute('readonly', 'true');
            inp.removeAttribute('disabled');
            inp.value = "This text field is readonly!";
        }
        else {
            inp.setAttribute('disabled', 'disabled');
            inp.removeAttribute('readonly');
            inp.value = "This text field is disabled!";
        }
    });
    $('.date-picker').datepicker({autoclose: true}).next().on(ace.click_event, function () {
        $(this).prev().focus();
    });
});
/**
 * 新增模态框显示
 */
$("#addModal").on('show.bs.modal', function () {

    $(this).find('input[type=text]').val("");
})

/**
 * 删除模态框显示
 */
$("#deleteModal").on('show.bs.modal', function () {
    var name = $("#servicePersonalFinance_table").bootstrapTable('getSelections')[0].name,
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
        tableData = $("#servicePersonalFinance_table").bootstrapTable("getSelections")[0],
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
        $pass = $(this).find('input[type="password"]'),
        $radios = $(this).find('input[name="gender"]'),
        $status = $(this).find('select[name="status"]'),
        //$checkboxs = $(this).find('input[type="checkbox"]').prop('checked',true),
        tableData = $("#servicePersonalFinance_table").bootstrapTable("getSelections")[0],
        data = null,
        $that = $(this);

    var serviceType = [];
    // 模式选择,设置不同的data和url
    if (type === 'delete') {
        data = {
            'serviceProviderId': tableData.serviceProviderId
        };
    } else if (type === 'add') {
        var $stationId = $(this).find('select[name="stationId"]');
        var $status = $(this).find('select[name="status"]');
        $(this).find("input[name='serviceType']:checked").each(function (i, n) {
            serviceType.push(n.value);
        });
        data = {
            'name': $inputs[0].value,
            'account': $inputs[1].value,
            'identityCardNo': $inputs[2].value,
            'password': $pass.val(),
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
        $("#servicePersonalFinance_table").bootstrapTable("refresh");
    });

    // 阻止表格冒泡事件
    return false;
});
