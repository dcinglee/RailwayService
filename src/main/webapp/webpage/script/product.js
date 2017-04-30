var $tbody = $("#sample-table-1 tbody");
getAllService();


// 初始化table
getAllStation().done(function () {
    $("#product_table").bootstrapTable({
        'url': '../product/queryAllProduct',
        'toolbar': $('div[data-name="btnCtl"]'),
        'toolbarAlign': 'right',
        'queryParams': function (params) {
            params["stationId"] = $("#productStationId").val();
            params["serviceType"] = $("#serviceType").val();
            params["merchantName"] = $("#merchant").val();
            params["productName"] = $("#product").val();
            params["recommend"] = $("#serviceCommend").val();
            return params;
        },
        'striped': true,
        'pagination': true,
        // 'sidePagination': 'server',
        'pageSize': globalPageSize,
        'pageList': [],
        'clickToSelect': true,
        'singleSelect': true,
        'smartDisplay': true,
        /*'responseHandler': function (resb) {
         return {
         'total': resb.data.length,
         'rows': resb.data,
         }
         },*/
        'onCheck': function () {
            $("button[data-target='#updateModal'], button[data-target='#deleteModal']").removeAttr("disabled");
        },
        'onUncheck': function () {
            $("button[data-target='#updateModal'], button[data-target='#deleteModal']").attr("disabled", 'true');
        },
        'onPageChange': function () {
            $("#product_table").bootstrapTable('uncheckBy', {
                field: 'stationId',
                values: [$("#product_table").bootstrapTable('getSelections')[0].stationId]
            });
        },
        'columns': [{
            'checkbox': true,
            //'class': 'hide'
        }, {
            'field': 'stationId',
            'title': 'stationId',
            'align': 'center',
            'class': 'hide'
        }, {
            'field': 'stationName',
            'title': '车站名称',
            'align': 'center'
        }, {
            'field': 'merchantName',
            'title': '商户名称',
            'align': 'center',
        }, {
            'field': 'productName',
            'title': '商品名称',
            'align': 'center',
        }, {
            'field': 'productPrice',
            'title': '商品售价(元)',
            'align': 'center',
            'formatter': function (productPrice) {
                if (productPrice == null) {
                    productPrice = '无';
                    return productPrice;
                }
                return productPrice.toFixed(2);
            }
        }, {
            'field': 'sales',
            'title': '商品销量',
            'align': 'center',
        }, {
            'field': 'state',
            'title': '商品状态',
            'align': 'center',
            'formatter': function (state) {
                if (state == '15001') {
                    state = '开售';
                } else if (state == '15002') {
                    state = '停售';
                }
                return state;
            }
        }, {
            'field': 'hits',
            'title': '商品点击量',
            'align': 'center',
        }, {
            'field': 'recommend',
            'title': '推荐类型',
            'align': 'center',
            'formatter': function (recommend) {
                if (recommend == '10000') {
                    recommend = '精选美食';
                } else if (recommend == '10001' || recommend == null) {
                    recommend = '未推荐';
                } else if (recommend == '10002') {
                    recommend = '热门商品';
                }
                return recommend;
            }
        }, {
            'field': 'score',
            'title': '商品评分',
            'align': 'center',
        }, {
            'field': 'typeId',
            'title': '服务类型',
            'align': 'center',
            'formatter': function (typeId) {
                if (typeId == 'service_type_charge') {
                    typeId = '充电服务';
                } else if (typeId == 'service_type_catering_trade') {
                    typeId = '点餐服务';
                } else if (typeId == 'service_type_shopping') {
                    typeId = '购物服务';
                } else if (typeId == 'service_type_buybook') {
                    typeId = '图书服务';
                }
                return typeId;
            }

        }, {
            'field': 'merchantId',
            'title': '商户id',
            'align': 'center',
            'class': 'hide'
        }, {
            'field': 'productId',
            'title': '产品id',
            'align': 'center',
            'class': 'hide'
        }]
    });
});


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
                    $("#productStationId").append("<option value=''>所有车站</option>");
                }
                ;
                for (var i = 0; i < rs.data.length; i++) {
                    $("#productStationId").append("<option value=" + rs.data[i].stationId + ">" + rs.data[i].stationName + "</option>");
                }
                ;
                allStation = rs;
            }
            ;
        }
    });
};
/*
 * 查询服务类型
 */
