var $tbody = $("#sample-table-1 tbody");
var queryAllStations = null;
var serviceType = null;
$(document).ready(function () {
    $.axs("../railwayStation/getOnLineStation", null, function (data) {
        queryAllStations = eval(data);
    });
    $.axs("../serviceType/findAll", null, function (servtypedata) {
        serviceType = eval(servtypedata);
    });

});
// 初始化table
$("#serviceMerchant_table").bootstrapTable({
    'url': '../merchant/query',
    'queryParams': function (params) {
        params["name"] = $("#inputStation").val();
        return params;
    },
    'toolbar': $('div[data-name="btnCtl"]'),
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
            'total': resb.data.totalElements,
            'rows': resb.data.content
        }
    },
    'onCheck': function () {
        $("#showdeltemercModal,#showupdatemercModal").removeAttr("disabled");
    },
    'onUncheck': function () {
        $("#showdeltemercModal,#showupdatemercModal").attr("disabled", 'true');
    },
    'columns': [{
        'checkbox': true,
        // 'class': 'hide'
    },
        {
            'field': 'merchantId',
            'title': 'ID',
            'align': 'center',
            'class': 'hide'
        },
        {
            'field': 'imageId',
            'title': 'imageId',
            'align': 'center',
            'class': 'hide'
        },
        {
            'field': 'stationId',
            'title': '车站',
            'width': 150,
            'align': 'center',
            'formatter': function (value) {
                for (var i = 0; i < queryAllStations.data.length; i++) {
                    if (value == queryAllStations.data[i].stationId) {
                        return queryAllStations.data[i].stationName;
                    }
                }
                return "";
            }
        }, {
            'field': 'name',
            'title': '商户名称',
            'width': 120,
            'align': 'center'

        }, {
            'field': 'introduction',
            'title': '商户简介',
            'align': 'center'

        }, {
            'field': 'announcement',
            'title': '公告',
            'align': 'center'

        }, {
            'field': 'linkman',
            'title': '联系人',
            'width': 120,
            'align': 'center'

        }, {
            'field': 'phoneNo',
            'title': '联系电话',
            'align': 'center',
            'width': 120,
            'sortable': false
        }, {
            'field': 'address',
            'title': '地址',
            'align': 'center'
        },
        {
            'field': 'serviceTypeId',
            'title': '服务类型',
            'width': 150,
            'align': 'center',
            'formatter': function (value) {
                for (var i = 0; i < serviceType.data.length; i++) {
                    if (value == serviceType.data[i].typeId) {
                        return serviceType.data[i].name;
                    }
                }
                return "";
            }
        }, {
            'field': 'account',
            'title': '商户账号',
            'width': 150,
            'align': 'center'
        }, {
            'field': 'sailsInMonth',
            'title': '月销售量',
            'width': 100,
            'align': 'center'
        },
        {
            'field': 'evaluate',
            'title': '评价',
            'align': 'center',
            'width': 90,
            'formatter': function (evaluate) {
                if (evaluate == null) {
                    return evaluate = '0星';
                }
                return evaluate + '星';

            }

        },
        {
            'field': 'status',
            'title': '状态',
            'width': 90,
            'align': 'center',
            'formatter': function (status) {
                if (status == '8001') {
                    status = '营业中';
                } else if (status == '8002') {
                    status = '歇业';
                } else if (status == '8003') {
                    status = '商家休息';
                }

                return status;
            }

        },
        {
            'field': 'password',
            'title': '密码',
            'align': 'center',
            'class': 'hide'
        }
    ]
});


/**
 * 服务人员查询ajax
 * @param name 请求查询的服务人员名称
 */
