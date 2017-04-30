var $tbody = $("#sample-table-1 tbody");
var allStation = [];
getAllStation();

// 初始化table
getAllStation().done(function () {
    $("#station_table").bootstrapTable({
        'url': '../railwayStation/query',
        'toolbar': $('div[data-name="btnCtl"]'),
        'toolbarAlign': 'right',
        'queryParams': function (params) {
            params["stationName"] = $("#inputStation").val();
            params["status"] = $("#status").val();
            return params;
        },
        'striped': true,
        'pagination': true,
        'sidePagination': 'server',
        'pageSize': globalPageSize,
        'pageList': [],
        'clickToSelect': true,
        'singleSelect': true,
        'smartDisplay': true,
        'responseHandler': function (resb) {
            return {
                'total': resb.data.total,
                'rows': resb.data.rows,
            }
        },
        'onCheck': function () {
            $("button[data-target='#updateModal'], button[data-target='#deleteModal']").removeAttr("disabled");
        },
        'onUncheck': function () {
            $("button[data-target='#updateModal'], button[data-target='#deleteModal']").attr("disabled", 'true');
        },
        'onPageChange': function () {
            $("#station_table").bootstrapTable('uncheckBy', {
                field: 'stationId',
                values: [$("#station_table").bootstrapTable('getSelections')[0].stationId]
            });
        },
        'columns': [{
            'checkbox': true,
            // 'class': 'hide'
        }, {
            'field': 'stationId',
            'title': 'ID',
            'align': 'center',
            'class': 'hide'
        }, {
            'field': 'stationName',
            'title': '站名',
            'align': 'center',
            'sortable': false,
        }, {
            'field': 'stationNameAbbr',
            'title': '站名缩写',
            'align': 'center'
        }, {
            'field': 'province',
            'title': '省',
            'align': 'center'

        }, {
            'field': 'city',
            'title': '市',
            'align': 'center'
        }, {
            'field': 'cityId',
            'title': '城市编码',
            'align': 'center'

        }, {
            'field': 'status',
            'title': '状态',
            'width': 100,
            'align': 'center',
            'formatter': function (status) {
                if (status == '17001') {
                    status = '启用';
                } else if (status == '17002') {
                    status = '停用';
                }
                return status;
            }
        }, {
            'field': 'createDate',
            'title': '创建时间',
            'align': 'center'
        }, {
            'field': 'latitude',
            'title': '纬度',
            'align': 'center'
        }, {
            'field': 'longitude',
            'title': '经度',
            'align': 'center'
        }, {
            'field': 'imgUrl',
            'title': '车站示意图',
            'align': 'center',
            'formatter': function (imgUrl, rowData) {
                var imageId = rowData.imageId;
                return '<button class= "btn btn-xs btn-danger image" data-toggle = "modal" data-target = "#imgModal" data-imageId=' + imageId + ' data-src=' + imgUrl + '>查看原图</button >';

            }
        }]

    });
});

$(document).on('click', '.image', function () {
    // $("#image-box").attr('src',$(this).attr('data-src'))
    var imageId = $(this).attr('data-imageId');
    // console.log(imageId);
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
                if (rs.data == null) {
                    $("#image-box").attr('src', '../webpage/assets/images/photo1.png');
                    // margin: auto;top: 0;bottom: 0;left: 0;right: 0;position: absolute;
                    $("#image-box").css({
                        'margin': 'auto',
                        'top': '0',
                        'bottom': '0',
                        'left': '0',
                        'right': '0',
                        'position': 'absolute'
                    })
                } else {
                    // ../images/8a9b35ab5b55b8ad015b5b2100900008.jpg
                    $("#image-box").attr('src', rs.data);
                    $("#image-box").css({
                        'margin': 'auto',
                        'top': '0',
                        'bottom': '0',
                        'left': '0',
                        'right': '0',
                        'position': 'absolute'
                    })
                    var oDiv = document.getElementById("image-box");
                    oDiv.onmousedown = function (ev) {
                        var oEvent = ev;
                        var disX = oEvent.clientX - oDiv.offsetLeft;
                        var disY = oEvent.clientY - oDiv.offsetTop;
                        document.onmousemove = function (ev) {
                            oEvent = ev;
                            oDiv.style.left = oEvent.clientX - disX + "px";
                            oDiv.style.top = oEvent.clientY - disY + "px";
                        }
                        document.onmouseup = function () {
                            document.onmousemove = null;
                            document.onmouseup = null;
                        }

                    }
                }
            } else {
                alert("预览失败，请重新上传");
            }

        }
    });
});

/**
 * 角色查询ajax
 * @param stationName 请求查询的车站名
 */
