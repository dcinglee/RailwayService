var $tbody = $("#sample-table-1 tbody");
var allStation = [];
getAllStation().done(function() {
	// 初始化table
	$("#service_table").bootstrapTable({
	    'url': '../serviceType/findAll',     
	    'toolbar': $('div[data-name="btnCtl"]'),
	    'queryParams':function(params){
	    	params["stationId"]=$("#stationSelects").val();
	    	return params;
	    },
	    'toolbarAlign': 'right',
	    'striped': true,
	    'pagination': true,
		'sortable': true, //是否启用排序
		'sortOrder': "asc", //排序方式
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
	    	if($("#service_table").bootstrapTable('getSelections')[0]){
	    		$("#service_table").bootstrapTable('uncheckBy', {
	                field: 'typeId',
	                values: [$("#service_table").bootstrapTable('getSelections')[0].typeId]
	            });
	    	}
	        
	    },
	    'columns': [{
	        'checkbox': true,
	       // 'class': 'hide'
	    }, 
	    {
	        'field': 'typeId',
	        'title': 'ID',
	        'align': 'center',
	        'class': 'hide'
	       
	    },
	    {
	        'field': 'name',
	        'title': '服务名',
	        'width':150,
	        'align': 'center'
	       
	    },{
	        'field': 'introduction',
	        'title': '描述',
	        'align': 'center'
	        
	    },{
	        'field': 'distributionCosts',
	        'title': '配送费用(元)',
	        'width':100,
	        'align': 'right',
	        'formatter': function (distributionCosts) {
                 if(distributionCosts == null){
                	 return distributionCosts = "无"
                 }
                return distributionCosts.toFixed(2);
            }
	       
	    },{
	        'field': 'status',
	        'title': '状态',
	        'width':100,
	        'align': 'center',
	        'formatter': function (status) {
                if (status == '4001') {
                    status = '启用';
                } else if (status == '4002') {
                    status = '停用';
                }
                ;
                return status === null ? '未知' : status;
            }
	    },{
	        'field': 'createDate',
	        'title': '日期',
	        'width':150,
	        'align': 'center',
	        'sortable': true
	    }
	    
	    ]
	});
});


/**
 * 送货地址查询ajax
 * @param name 请求查询的服务人员名称
 */
function serviceQuery(name) {
	if(($("#inputStation").val()== '')){
		$('#service_table').bootstrapTable('refreshOptions',{pageNumber:1,pageSize:globalPageSize});
	}else{
    $.ajax({
        type: "POST",
        url: "../serviceType/findServiceByName",
        data: {           
            "name": name
        },        
        dataType: 'json',       
        success: function(resb) { 
        	 if (resb.data.length != 0) {  
                 $("#service_table").bootstrapTable('load', resb.data);
             } else {
                 $("#service_table").bootstrapTable('removeAll');
             }
        },
        error: function(XMLHttpRequest, textStatus, errorThrown) {
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
 *               [delete 数据结构 {roleId: int}]
 * @param async/scync [default async]
 */
function serviceManagement(action, data, async) {
    var url = null;
    (async === false) ? async = false: async = true;
    if (action === 'add') {
        url = '../serviceType/add';
    } else if (action === 'update') {
        url = '../serviceType/update';
    } else {
        url = '../serviceType/delete';
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
         url:'../railwayStation/queryAllStations',
         error: function(request) {
         	alert(request);
         },
         success: function(resb) {
         	var rs = eval("("+resb+")");         	
         	if(rs.success){
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
	
    return serviceQuery($("#inputStation").val());
});

//$("#stationFormId").submit(function() {
//    return serviceQueryByStationId($("#serviceProviderSel").val());
//});

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
    var name = $("#service_table").bootstrapTable('getSelections')[0].name,
        html = '你确定要删除服务: <span>' + name + '</span>?';
    $(this).find('div.modal-body').empty().append(html);
});

/**
 * 更新模态框显示
 */

$("#updateModal").on('show.bs.modal', function() {
    var $that = $(this),
        $inputs = $that.find("input[type=text]"),
       
        tableData = $("#service_table").bootstrapTable("getSelections")[0],
        roleAuth = {};

    
	var $status = $(this).find('select[name="status"]');
	
 	
    // 设置ID,姓名,账号,电话,年龄
	$inputs[0].value = tableData.typeId;
    $inputs[1].value = tableData.name;
    $inputs[2].value = tableData.introduction;
    $inputs[3].value = tableData.distributionCosts;
    $status.val(tableData.status);
   
   
    
});

/**
 * 新增,更新,删除角色表单提交
 */
$("#addForm,#updateForm,#deleteForm").submit(function() {
    var type = $(this).data("type"),
        $modal = $("#" + type + "Modal"),
        $inputs = $(this).find('input[type="text"]'),
       
     
        //$checkboxs = $(this).find('input[type="checkbox"]').prop('checked',true),
        tableData = $("#service_table").bootstrapTable("getSelections")[0],
        data = null,
        $that = $(this);
    
   
    // 模式选择,设置不同的data和url
    if (type === 'delete') {
    	data = {
    		'typeId': tableData.typeId
    		
    	};
    } else if (type === 'add') {
    	var obj = jQuery("#addForm").validationEngine('validate', {promptPosition : "centerRight", autoPositionUpdate : true});
    	if(!obj){
    		return false;
    	};
    	var $status = $(this).find('select[name="status"]');
    	var $orderNo = $(this).find('select[name="orderNo"]');
    	data = {    
    			
    			'name':$inputs[0].value,
    			'introduction': $inputs[1].value,
    			'distributionCosts': $inputs[2].value,
    	        'status':$status.val()
    	        
    	};
    } else if (type ==='update') {  
    	var $status = $(this).find('select[name="status"]');
    
        data = {        	           
        		'typeId':$inputs[0].value,
        		'name':$inputs[1].value,
    			'introduction': $inputs[2].value,
    			'distributionCosts': $inputs[3].value,
    	        'status':$status.val()
  	            
        };
        data['typeId'] = tableData.typeId;
    };
 

    $modal.modal('hide');
    // 完成ajax后刷新表格数据
    serviceManagement(type, data).done(function() {
        $("#service_table").bootstrapTable("refresh");
        $that[0].reset();
    });

    // 阻止表格冒泡事件
    return false;
});