function getAllService() {
    return $.ajax({
        type: "POST",
        url: '../serviceType/findAll',
        error: function (request) {
            alert(request);
        },
        success: function (resb) {
            var rs = eval("(" + resb + ")");
            if (rs.success) {
                if (rs.data.length > 1) {
                    $("#serviceType").append("<option value=''>所有服务</option>");
                }
                ;
                for (var i = 0; i < rs.data.length; i++) {
                    $("#serviceType").append("<option value=" + rs.data[i].typeId + ">" + rs.data[i].name + "</option>");
                }
                ;
                allStation = rs;
            }
            ;
        }
    });
};


function productQuery(stationId, serviceType, merchantName, productName, recommend) {
    if (($("#merchantName").val() == '') && ($("#productName").val() == '')) {
        $('#product_table').bootstrapTable('refreshOptions', {pageNumber: 1, pageSize: globalPageSize});
    } else {
        $.ajax({
            type: "POST",
            url: "../product/queryAllProduct",
            data: {
                "stationId": stationId,
                'serviceType': serviceType,
                'merchantName': merchantName,
                'productName': productName,
                'recommend': recommend,
                "offset": 0,
                "limit": globalPageSize
            },
            dataType: 'json',
            success: function (resb) {
                console.log(resb);
                if (resb.data.length != 0) {

                    $("#product_table").bootstrapTable('load', resb.data);
                }
                else {
                    $("#product_table").bootstrapTable('removeAll');
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
    return productQuery($("#productStationId").val(), $("#serviceType").val(), $("#merchant").val(), $("#product").val(), $("#serviceCommend").val());
});

/**
 * 单个提交
 */
/*$("select").change(function () {
 $(this).submit()
 });*/
/**
 * 更新模态框显示
 */
$("#updateModal").on('show.bs.modal', function () {
    var $that = $(this),
        $inputs = $that.find("input[type=text]"),
        tableData = $("#product_table").bootstrapTable("getSelections")[0],
        $recommend = $(this).find('select[name="position1"]');
    roleAuth = {};
    // 设置角色名、描述
    $inputs[0].value = tableData.stationName;
    $inputs[1].value = tableData.merchantName;
    $inputs[2].value = tableData.productName;
    $inputs[3].value = tableData.productPrice;
    $inputs[7].value = tableData.productId;
    $inputs[4].value = tableData.sales;
    $inputs[5].value = tableData.hits;
    $inputs[6].value = tableData.score;

    if (tableData.recommend == null) {
        $recommend.val('10001')
    } else {
        $recommend.val(tableData.recommend);
    }

});

/**
 * 角色操作(新增，更新，删除)
 * @param action [add/update/remove]
 * @param data
 *               [add/update 数据结构 {name: string, description: string, listAuthorityId: array} ]
 *               [delete 数据结构 {roleId: int}]
 * @param async/scync [default async]
 */
function product(action, data, async) {
    var url = null;
    (async === false) ? async = false : async = true;
    if (action === 'update') {
        url = '../product/updateProductRecommend';
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


/**
 * 新增,更新,删除角色表单提交
 */
$("#updateForm").submit(function () {
    var type = $(this).data("type"),
        $modal = $("#" + type + "Modal"),
        $inputs = $(this).find('input[type="text"]'),


        //$checkboxs = $(this).find('input[type="checkbox"]').prop('checked',true),
        tableData = $("#product_table").bootstrapTable("getSelections")[0],
        data = null,
        $that = $(this);


    // 模式选择,设置不同的data和url
    if (type === 'update') {
        var $recommen = $(this).find('select[name="position1"]'),
            $inputs = $(this).find('input[type="text"]');


        data = {
            'stationName': $inputs[0].value,
            'merchantName': $inputs[1].value,
            'productName': $inputs[2].value,
            'productPrice': $inputs[3].value,
            'recommend': $recommen.val(),
            'productId': $inputs[7].value,
            'sales': $inputs[4].value,
            'hits': $inputs[5].value,
            'score': $inputs[6].value,

        };
//        data['stationId'] = tableData.recommend;
    }
    ;

    $modal.modal('hide');
    //    完成ajax后刷新表格数据
    product(type, data).done(function () {
        $("#product_table").bootstrapTable("refresh");
    });
    // 阻止表格冒泡事件
    return false;
});
