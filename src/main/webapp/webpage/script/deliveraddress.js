var $tbody = $("#sample-table-1 tbody");
var allStation = [];
getAllStation().done(function() {
	// 初始化table
	$("#deliverAddress_table").bootstrapTable({
	    'url': '../deliver/query',     
	    'toolbar': $('div[data-name="btnCtl"]'),
	    'queryParams':function(params){
	    	params["stationId"]=$("#stationSelects").val();
	    	return params;
	    },
	    // 'toolbarAlign': 'right',
	    'striped': true,
	    //'sidePagination': 'server',
	    'pagination': true,
	    'pageSize': globalPageSize,
	    'pageList': [],
	    'clickToSelect': true,
	    'singleSelect': true,
	    'smartDisplay': true,
	  
	    'onCheck': function() {
	        $("button[data-target='#updateModal'], button[data-target='#deleteModal']").removeAttr("disabled");
	    },
	    'onUncheck': function() {
	        $("button[data-target='#updateModal'], button[data-target='#deleteModal']").attr("disabled", 'true');
	    },
	    'onPageChange': function() {
	    	if($("#deliverAddress_table").bootstrapTable('getSelections')[0]){
	    		$("#deliverAddress_table").bootstrapTable('uncheckBy', {
	                field: 'deliverAddressId',
	                values: [$("#deliverAddress_table").bootstrapTable('getSelections')[0].serviceProviderId]
	            });
	    	}
	        
	    },
	    'columns': [{
	        'checkbox': true,
	        //'class': 'hide'
	    }, 
	    {
	        'field': 'deliverAddressId',
	        'title': '送货地址ID',
	        'align': 'center',
	        'class': 'hide'
	       
	    },
	    {
	        'field': 'stationId',
	        'title': '车站',
	        'width':200,
	        'align': 'center',
	        'formatter': function(stationId) { 
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
	       }
	        
	    },
	    {
	        'field': 'address',
	        'title': '地址',
	        'align': 'center'
	       
	    },{
	        'field': 'orderNo',
	        'title': '显示顺序',
	        'width':20,
	        'align': 'center',
	        'sortable': false
	    }]
	});
});


/**
 * 送货地址查询ajax
 * @param name 请求查询的服务人员名称
 */
function deliverAddressQuery(stationId) {
    $.ajax({
        type: "POST",
        url: "../deliver/query",
        data: {           
            "stationId": stationId,
            "offset":0,
            "limit":globalPageSize
        },        
        dataType: 'json',       
        success: function(resb) { 
        	
        	 if (resb.length != 0) {  
                 $("#deliverAddress_table").bootstrapTable('load', resb.data);
             } else {
                 $("#deliverAddress_table").bootstrapTable('removeAll');
             }
        },
        error: function(XMLHttpRequest, textStatus, errorThrown) {
            alert(textStatus);
        }
    });
    return false;
}




/**
 * 角色操作(新增，更新，删除)
 * @param action [add/update/remove]
 * @param data
 *               [add/update 数据结构 {name: string, description: string, listAuthorityId: array} ]
 *               [delete 数据结构 {roleId: int}]
 * @param async/scync [default async]
 */
function deliverManagement(action, data, async) {
    var url = null;
    (async === false) ? async = false: async = true;
    if (action === 'add') {
        url = '../deliver/add';
    } else if (action === 'update') {
        url = '../deliver/update';
    } else {
        url = '../deliver/delete';
    };
    return $.ajax({
        type: "POST",
        url: url,
        data: data,
        dataType: 'json',
        async: async,
        success: function(resb) {
            alert(resb.message);
        },
        error: function(XMLHttpRequest, textStatus, errorThrown) {
            // console.log(XMLHttpRequest, textStatus, errorThrown);
            alert(textStatus);
        }
    });
}


/*
 * 查询所有车站
 */