function merchantQuery(name) {
    if (($("#inputStation").val() == '')) {
        $('#serviceMerchant_table').bootstrapTable('refreshOptions', {pageNumber: 1, pageSize: globalPageSize});
    } else {
        $.ajax({
            type: "POST",
            url: "../merchant/query",
            data: {
                "name": name,
                "offset": 0,
                "limit": globalPageSize
            },
            dataType: 'json',
            //sidePagination: 'server',

            success: function (resb) {
                if (resb.data.totalElements != 0) {
                    $("#serviceMerchant_table").bootstrapTable('load', {
                        'total': resb.data.totalElements,
                        'rows': resb.data.content
                    });
                } else {
                    $("#serviceMerchant_table").bootstrapTable('removeAll');
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
 * 查询表单提交
 */
$("#stationFormId").submit(function () {
    return merchantQuery($("#inputStation").val());
});

$("#addModal").on('hide.bs.modal', function () {
    /* autoRenderAuth($(this).find('div#perms'));*/
    $(this).find('img').attr("src", "");
});

$("#showmercModal").click(function () {
    $("#insetserviceTypeId").empty();
    $("#insetstationId").empty();
    for (var i = 0; i < serviceType.data.length; i++) {
        $("#insetserviceTypeId").append("<option  value='" + serviceType.data[i].typeId + "'>" + serviceType.data[i].name + "</option>");
    }
    for (var j = 0; j < queryAllStations.data.length; j++) {
        $("#insetstationId").append("<option  value='" + queryAllStations.data[j].stationId + "'>" + queryAllStations.data[j].stationName + "</option>");
    }

    $('#addModal').modal('show');

});

$("#addModal").on('show.bs.modal', function () {
    $(this).find("input[type=text]").val("");
    $(this).find("input[type=password]").val("");
})

$("#saveMerch").click(function () {
    var obj = jQuery("#addForm").validationEngine('validate', {
        promptPosition: "centerRight",
        autoPositionUpdate: true
    });
    if (!obj) {
        return false;
    }
    ;
    var $pass = $(this).find('input[name="password"]');
    var $conPass = $(this).find('input[name="confirmPassword"]');
    if ($pass.val() != $conPass.val()) {
        alert("两次输入的密码不一致，请重新输入");
        return false;
    }
    ;
    var conten = $("#addForm").serialize();
    $.axs("../merchant/add", conten, function (data) {
        $('#addModal').modal('hide');
        $("#serviceMerchant_table").bootstrapTable("refresh");
        alert(data.message);
    });
});

$(document).ready(function () {
    $("#addPageAddImg").click(function () {
        $('#bord').addClass("borderbox");
        $('#merchantImgs').css({"width": "100%", "height": "100%"});
    });
});

$("#showupdatemercModal").click(function () {
    var $that = $(this);
    $("#updateserviceTypeId").empty();
    $("#updatestationId").empty();
    for (var i = 0; i < serviceType.data.length; i++) {
        $("#updateserviceTypeId").append("<option  value='" + serviceType.data[i].typeId + "'>" + serviceType.data[i].name + "</option>");
    }
    for (var j = 0; j < queryAllStations.data.length; j++) {
        $("#updatestationId").append("<option  value='" + queryAllStations.data[j].stationId + "'>" + queryAllStations.data[j].stationName + "</option>");
    }
    var tableData = $("#serviceMerchant_table").bootstrapTable("getSelections")[0];
    var $status = $(this).find('select[name="status"]');
    var $sailsMonths = $(this).find('input[name="sailsInMonth"]');
    var $introduction = $(this).find('input[name="introduction"]');
    var $evaluates = $(this).find('input[name="evaluate"]');
    var conten = $("#updateForm input,#updateForm select").serializeArray();

    $("#inputfileDesc").hide();
    var imageId = tableData.imageId;
    if (tableData["imageId"] != null) {
        $("#inputfileDesc").show();
        $.ajax({
            url: '../common/getImageUrl',
            type: 'POST',
            data: {
                "imageId": imageId,
            },
            cache: false,
            dataType: 'json',
            success: function (resb) {
                var rs = resb;
                if (rs.code == '1') {
                    var preview = document.getElementById('merchantImg');
                    preview.src = rs.data;
                } else {
                    alert("预览失败，请重新上传");
                }

            }
        });
    }
    for (var ii = 0; ii < conten.length; ii++) {
        //	console.log("i="+conten[ii].name);
        $.each(tableData, function (key, val) {
            // console.log(conten[ii].val);
            if (key == conten[ii].name) {
                // console.log(key);
                /* if(key=="sailsInMonth"){
                 console.log("-----------"+tableData["sailsInMonth"]+"=");
                 console.log("before="+$("#update" + conten[ii].name + "").val());
                 // console.log($("#update" + conten[ii].name + "").val());
                 }*/

                $("#update" + conten[ii].name + "").val(tableData[key]);

                /* if(key=="sailsInMonth"){
                 console.log("after="+$("#update" + conten[ii].name + "").val());
                 }*/
            }
        });
    }

    $('#updateModal').modal('show');
});
$("#updateMerch").click(function () {
    var conten = $("#updateForm").serialize();
    $.axs("../merchant/update", conten, function (data) {
        $('#updateModal').modal('hide');
        $("#serviceMerchant_table").bootstrapTable("refresh");
        alert(data.message);
    });
});
$("#showdeltemercModal").click(function () {
    $('#deleteModal').modal('show');
});
$("#deleteMerch").click(function () {
    var tableData = $("#serviceMerchant_table").bootstrapTable("getSelections")[0];
    var conten = {'merchantId': tableData.merchantId};
    $.axs("../merchant/delete", conten, function (data) {
        $('#deleteModal').modal('hide');
        $("#serviceMerchant_table").bootstrapTable("refresh");
        alert(data.message);
    });
});