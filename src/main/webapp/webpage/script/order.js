var $tbody = $("#sample-table-1 tbody");
var allStation = [];
getAllStation();
var nowdate = new Date(); 
var oneweekdate = new Date(nowdate-7*24*3600*1000);
$("#startdate").val(beginDate(nowdate));
$("#enddate").val(endDate(oneweekdate));

function beginDate(date) {
    return endDate(date);
};

function endDate(date) {
    return date.getFullYear() + "-" + ("0" + (date.getMonth() + 1)).slice(-2) + "-" + ("0" + date.getDate()).slice(-2);
};
// 初始化table
$("#order_table").bootstrapTable({
    'url': '../order/queryMainOrder',
    'toolbar': $('div[data-name="btnCtl"]'),
    // 'toolbarAlign': 'right',
    'queryParams': function (params) {
        params["orderNo"] = $("#orderno").val();
        params["customerName"] = $("#csname").val();
        params["customerPhoneNo"] = $("#csphone").val();
        params["startTime"] = $("#startdate").val();
        params["endTime"] = $("#enddate").val();
        return params;
    },
    'striped': true,
    'pagination': true,
    'sortable': true,
    'sortOrder': "createDate",
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
        // console.log($("#order_table").bootstrapTable('getSelections')[0]=='undefined');
        if (typeof($("#order_table").bootstrapTable('getSelections')[0]) != 'undefined') {
            $("#order_table").bootstrapTable('uncheckBy', {
                field: 'orderId',
                values: [$("#order_table").bootstrapTable('getSelections')[0].orderId]
            });
        }
    },
    'columns': [{
        'checkbox': true,
        // 'class': 'hide'
    }, {
        'field': 'orderId',
        'title': 'ID',
        'align': 'center',
        'class': 'hide'
    }, {
        'field': 'orderNo',
        'title': '订单编号',
        'align': 'center',
        'sortable': false,
    }, {
        'field': 'createDate',
        'title': '订单日期',
        'align': 'center',
        'sortable': true,
        //'sortable': true,
    }, {
        'field': 'customerName',
        'title': '客户姓名',
        'align': 'center'
    }, {
        'field': 'customerPhoneNo',
        'title': '客户手机',
        'align': 'center'
    }, {
        'field': 'orderTotalPrice',
        'title': '订单价格(元)',
        'align': 'center',
        'class': 'hide'
    }, {
        'field': 'deliverType',
        'title': '递送方式',
        'align': 'center',
        'class': 'hide'
    }, {
        'field': 'deliverAddressId',
        'title': '收货地址',
        'align': 'center',
        'class': 'hide'
    }, {
        'field': 'merchantName',
        'title': '商户名称',
        'align': 'center'
    },
        {
            'field': 'stationId',
            'title': '所属车站',
            'width': 200,
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
            'field': 'payType',
            'title': '支付类型',
            'align': 'center',
            'class': 'hide',
            'formatter': function (payType) {
                if (payType == '14001') {
                    payType = '微信支付';
                } else if (payType == '14002') {
                    payType = '支付宝支付';
                } else if (payType == '14003') {
                    payType = '信美分期';
                }
                return payType;
            }
        }, {
            'field': 'payStatus',
            'title': '支付状态',
            'align': 'center',
            'formatter': function (payStatus) {
                if (payStatus == '13001') {
                    payStatus = '已支付';
                } else if (payStatus == '13002') {
                    payStatus = '未支付';
                } else if (payStatus == '13003') {
                    payStatus = '申请退款';
                }
                else if (payStatus == '13004') {
                    payStatus = '已退款';
                }
                return payStatus;
            }
        },
        {
            'field': 'orderStatus',
            'title': '订单状态',
            'align': 'center',
            'formatter': function (orderStatus) {
                if (orderStatus == '7001') {
                    orderStatus = '新建待支付';
                } else if (orderStatus == '7000') {
                    orderStatus = '订单已完成';
                } else if (orderStatus == '7010') {
                    orderStatus = '等待商家接单';
                } else if (orderStatus == '7020') {
                    orderStatus = '商家已接单';
                } else if (orderStatus == '7030') {
                    orderStatus = '商家拒绝接单';
                } else if (orderStatus == '7040') {
                    orderStatus = '服务员已接单';
                } else if (orderStatus == '7050') {
                    orderStatus = '服务员已取货';
                } else if (orderStatus == '7055') {
                    orderStatus = '等待用户取货';
                }
                 else if (orderStatus == '7060') {
                    orderStatus = '订单已完成';
                } else if (orderStatus == '7070') {
                    orderStatus = '订单已取消';
                } else if (orderStatus == '7080') {
                    orderStatus = '订单已失效';
                } else if (orderStatus == '7090') {
                    orderStatus = '订单已超时';
                }
                return orderStatus;
            }
        }]
});

