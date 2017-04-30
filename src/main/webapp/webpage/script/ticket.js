var $tbody = $("#sample-table-1 tbody");
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
// 初始化table
$("#ticket_table").bootstrapTable({
    'url': '../ticketOrder/queryTicketOrderDetail',
    'toolbar': $('div[data-name="btnCtl"]'),
    // 'toolbarAlign': 'right',
    'queryParams': function (params) {
        params["passengerName"] = $("#passengername").val();
        params["noticePhoneNo"] = $("#csphone").val();
        params["orderType"] = $("#ordertype").val();
        params["orderStatus"] = $("#orderstatus").val();
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
        // console.log($("#ticket_table").bootstrapTable('getSelections')[0]=='undefined');
        if (typeof($("#ticket_table").bootstrapTable('getSelections')[0]) != 'undefined') {
            $("#ticket_table").bootstrapTable('uncheckBy', {
                field: 'orderId',
                values: [$("#ticket_table").bootstrapTable('getSelections')[0].orderId]
            });
        }
    },
    'columns': [{
        'checkbox': true,
        // 'class': 'hide'
    }, {
        'field': 'ticketOrderId',
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
        'field': 'orderType',
        'title': '购票类型',
        'align': 'center',
        'formatter': function (orderType) {
                if (orderType == '18601') {
                	orderType = '订票';
                } else if(orderType == '18602') {
                	orderType = '抢票';
                }
                return orderType;
            }
    }, {
        'field': 'passengerName',
        'title': '乘客姓名',
        'align': 'center'
    }, {
        'field': 'noticePhoneNo',
        'title': '通知手机号',
        'align': 'center'
    }, {
        'field': 'aboardStation',
        'title': '出发站',
        'align': 'center',
    },{
        'field': 'arrivedStation',
        'title': '目的站',
        'align': 'center',
    },{
        'field': 'ticketNumber',
        'title': '票数',
        'align': 'center',
    }, {
        'field': 'orderStatus',
        'title': '订单状态',
        'align': 'center',
        'formatter': function (orderStatus) {
            if (orderStatus == '18501') {
            	orderStatus = '订票成功';
            } else if (orderStatus == '18502') {
            	orderStatus = '订票异常';
            } else if (orderStatus == '18503') {
            	orderStatus = '待抢票';
            }
            else if (orderStatus == '18504') {
            	orderStatus = '抢票中';
            }
            else if (orderStatus == '18505') {
            	orderStatus = '用户取消';
            }
            else if (orderStatus == '18506') {
            	orderStatus = '停止抢票';
            }
            else if (orderStatus == '18507') {
            	orderStatus = '抢票成功';
            }
            else if (orderStatus == '18508') {
            	orderStatus = '未抢到票';
            }
            else if (orderStatus == '18509') {
            	orderStatus = '抢票异常';
            }
            return orderStatus;
        }
        }]
});

/**
 * 角色查询ajax
 * @param stationName 请求查询的车站名
 */

function orderQuery(passengerName, noticePhoneNo, orderType,orderStatus, startTime, endTime) {
    if (($("#passengerName").val() == '') || ($("#noticePhoneNo").val() == '') || ($("#orderType").val() == '')|| ($("#orderStatus").val() == '')) {
        $('#ticket_table').bootstrapTable('refreshOptions', {pageNumber: 1, pageSize: globalPageSize});
    } else {
        $.ajax({
            type: "POST",
            url: "../ticketOrder/queryTicketOrderDetail",
            data: {
                "passengerName": passengerName,
                'noticePhoneNo': noticePhoneNo,
                'orderType': orderType,
                'orderStatus':orderStatus,
                'startTime': startTime,
                'endTime':endTime,
                "offset": 0,
                "limit": globalPageSize
            },
            dataType: 'json',
            'sidePagination': 'server',
            success: function (resb) {
                console.log(resb.data);
                if (resb.data.length != 0) {
                    $("#ticket_table").bootstrapTable('load', {
                        'total': resb.data.total,
                        'rows': resb.data.rows,
                    });
                }
                else {
                    $("#ticket_table").bootstrapTable('removeAll');
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
 * 导出Excel
 * @returns
 */
function exportExcel() {
	console.log('1')
    var $that = $(this),
        $inputs = $that.find("input[type=text]"),
        $orderType = $(this).find('select[name="ordertype"]'),
    	$orderStatus = $(this).find('select[name="orderstatus"]');
    orderType = $("#ordertype").val(),
    orderStatus = $("#orderstatus").val(),
        passengerName = $("#passengername").val(),
        noticePhoneNo = $("#csphone").val(),
    	startTime = $("#startdate").val(),
        endTime = $("#enddate").val();
    var url = '../ticketOrder/downloadServiceProvider.action?' + 'passengerName=' + passengerName + '&noticePhoneNo=' + noticePhoneNo +
        '&orderType=' + orderType + '&orderStatus=' + orderStatus + '&startTime=' + startTime + '&endTime=' + endTime;
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
 * 查询当前id的最新权限
 * @param  {[int]} id [角色id]
 * @return {[array]}  [权限列表]
 */

/**
 * 查询表单提交
 */
/**/
$("#stationFormId").submit(function () {
    return orderQuery($("#passengername").val(), $("#csphone").val(), $("#ordertype").val(),$("#orderstatus").val(), $("#startdate").val(), $("#enddate").val());
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