function stationQuery(stationName, status) {
    if (($("#inputStation").val() == '')) {
        $('#station_table').bootstrapTable('refreshOptions', {pageNumber: 1, pageSize: globalPageSize});
    } else {

        $.ajax({
            type: "POST",
            url: "../railwayStation/query",
            data: {
                "stationName": stationName,
                "status": status,
                "offset": 0,
                "limit": globalPageSize
            },
            dataType: 'json',
            'sidePagination': 'server',
            success: function (resb) {
                //console.log(resb.data.total);
                if (resb.data.length != 0) {
                    $("#station_table").bootstrapTable('load', {
                        'total': resb.data.total,
                        'rows': resb.data.rows,
                    });
                }
                else {
                    $("#station_table").bootstrapTable('removeAll');
                }
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                alert(textStatus);
            }
        });
    }
    return false;
};


function getAllStation() {
    return $.ajax({
        type: "POST",
        url: '../railwayStation/queryStationsByUser',
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

/**
 * 角色操作(新增，更新，删除)
 * @param action [add/update/remove]
 * @param data
 *               [add/update 数据结构 {name: string, description: string, listAuthorityId: array} ]
 *               [delete 数据结构 {roleId: int}]
 * @param async/scync [default async]
 */
function stationManagement(action, data, async) {
    var url = null;
    (async === false) ? async = false : async = true;
    if (action === 'add') {
        url = '../railwayStation/add';
    } else if (action === 'update') {
        url = '../railwayStation/update';
    } else {
        url = '../railwayStation/delete';
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
            alert(textStatus);
        }
    });
}

/**
 * 查询所有权限
 * @param authority array, 所有权限
 */

/**
 * 查询当前id的最新权限
 * @param  {[int]} id [角色id]
 * @return {[array]}  [权限列表]
 */

/**
 * 查询表单提交
 */
$("#stationFormId").submit(function () {
    return stationQuery($("#inputStation").val(), $("#status").val());
});


/**
 * 新增模态框显示
 */
$("#addModal").on('show.bs.modal', function () {
    /* autoRenderAuth($(this).find('div#perms'));*/

    $(this).find('input[type=text]').val("");
})

/**
 * 删除模态框显示
 */
$("#deleteModal").on('show.bs.modal', function () {
    var name = $("#station_table").bootstrapTable('getSelections')[0].stationName,
        html = '你确定要删除车站: <span>' + name + '</span>?';
    $(this).find('div.modal-body').empty().append(html);
});

/**
 * 更新模态框显示
 */
$("#updateModal").on('show.bs.modal', function () {
    var $that = $(this),
        $inputs = $that.find("input[type=text]"),
        $sels = $that.find("select"),
        tableData = $("#station_table").bootstrapTable("getSelections")[0];

    // 设置ID、站名、站名缩写、省、市、状态、时间
    $inputs[0].value = tableData.stationId;
    $inputs[1].value = tableData.stationName;
    $inputs[2].value = tableData.stationNameAbbr;
    $inputs[3].value = tableData.province;
    $inputs[4].value = tableData.city;
    $inputs[5].value = tableData.cityId;
    $inputs[6].value = tableData.latitude;
    $inputs[7].value = tableData.longitude;
    $sels.val(tableData.status);

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
                    var preview = document.getElementById('advertiseImgs');
                    preview.src = rs.data;
                } else {
                    alert("预览失败，请重新上传");
                }

            }
        });

    }

    $('#updateModal').modal('hide');
});

/**
 * 新增,更新,删除角色表单提交
 */
$("#addForm,#updateForm,#deleteForm").submit(function () {
    var type = $(this).data("type"),
        $modal = $("#" + type + "Modal"),
        $inputs = $(this).find('input[type="text"]'),
        $sels = $(this).find('select'),
        tableData = $("#station_table").bootstrapTable("getSelections")[0],
        data = null;
    $that = $(this);

    // 模式选择,设置不同的data和url
    if (type === 'delete') {
        data = {
            'stationId': tableData.stationId
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
        data = {
            'stationName': $inputs[0].value,
            'stationNameAbbr': $inputs[1].value,
            'province': $inputs[2].value,
            'city': $inputs[3].value,
            'cityId': $inputs[4].value,
            'status': $sels.val(),
            'latitude': $inputs[5].value,
            'longitude': $inputs[6].value,
            // 'linkUrl': $("#imgUrl").val(),
            'imageId': $("#advertiseImages").val()
        };
    }

    else {
        if (type === 'update') {
            data = {
                'stationId': $inputs[0].value,
                'stationName': $inputs[1].value,
                'stationNameAbbr': $inputs[2].value,
                'province': $inputs[3].value,
                'city': $inputs[4].value,
                'cityId': $inputs[5].value,
                'latitude': $inputs[6].value,
                'longitude': $inputs[7].value,
                'status': $sels.val(),
                'imageId': $("#advertiseUpdateImages").val()
            }
//            data['stationId'] = tableData.stationId;
        }
        ;
    }
    ;

    $modal.modal('hide');
    // 完成ajax后刷新表格数据
    stationManagement(type, data).done(function () {
        $("#station_table").bootstrapTable("refresh");
        // $that[0].reset();
    });

    // 阻止表格冒泡事件
    return false;
});
