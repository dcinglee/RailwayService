var $tbody = $("#sample-table-1 tbody");
var allStation = [];
var allAdminRoles = [];


// 初始化table
$("#advertise_table").bootstrapTable({
    'url': '../AdBanner/query',
    'queryParams': function (params) {
        params["title"] = $("#inputStation").val();
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
            'total': resb.data.total,
            'rows': resb.data.rows
        }
    },
    'onCheck': function () {
        $("button[data-target='#updateModal'], button[data-target='#deleteModal']").removeAttr("disabled");
    },
    'onUncheck': function () {
        $("button[data-target='#updateModal'], button[data-target='#deleteModal']").attr("disabled", 'true');
    },
    'onPageChange': function () {
        if ($("#advertise_table").bootstrapTable('getSelections')[0]) {
            $("#advertise_table").bootstrapTable('uncheckBy', {
                field: 'adBannerId',
                values: [$("#advertise_table").bootstrapTable('getSelections')[0].adBannerId]
            });
        }

    },
    'columns': [{
        'checkbox': true,
        
    },
        {
            'field': 'adBannerId',
            'title': 'ID',
            'align': 'center',
            'class': 'hide'
        },
        {
            'field': 'adminId',
            'title': 'ID',
            'align': 'center',
            'class': 'hide'
        },
        {
            'field': 'title',
            'title': '标题',
            'align': 'center'

        },
        {
            'field': 'content',
            'title': '广告内容',
            'align': 'center'

        },
        {
            'field': 'adWeight',
            'title': '广告权重',
            'align': 'center'

        },
        {
            'field': 'linkUrl',
            'title': '广告链接',
            'align': 'center'
        },
        {
            'field': 'adminName',
            'title': '管理员姓名',
            'align': 'center'

        },
        {
            'field': 'hits',
            'title': '点击率',
            'align': 'center'
        },
        {
            'field': 'createDate',
            'title': '创建时间',
            'align': 'center'
        }
    ]

});

/**
 * 管理人员查询ajax
 * @param name 请求查询的服务人员
 */
function advertiseQuery(title) {
	if(($("#inputStation").val()== '')){
		$('#advertise_table').bootstrapTable('refreshOptions',{pageNumber:1,pageSize:globalPageSize});
	}else{
    $.ajax({
        type: "POST",
        url: "../AdBanner/query",
        data: {
            'title': title,
            'offset': 0,
            'limit': 15
        },
        dataType: 'json',
        'sidePagination': 'server',

        success: function (resb) {
            if (resb.data.totalElements != 0) {
                $("#advertise_table").bootstrapTable('load', {
                    'total': resb.data.total,
                    'rows': resb.data.rows
                });
            } else {
                $("#advertise_table").bootstrapTable('removeAll');
            }
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
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
 *               [delete 数据结构 {adminId: int}]
 * @param async/scync [default async]
 */
function advertiseManagement(action, data, async) {
  
    var url = null;
    (async === false) ? async = false : async = true;
    if (action === 'add') {
        url = '../AdBanner/add';
    } else if (action === 'update') {
        url = '../AdBanner/update';
    } else {
        url = '../AdBanner/delete';
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
 * 添加模态框隐藏
 */
$("#addModal").on('hide.bs.modal', function () {
    /* autoRenderAuth($(this).find('div#perms'));*/
    $(this).find('img').attr("src","");
});

/**
 * 查询表单提交
 */
$("#stationFormId").submit(function () {
    return advertiseQuery($("#inputStation").val());
});

/**
 * 新增模态框显示
 */
$("#addModal").on('show.bs.modal', function () {

    $(this).find('input[type=text]').val("");
});

/**
 * 删除模态框显示
 */
$("#deleteModal").on('show.bs.modal', function () {
    var name = $("#advertise_table").bootstrapTable('getSelections')[0].title,
        html = '你确定要删除广告: <span>' + name + '</span>?';
    $(this).find('div.modal-body').empty().append(html);
});

/**
 * 更新模态框显示
 */
$("#updateModal").on('show.bs.modal', function () {
    var $that = $(this),
        $inputs = $that.find("input[type=text]"),
        tableData = $("#advertise_table").bootstrapTable("getSelections")[0],
        roleAuth = {};

    //var $belongId = $(this).find('select[name="belongId"]'); 

    // 设置ID,姓名,账号,电话,年龄

    $inputs[0].value = tableData.adBannerId;
    $inputs[1].value = tableData.adminId;
    $inputs[2].value = tableData.title;
    $inputs[3].value = tableData.content;
    $inputs[4].value = tableData.adWeight;
    $inputs[5].value = tableData.linkUrl;
    $inputs[6].value = tableData.hits;
    //   $inputs[5].value = tableData.gender;


    //$inputs[5].value = tableData.gender.value;
    //$that.find("input[type=radio]").removeAttr("checked");
    //$that.find("input[type=radio]").attr("checked",false);
    
    var imageId=tableData.imageId;
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
                if(rs.code == '1'){               	
                	var preview = document.getElementById('advertiseImgs');
                	preview.src = rs.data;
                }else{
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

        //  $inputps = $(this).find('input[type="passowrd"]'),
        tableData = $("#advertise_table").bootstrapTable("getSelections")[0],
        data = null,
        $that = $(this);

    // 模式选择,设置不同的data和url
    if (type === 'delete') {
        data = {
            'adBannerId': tableData.adBannerId
        };
    } else if (type === 'add') {
    	 var obj = jQuery("#addForm").validationEngine('validate', {
             promptPosition: "centerRight",
             autoPositionUpdate: true
         });
         if (!obj) {
             return false;
         };
        var $inputs = $(this).find('input[type="text"]');
        data = {
            'title': $inputs[0].value,
            'content': $inputs[1].value,
            'adWeight': $inputs[2].value,
            'linkUrl': $inputs[3].value,
            'imageId': $("#advertiseImages").val()

        };
    } else if (type === 'update') {
        var $inputs = $(this).find('input[type="text"]');
        data = {
            'adBannerId': $inputs[0].value,
            'adminId':$inputs[1].value,
            'title': $inputs[2].value,
            'content': $inputs[3].value,
            'adWeight': $inputs[4].value,
            'linkUrl': $inputs[5].value,
            'hits': $inputs[6].value,
           'imageId':$("#advertiseUpdateImages").val()
        };
        // data['adBannerId'] = tableData.adBannerId;
    }
    ;

    $modal.modal('hide');
    // 完成ajax后刷新表格数据
    advertiseManagement(type, data).done(function () {
        $("#advertise_table").bootstrapTable("refresh");
    });

    // 阻止表格冒泡事件
    return false;
});