/**
 * 角色查询ajax
 * @param stationName 请求查询的车站名
 */

function orderQuery(orderNo, customerName, customerPhoneNo, beginDate, endDate) {
    if (($("#orderno").val() == '') || ($("#csname").val() == '') || ($("#csphone").val() == '')) {
        $('#order_table').bootstrapTable('refreshOptions', {pageNumber: 1, pageSize: globalPageSize});
    } else {
        $.ajax({
            type: "POST",
            url: "../order/queryMainOrder",
            data: {
                "orderNo": orderNo,
                'customerName': customerName,
                'customerPhoneNo': customerPhoneNo,
                'startTime': beginDate,
                'endTime': endDate,
                "offset": 0,
                "limit": globalPageSize
            },
            dataType: 'json',
            'sidePagination': 'server',
            success: function (resb) {
                //console.log(resb.data.total);
                if (resb.data.length != 0) {
                    $("#order_table").bootstrapTable('load', {
                        'total': resb.data.total,
                        'rows': resb.data.rows,
                    });
                }
                else {
                    $("#order_table").bootstrapTable('removeAll');
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
 * 查询详情
 *
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
                /*if(rs.data.length>1){
                 $(".selectAllStation").append("<option value=''>所有</option>");
                 }*/
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
$("#detailModal").on('show.bs.modal', function () {
    var $that = $(this),
        $inputs = $that.find("input[type=text]"),
        $sels = $that.find("select"),
        tableData = $("#order_table").bootstrapTable("getSelections")[0];

    // 设置ID、站名、站名缩写、省、市、状态、时间
    $inputs[0].value = tableData.orderId;
    $inputs[1].value = tableData.orderNo;
    $inputs[2].value = tableData.createDate;
    $inputs[3].value = tableData.customerName;
    $inputs[4].value = tableData.customerPhoneNo;
    $inputs[5].value = tableData.merchantName;
    // $inputs[5].value = tableData.payType;
    if (tableData.payType == '14001') {
        $inputs[6].value = '微信支付'
    } else if (tableData.payType == '14002') {
        $inputs[6].value = '支付宝支付'
    } else if (tableData.payType == '14003') {
        $inputs[6].value = '信美分期'
    } else {
        $inputs[6].value = '-'
    }
    ;

    if (tableData.payDate == null) {
        $inputs[7].value = '无';
    } else {
        $inputs[7].value = tableData.payDate;
    }
    ;


    if (tableData.payStatus == '13001') {
        $inputs[8].value = '已支付'
    } else if (tableData.payStatus == '13002') {
        $inputs[8].value = '未支付'
    } else if (tableData.payStatus == '13003') {
        $inputs[8].value = '申请退款'
    } else if (tableData.payStatus == '13004') {
        $inputs[8].value = '已退款'
    } else {
        $inputs[8].value = '-'
    }
    ;

    if (tableData.orderStatus == '7000') {
        $inputs[9].value = '订单已完成'
    } else if (tableData.orderStatus == '7010') {
        $inputs[9].value = '等待商家接单'
    } else if (tableData.orderStatus == '7020') {
        $inputs[9].value = '商家已接单'
    } else if (tableData.orderStatus == '7030') {
        $inputs[9].value = '商家拒绝接单'
    } else if (tableData.orderStatus == '7040') {
        $inputs[9].value = '服务员已接单'
    } else if (tableData.orderStatus == '7050') {
        $inputs[9].value = '服务员已取货'
    } else if (tableData.orderStatus == '7060') {
        $inputs[9].value = '订单已完成'
    } else if (tableData.orderStatus == '7070') {
        $inputs[9].value = '订单已取消'
    } else if (tableData.orderStatus == '7080') {
        $inputs[9].value = '订单已失效'
    } else if (tableData.orderStatus == '7090') {
        $inputs[9].value = '订单已超时'
    } else {
        $inputs[9].value = '-'
    }
    ;

    $inputs[10].value = tableData.orderTotalPrice;
    if (tableData.refundApplyDate == null) {
        $inputs[11].value = '无';
    } else {
        $inputs[11].value = tableData.refundApplyDate;
    }
    ;
    if (tableData.refundDate == null) {
        $inputs[12].value = '无';
    } else {
        $inputs[12].value = tableData.refundDate;
    }
    ;

    var $stationId = $(this).find('select[name="stationId"]');
    $stationId.val(tableData.stationId);


    var orderId = tableData.orderId;
    $.ajax({
        type: "POST",
        url: "../order/querySubOrder",
        data: {
            "mainOrderId": orderId,
        },
        dataType: 'json',
        success: function (resb) {
            var $tbody = $("#sample-table-1 tbody");
            $tbody.empty();
            if (resb.data != null && resb.data.length != 0) {
                $.each(resb.data, function () {
                    //克隆模板并取得其HTML
                	/*console.log(resb.data);
                	console.log(resb.data[0]);*/
                    var html = $("#cloneTR").clone().html();
                    var str = html.replace("1", this.mainOrderId);
                    //替换相关信息
                    str = str.replace("秀秀",this.productName);
                   
                   if (this.productCount == null) {
                        str = str.replace("99", "0");
                    } else {
                        str = str.replace("99", this.productCount);
                    }
                    if (this.productPrice == "null") {
                        str = str.replace("998", "0");
                    } else {
                        str = str.replace("998", this.productPrice);
                    }
                    ;
                    if (this.totalPrice == null) {
                        str = str.replace("9999", "0");
                    } else {
                        str = str.replace("9999", this.totalPrice);
                    }
                    ;
                    $tbody.append(str);
                });
            }
            else {
                $tbody.empty();
                $tbody.append("<tr class='no-records-found'><td colspan='4' style='text-align:center'>没有找到匹配的记录</td></tr>");
            }
            ;
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            alert(textStatus);
        }
    });
});

/**
 * 查询当前id的最新权限
 * @param  {[int]} id [角色id]
 * @return {[array]}  [权限列表]
 */

/**
 * 查询表单提交
 */
/**/
$("#stationFormId").submit(function () {
    /*if(($("#enddate").val() != "")){
     var isdate = new Date($("#enddate").val().replace(/-/g,"/"));
     isdate = new Date((isdate/1000+86400)*1000);
     pdate = isdate.getFullYear()+"-"+(isdate.getMonth()+1)+"-"+(isdate.getDate());
     return orderQuery($("#orderno").val(),$("#csname").val(),$("#csphone").val(),$("#startdate").val(),pdate);
     }else{
     return orderQuery($("#orderno").val(),$("#csname").val(),$("#csphone").val(),$("#startdate").val(),);
     };*/
    return orderQuery($("#orderno").val(), $("#csname").val(), $("#csphone").val(), $("#startdate").val(), $("#enddate").val());
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