function getAllStation(){
	return $.ajax({
         type: "POST",
         url:'../railwayStation/getOnLineStation',
         error: function(request) {
         	alert(request);
         },
         success: function(resb) {
         	var rs = eval("("+resb+")");         	
         	if(rs.success){
         		if(rs.data.length>1){
         			$(".selectAllStation").append("<option value=''>所有</option>");
         		};
             	for(var i = 0;i<rs.data.length;i++){
             		$(".selectAllStation").append("<option value="+rs.data[i].stationId+">"+rs.data[i].stationName+"</option>");
             	};
             	allStation = rs;
             
         	};
         }
 	});	
};


/**
 * 查询表单提交
 */
$("#stationFormId").submit(function() {
    return deliverAddressQuery($("#stationSelects").val());
});

/**
 * 新增模态框显示
 */
$("#addModal").on('show.bs.modal', function() {
	 $(this).find("input[type=text]").val("");
})

/**
 * 删除模态框显示
 */
$("#deleteModal").on('show.bs.modal', function() {
    var name = $("#deliverAddress_table").bootstrapTable('getSelections')[0].address,
        html = '你确定要删除送货地址: <span>' + name + '</span>?';
    $(this).find('div.modal-body').empty().append(html);
});

/**
 * 更新模态框显示
 */

$("#updateModal").on('show.bs.modal', function() {
    var $that = $(this),
        $inputs = $that.find("input[type=text]"),
       
        tableData = $("#deliverAddress_table").bootstrapTable("getSelections")[0],
        roleAuth = {};

    var $stationId = $(this).find('select[name="stationId"]');
	var $status = $(this).find('select[name="status"]');
	var $orderNo = $(this).find('select[name="orderNo"]');
 	
    // 设置ID,姓名,账号,电话,年龄
	$inputs[0].value = tableData.deliverAddressId;
    $inputs[1].value = tableData.address;
    $orderNo.val(tableData.orderNo);
    $stationId.val(tableData.stationId);
   
    
});

/**
 * 新增,更新,删除角色表单提交
 */
$("#addForm,#updateForm,#deleteForm").submit(function() {
    var type = $(this).data("type"),
        $modal = $("#" + type + "Modal"),
        $inputs = $(this).find('input[type="text"]'),
       
     
        //$checkboxs = $(this).find('input[type="checkbox"]').prop('checked',true),
        tableData = $("#deliverAddress_table").bootstrapTable("getSelections")[0],
        data = null,
        $that = $(this);
    
   
    // 模式选择,设置不同的data和url
    if (type === 'delete') {
    	data = {
    		'deliverAddressId': tableData.deliverAddressId
    	};
    } else if (type === 'add') {  
    	 var obj = jQuery("#addForm").validationEngine('validate', {
             promptPosition: "centerRight",
             autoPositionUpdate: true
         });
         if (!obj) {
             return false;
         };
    	var $stationId = $(this).find('select[name="stationId"]');
    	//var $stationId = $(this).find('select[name="stationId"]');
    	var $orderNo = $(this).find('select[name="orderNo"]');
    	data = {    
    			'stationId':$stationId.val(),
    			'address': $inputs[0].value,            
    	        'orderNo':$orderNo.val()
    	        
    	};
    } else if (type ==='update') {  
    	var $stationId = $(this).find('select[name="stationId"]');
    	var $orderNo = $(this).find('select[name="orderNo"]');
        data = {        	           
         	    
         	    'stationId':$stationId.val(tableData.stationId),
         	    'deliverAddressId':$inputs[0].value,
          	    'address': $inputs[1].value,            
  	            'orderNo':$orderNo.val()
  	            
        };
        data['stationId'] = tableData.stationId;
    };
 

    $modal.modal('hide');
    // 完成ajax后刷新表格数据
    deliverManagement(type, data).done(function() {
        $("#deliverAddress_table").bootstrapTable("refresh");
    });

    // 阻止表格冒泡事件
    return false;
});
