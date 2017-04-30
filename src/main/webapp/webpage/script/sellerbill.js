var $tbody = $("#sample-table-1 tbody");
var allStation = [];
//var startdate = new Date();
//var pdate = startdate.getFullYear()+"-"+(startdate.getMonth()+1)+"-"+(startdate.getDate()),
//    pdate2 = startdate.getFullYear()+"-"+(startdate.getMonth())+"-"+(startdate.getDate());
////var p2date = startdate.getFullYear()+"-"+(startdate.getMonth())+"-"+(startdate.getDate());
//$("#startdate").val(pdate2);
//$("#enddate").val(pdate);

var nowdate = new Date(); 
var oneweekdate = new Date(nowdate-7*24*3600*1000);  
$("#startdate").val(beginDate(oneweekdate));
$("#enddate").val(endDate(nowdate));

function beginDate(date) {
    return endDate(date);
};

function endDate(date) {
    return date.getFullYear()+ "-"+ ("0" + (date.getMonth() + 1)).slice(-2)+ "-"+ ("0" + date.getDate()).slice(-2);
};


//getAllStation();
getAllStation().done(function () {
// 初始化table
$("#sellerbill_table").bootstrapTable({
    'url': '../finance/queryMerchant',
    'toolbar': $('div[data-name="btnCtl"]'),
    // 'toolbarAlign': 'right',
    'queryParams':function(params){
    	params["stationId"] = $("#serviceProviderSel").val();
    	params["merchantName"] = $("#mcname").val();
    	params["beginDate"]=$("#startdate").val();
    	params["endDate"]=$("#enddate").val();
    	return params;
    },
    'striped': true,
    'pagination': true,
    //'sidePagination': 'server',
    'pageSize': '15',
    'pageList': [],
    'clickToSelect': true,
    'singleSelect': true,
    'smartDisplay': true,
    /*'responseHandler': function(resb) {
    	return {
    		'total': resb.data.length,
    		'rows': resb.data,
    	}
    },*/
    'onCheck': function() {
        $("button[data-target='#updateModal'], button[data-target='#deleteModal']").removeAttr("disabled");
    },
    'onUncheck': function() {
        $("button[data-target='#updateModal'], button[data-target='#deleteModal']").attr("disabled", 'true');
    },
    'onPageChange': function() {
        $("#sellerbill_table").bootstrapTable('uncheckBy', {
            field: 'stationId',
            values: [$("#sellerbill_table").bootstrapTable('getSelections')[0].stationId]
        });
    },
    'columns': [{
        'checkbox': true,
        'class': 'hide'
    }, {
        'field': 'stationId',
        'title': '车站名称',
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
    }, {
        'field': 'merchantId',
        'title': '商户ID',
        'align': 'center',
        'class':'hide'
    }, {
        'field': 'merchantName',
        'title': '商户名称',
        'align': 'center',
    }, {
        'field': 'createDate',
        'title': '订单日期',
        'align': 'center',
    }, {
        'field': 'completedCount',
        'title': '已完成订单',
        'align': 'center',
    }, {
        'field': 'completedPrice',
        'title': '完成订单金额(元)',
        'align': 'center',
        'formatter': function (completedPrice) {
        	 if(completedPrice == null){
          	   return  completedPrice = '无';
             }
            return completedPrice.toFixed(2);
        }
    }, {
        'field': 'uncompletedCount',
        'title': '未完成订单',
        'align': 'center',
    }, {
        'field': 'uncompletedPrice',
        'title': '未完成订单金额(元)',
        'align': 'center',
        'formatter': function (uncompletedPrice) {
           if(uncompletedPrice == null){
        	   return  uncompletedPrice = '无';
           }
            return uncompletedPrice.toFixed(2);
        }
    
    }, {
        'field': 'cancelCount',
        'title': '取消订单',
        'align': 'center',
    }, {
        'field': 'cancelPrice',
        'title': '取消订单金额(元)',
        'align': 'center',
        'formatter': function (cancelPrice) {
        	if(cancelPrice == null){
         	   return  cancelPrice = '无';
            }
            return cancelPrice.toFixed(2);
        }
    }, {
        'field': 'rejectCount',
        'title': '拒绝订单',
        'align': 'center',
    }, {
        'field': 'rejectPrice',
        'title': '拒绝订单金额(元)',
        'align': 'center',
        'formatter': function (rejectPrice) {
        	if(rejectPrice == null){
          	   return  rejectPrice = '无';
             }
            return rejectPrice.toFixed(2);
        }
    }, {
        'field': 'totalCount',
        'title': '合计订单',
        'align': 'center',
    }, {
        'field': 'totalPrice',
        'title': '合计金额(元)',
        'align': 'center',
        'formatter': function (totalPrice) {
        	if(totalPrice == null){
           	   return  totalPrice = '无';
              }
            return totalPrice.toFixed(2);
        }
    }]
  });
});

/**
 * 角色查询ajax
 * @param stationName 请求查询的车站名
 */
function orderQuery(n,m,w,y) {
    if(($("#mcname").val()== '')){
        $('#sellerbill_table').bootstrapTable('refreshOptions',{pageNumber:1,pageSize:globalPageSize});
    }else {
        $.ajax({
            type: "POST",
            url: "../finance/queryMerchant",
            data: {
                "stationId": n,
                'merchantName': m,
                'beginDate': w,
                'endDate': y,
                "offset": 0,
                "limit": globalPageSize
            },
            dataType: 'json',
           // 'sidePagination': 'server',
            success: function (resb) {
                //console.log(resb.data.total);
                if (resb.data.length != 0) {
                    $("#sellerbill_table").bootstrapTable('load', resb.data);
                }               
                else {
                    $("#sellerbill_table").bootstrapTable('removeAll');
                }
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                alert(textStatus);
            }
        });
    }
    return false;
};


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
            	if(rs.data.length>1){
         			$(".selectAllStation").append("<option value=''>所有</option>");
         		};
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
 * 查询详情
 * 
 */

/**
 * 查询当前id的最新权限
 * @param  {[int]} id [角色id]
 * @return {[array]}  [权限列表]
 */

/**
 * 查询表单提交
 */
$("#stationFormId").submit(function() {
    return orderQuery($("#serviceProviderSel").val(),$("#mcname").val(),$("#startdate").val(),$("#enddate").val());
});




/**
 * 导出表格
 */
function exportExcel(){
	var stationId = $("#serviceProviderSel").val(),
	    name = $("#mcname").val(),
	    beginDate = $("#startdate").val(),
	    endDate = $("#enddate").val();
	    console.log(stationId+beginDate);
	    var url= '../finance/downloadMerchant.action?'+'stationId='+stationId+'&name='+name+
	   		'&beginDate='+beginDate+'&endDate='+endDate;	 
	       
		 $('body').append("<iframe id=\"downloadcsv\" style=\"display:none\"></iframe>");
		 $('#downloadcsv').attr('src',url);
};

$("#derive").on('click',function(){exportExcel()});


jQuery(function($) {
	$('#id-disable-check').on('click', function() {
		var inp = $('#form-input-readonly').get(0);
		if(inp.hasAttribute('disabled')) {
			inp.setAttribute('readonly' , 'true');
			inp.removeAttribute('disabled');
			inp.value="This text field is readonly!";
		}
		else {
			inp.setAttribute('disabled' , 'disabled');
			inp.removeAttribute('readonly');
			inp.value="This text field is disabled!";
		}
	}); 	
		$('.date-picker').datepicker({autoclose:true}).next().on(ace.click_event, function(){
		$(this).prev().focus();
	}); 
});