var $tbody = $("#sample-table-1 tbody");

// 初始化table
$("#user_table").bootstrapTable({
    'url': '../user/queryAllUser',
    'queryParams':function(params){
    	params["nickName"] = $("#nickname").val();
    	params["phoneNo"] = $("phoneno").val();
    	return params;
    },
    'toolbar': $('div[data-name="btnCtl"]'),
    'striped': true,
    'pagination': true,
    'sidePagination': 'server',
    'pageSize': globalPageSize,
    'pageList': [],
    'clickToSelect': true,
    'singleSelect': true,
    'smartDisplay': true,
    'responseHandler': function(resb) {
    	return {
    		'total': resb.data.total,
    		'rows': resb.data.rows,
    	}
    },
    'onCheck': function() {
        $("button[data-target='#updateModal'], button[data-target='#deleteModal']").removeAttr("disabled");
    },
    'onUncheck': function() {
        $("button[data-target='#updateModal'], button[data-target='#deleteModal']").attr("disabled", 'true');
    },
    'columns': [{
        'checkbox': true,
        'class': 'hide'
    }, {
    	'field': 'userId',
        'title': 'ID',
        'align': 'center',
        'class': 'hide'
    }, {
        'field': 'openid',
        'title': '账号',
        'align': 'center'
    }, {
        'field': 'nickName',
        'title': '昵称',
        'align': 'center',
        'sortable': false,
    }, {
        'field': 'gender',
        'title': '性别',
        'align': 'center',
        'formatter': function(gender){
        	if(gender == '1'){
        		gender = '男';
        	}else if(gender == '2'){
        		gender = '女';
        	}
        	return gender == '0' ? '保密': gender;
        }
    }, {
        'field': 'phoneNo',
        'title': '手机号码',
        'align': 'center'
    }, {
        'field': 'province',
        'title': '省',
        'align': 'center'
    }, {
        'field': 'city',
        'title': '市',
        'align': 'center'
    }//, {
      //  'field': 'status',
       // 'title': '状态',
       // 'align': 'center',
       // 'formatter':function(status){
        //	if(status == '4001'){
        //		 status = '启用';
        	// } else if(status=='4002'){
        	//	 status = '停用';
        //	 }
        	//return status;
       // }
   // }
        , {
        'field': 'createDate',
        'title': '创建时间',
        'align': 'center'
    }]
});

/**
 * 角色查询ajax
 * @param stationName 请求查询的车站名
 */

function userQuery(n,m) {
	if(($("#nickname").val()== '')&&($("#phoneno").val())=='' ){
		$('#user_table').bootstrapTable('refreshOptions',{pageNumber:1,pageSize:globalPageSize});
	}else{
    $.ajax({
        type: "POST",
        url: "../user/queryAllUser",
        data: {
            "nickName": n,
            'phoneNo': m,
            "offset":0,
            "limit":globalPageSize
        },
        dataType: 'json',
        success: function(resb) {
        	console.log(resb.data);
        	console.log();
            if (resb.data.length != 0) {
                $("#user_table").bootstrapTable('load',  {
            		'total': resb.data.total,
            		'rows': resb.data.rows,
                  } );
            } else {
                $("#user_table").bootstrapTable('removeAll');
            }
        },
        error: function(XMLHttpRequest, textStatus, errorThrown) {
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
function userManagement(action, data, async) {
    var url = null;
    (async === false) ? async = false: async = true;
    if (action === 'add') {
        url = '../user/add';
//    } else if (action === 'update') {
//        url = '../user/update';
//    } else {
//        url = '../user/delete';
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
$("#stationFormId").submit(function() {
    return userQuery($("#nickname").val(),$("#phoneno").val());
});


/**
 * 新增模态框显示
 */
$("#addModal").on('show.bs.modal', function() {
   /* autoRenderAuth($(this).find('div#perms'));*/
	$(this).find('input[type=text]').val("");
})

/**
 * 删除模态框显示
 */

/**
 * 更新模态框显示
 */

/**
 * 新增,更新,删除角色表单提交
 */
$("#addForm,#updateForm,#deleteForm").submit(function() {
    var type = $(this).data("type"),
        $modal = $("#" + type + "Modal"),
        $inputs = $(this).find('input[type="text"]'),
        $sels = $(this).find('select'),
        tableData = $("#user_table").bootstrapTable("getSelections")[0],
        data = null;


    	if (type ==='add'){
        data = {
            'openid': $inputs[0].value,
            'nickName': $inputs[1].value,
            'phoneNo':$inputs[2].value,
            'province':$inputs[3].value,
            'city':$inputs[4].value,
            'gender':$('input:radio[name="gender"]:checked').val(),
               };
    };
    


    $modal.modal('hide');
    // 完成ajax后刷新表格数据
    userManagement(type, data).done(function() {
        $("#user_table").bootstrapTable("refresh")
    });

    // 阻止表格冒泡事件
    return false;
});
