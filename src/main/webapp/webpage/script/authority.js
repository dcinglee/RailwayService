var $tbody = $("#sample-table-1 tbody"),
    authority = [];
getParentAuthority();
// 初始化table
$("#authority_table").bootstrapTable({
    'url': '../authority/query',
    'toolbar': $('div[data-name="btnCtl"]'),
    // 'toolbarAlign': 'right',
    'queryParams': function (params) {
        params["name"] = $("#inputName").val();
        params["description"] = $("#inputDescription").val();       
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
        $("#authority_table").bootstrapTable('uncheckBy', {
            field: 'stationId',
            values: [$("#authority_table").bootstrapTable('getSelections')[0].stationId]
        });
    },
    'columns': [
        {
            'checkbox': true,
            
        },
        {
            'field': 'authorityId',
            'title': 'ID',
            'align': 'center',
            'class': 'hide'
        },
        {
            'field': 'name',
            'title': '权限名称',
            'align': 'center'
        },
        {
            'field': 'description',
            'title': '权限描述',
            'align': 'center'
        },
        {
            'field': 'code',
            'title': '权限码',
            'align': 'center',
            'sortable': true
        },
        {
            'field': 'icon',
            'title': '图标',
            'align': 'center',
            'class': 'hide'
        },
        {
            'field': 'status',
            'title': '权限状态',
            'align': 'center',
            'formatter': function (status) {
                if (status == '0') {
                    status = '开启';
                } else if (status == '1') {
                    status = '关闭';
                }
                return status;
            }
        },
        {
            'field': 'parentId',
            'title': '父权限',
            'align': 'center',
            'sortable': true,
            'formatter': function (parentId) {
                if (authority != null) {
                    if (typeof(authority.data) != 'undefined') {
                        for (var i = 0; i < authority.data.length; i++) {
                            if (authority.data[i].authorityId == parentId) {
                                return authority.data[i].name;
                            } else if (parentId == null || parentId == "") {
                                return "无父权限";
                            }
                        }
                    }
                }
                return "";
            }
        },
        {
            'field': 'orderNo',
            'title': '排序顺序',
            'align': 'center'
        },
        {
            'field': 'type',
            'title': '权限类型',
            'align': 'center',
            'formatter': function (type) {
                if (type == '0') {
                    type = '菜单';
                } else if (type == '1') {
                    type = '按钮';
                }
                return type;
            }
        },
        {
            'field': 'createDate',
            'title': '创建日期',
            'align': 'center'
        }
    ]
});
/**
 * 权限查询ajax
 */
function authorityQuery(name, description) {
	if(($("#inputName").val()== '')&&($("#inputDescription").val())== ''){
		$('#authority_table').bootstrapTable('refreshOptions',{pageNumber:1,pageSize:globalPageSize});
	}else{
    $.ajax({
        type: "POST",
        url: "../authority/query",
        data: {
            "name": name,
            'description': description,
            "offset": 0,
            "limit": globalPageSize
        },
        dataType: 'json',
        'sidePagination': 'server',
        success: function (resb) {
            if (resb.data.length != 0) {
                $("#authority_table").bootstrapTable('load', {
                    'total': resb.data.total,
                    'rows': resb.data.rows,
                });
            }
            else {
                $("#authority_table").bootstrapTable('removeAll');
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
    return authorityQuery($("#inputName").val(), $("#inputDescription").val());
});

/**
 * 权限操作(新增，更新，删除)
 * @param action [add/update/remove]
 * @param data
 * @param async/scync [default async]
 */
function optionAuthority(action, data, async) {
    var url = null;
    (async === false) ? async = false : async = true;
    if (action === 'add') {
        url = '../authority/add';
    } else if (action === 'update') {
        url = '../authority/update';
    } else {
        url = '../authority/delete';
    }
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
};

/**
 * 新增模态框显示
 */
$("#addModal").on('show.bs.modal', function () {
    $(this).find('input[type=text]').val("");
})

/**
 * 删除模态框显示
 */
$("#deleteModal").on('show.bs.modal', function () {
    var name = $("#authority_table").bootstrapTable('getSelections')[0].name,
        html = '你确定要删除此权限: <span>' + name + '</span>';
    $(this).find('div.modal-body').empty().append(html);
});

/**
 * 更新模态框显示
 */
$("#updateModal").on('show.bs.modal', function () {
    var $that = $(this),
        $inputs = $that.find("input[type=text]"),
        $status = $(this).find('select[name="status"]'),
        $parentId = $(this).find('select[name="parentId"]'),
        $orderNo = $(this).find('select[name="orderNo"]'),
        $type = $(this).find('select[name="type"]'),
        tableData = $("#authority_table").bootstrapTable("getSelections")[0];
    $inputs[0].value = tableData.authorityId;
    $inputs[1].value = tableData.name;
    $inputs[2].value = tableData.description;
    $inputs[3].value = tableData.menuUrl;
    $inputs[4].value = tableData.code;
    $inputs[5].value = tableData.icon;
    $status.val(tableData.status);
    $parentId.val(tableData.parentId);
    $orderNo.val(tableData.orderNo);
    $type.val(tableData.type);
});

/**
 * 新增,更新,删除角色表单提交
 */
$("#addAuthorityForm,#updateAuthorityForm,#deleteAuthorityForm").submit(function () {
    var type = $(this).data("type"),
        $modal = $("#" + type + "Modal"),
        $inputs = $(this).find('input[type="text"]'),
        $status = $(this).find('select[name="status"]'),
        $parentId = $(this).find('select[name="parentId"]'),
        $orderNo = $(this).find('select[name="orderNo"]'),
        $type = $(this).find('select[name="type"]'),
        tableData = $("#authority_table").bootstrapTable("getSelections")[0],
        data = null,
        $that = $(this);

    // 模式选择,设置不同的data和url
    if (type === 'delete') {
        data = {
            'authorityId': tableData.authorityId
        }
    } else if (type === 'add') {
    	var obj = jQuery("#addAuthorityForm").validationEngine('validate', {promptPosition : "centerRight", autoPositionUpdate : true});
    	if(!obj){
    		return false;
    	};
        data = {
            // 'authorityId': $inputs[0].value,
            'name': $inputs[0].value,
            'description': $inputs[1].value,
            'menuUrl': $inputs[2].value,
            'code': $inputs[3].value,
            'icon': $inputs[4].value,
            'status': $status.val(),
            'parentId': $parentId.val(),
            'orderNo': $orderNo.val(),
            'type': $type.val()
        }
    } else if (type === 'update') {
        data = {
            'authorityId': $inputs[0].value,
            'name': $inputs[1].value,
            'description': $inputs[2].value,
            'menuUrl': $inputs[3].value,
            'code': $inputs[4].value,
            'icon': $inputs[5].value,
            'status': $status.val(),
            'parentId': $parentId.val(),
            'orderNo': $orderNo.val(),
            'type': $type.val()
        }
    }

    $modal.modal('hide');
    // 完成ajax后刷新表格数据
    optionAuthority(type, data).done(function () {
        $("#authority_table").bootstrapTable("refresh");
        $that[0].reset();
    });

    // 阻止表格冒泡事件
    return false;
});

/**
 * 查询父权限
 */
function getParentAuthority() {
    return $.ajax({
        type: "POST",
        url: '../authority/getParentAuthority',
        //		             data:"",
        error: function (request) {
            alert(request);
        },
        success: function (data) {
            var rs = eval("(" + data + ")");
            if (rs.success) {
                for (var i = 0; i < rs.data.length; i++) {
                    $(".selectFatherAuthority").append("<option value=" + rs.data[i].authorityId + ">" + rs.data[i].name + "</option>");
                }
                ;
                authority = rs;
                // authorityQuery();
            }
            ;
        }
    });
};

//
//
// /**
//  * 新增权限函数
//  */
// function addAuthority() {
//     $.ajax({
//         type: "POST",
//         url: '../authority/add',
//         data: $("#addAuthorityForm").serializeJson(),
//         error: function (request) {
//             alert(request);
//         },
//         success: function (data) {
//             console.log(data);
//             var rs = eval("(" + data + ")");
//             if (rs.success) {
//                 $("#user-info-name").after(rs.data);
//                 $('#addModal').modal('hide');
// //		             		$("#addSuccessModal").modal('show');
// //		             		$("#addSuccessAffirm").on("click",function(){
// //		             			$("#addSuccessModal").modal('hide');
// //		             		});
//                 alert("添加成功！！");
// //							authorityQuery();
//                 getParentAuthority();
//             } else {
//                 alert("新增权限失败，请确认信息是否有误！！");
//             }
//             ;
//         }
//     });
// };
//
// /**
//  * 更新权限函数
//  */
// function updataAuthority() {
//     $.ajax({
//         type: "POST",
//         url: '../authority/update',
//         data: $("#updataAuthority").serializeJson(),
//         error: function (request) {
//             alert(request);
//         },
//         success: function (data) {
//             var rs = eval("(" + data + ")")
//             if (rs.success) {
//                 $("#user-info-name").after(rs.data);
//                 $("#updateModal").modal('hide');
// //		             		$("#updataSuccessModal").modal('show');
// //		             		$("#updataSuccessAffirm").on("click",function(){
// //		             			$("#updataSuccessModal").modal('hide');
// //		             		});
//                 alert("修改成功！！");
// //		             		authorityQuery();
//                 getParentAuthority()
//             } else {
//                 alert(rs.message);
//             }
//             ;
//         }
//     });
// };
//
// /**
//  * 删除权限函数
//  */
// function deleteAuthority(id) {
//     $.ajax({
//         type: "POST",
//         url: '../authority/delete',
//         data: "id=" + id,
//         error: function (request) {
//             alert(request);
//         },
//         success: function (data) {
//             var rs = eval("(" + data + ")")
//             if (rs.success) {
//                 $("#deleteModal").modal('hide');
//                 authorityQuery();
//             }
//             ;
//         }
//     });
// };
//
//
// /**
//  * 所有权限查询函数
//  */
// function authorityQuery(data) {
//     return $.ajax({
//         type: "POST",
//         url: " ../authority/query ",
//         data: data,
//         dataType: 'json',
//         contentType: "application/x-www-form-urlencoded; charset=utf-8",
//         beforeSend: function () {
//         },
//         success: function (data) {
//             console.log(data)
//             var $tbody = $("#sample-table-1 tbody");
//             if (data.data.rows.length != 0) {
//                 //将类型转换成具体描述
//                 var type1 = {"0": "菜单", "1": "按钮", "2": "其他", "null": " "};
//                 //将状态转化为具体描述
//                 var status1 = {"0": "开启", "1": "关闭", "null": " "};
//                 //获取父权限并放入对象中
//                 var fatherAuthority = {};
//                 for (var i = 0; i < authority.data.length; i++) {
//                     fatherAuthority[authority.data[i].authorityId] = authority.data[i].name;
//                 }
//                 ;
//                 fatherAuthority[0] = "无父权限";
//                 console.log(fatherAuthority)
//                 $tbody.empty();
//                 $.each(data.data.rows, function () {
//                     //克隆模板并取得其HTML
//                     var html = $("#cloneTR").clone().html();
//                     var str = html.replace("1", this.authorityId);
//                     //替换相关信息
//                     if (this.description == "null") {
//                         str = str.replace("超级管理员", " ");
//                     } else {
//                         str = str.replace("超级管理员", this.description);
//                     }
//                     ;
//                     str = str.replace("张三", this.name);
//                     if (this.menuUrl == null) {
//                         str = str.replace("url", " ");
//                     } else {
//                         str = str.replace("url", this.menuUrl);
//                     }
//                     ;
//                     if (this.code == null) {
//                         str = str.replace("广东省", " ");
//                     } else {
//                         str = str.replace("广东省", this.code);
//                     }
//                     ;
//                     str = str.replace("深圳市", status1[this.status]);
//                     if (this.parentId == null || this.parentId == "") {
//                         str = str.replace("父权限", "无父权限");
//                     } else {
//                         str = str.replace("父权限", fatherAuthority[this.parentId]);
//                     }
//                     ;
//                     if (this.orderNo == null) {
//                         str = str.replace("深圳市", " ");
//                     } else {
//                         str = str.replace("深圳市", this.orderNo);
//                     }
//                     ;
//
//                     str = str.replace("正常", type1[this.type]);
//                     if (this.createDate == null) {
//                         str = str.replace("2017-02-14", " ");
//                     } else {
//                         str = str.replace("2017-02-14", this.createDate);
//                     }
//                     ;
//                     $tbody.append(str);
//
//                 });
//                 // var myRows = $('table tr').click(function(){
//                 //        $(this).css({"backgroundColor":"#FF0000"});
//                 //        console.log($(this).children('td'));
//                 //    });
//             } else {
//                 $tbody.empty();
//                 $tbody.append("<tr class='no-records-found'><td colspan='9' style='text-align:center'>没有找到匹配的记录</td></tr>");
//             }
//             ;
//         }
//     });
// };

/*
 * 通过ID查询权限
 */
// function authorityGet(obj){
// 	$.ajax({
// 		type: "POST",
// 		url: " ../authority/get ",
// 		data:obj,
// 		dataType:'json',
// 		beforeSend: function () {
// 		},
// 		success: function(data) {
// 			if (data.data) {
//                       var $tbody = $("#sample-table-1 tbody");
//                       $tbody.empty();
//                       var type1 = {"0":"菜单","1":"按钮","2":"其他","null":" "};
//                       var status1 = {"0":"开启","1":"关闭","null":" "};
//                       var fatherAuthority ={};
// 				for(var i = 0;i<authority.data.length;i++){
// 					fatherAuthority[authority.data[i].authorityId]=authority.data[i].name;
// 				};
//                       var html = $("#cloneTR").clone().html();
//                       var str = html.replace("超级管理员", data.data.description);
//                       str = str.replace("张三", data.data.name);
//                       str = str.replace("1",data.data.authorityId);
//                       if(data.data.menuUrl==null){
//                       		str = str.replace("url"," ");
//                       	}else{
//                       		str = str.replace("url",data.data.menuUrl);
//                       	};
//                       if(data.data.code==null){
//                       		str = str.replace("广东省"," ");
//                       	}else{
//                       		str = str.replace("广东省",data.data.code);
//                       	};
//                       str = str.replace("深圳市",status1[data.data.staus]);
//                       if(data.data.parentId==null||data.data.parentId==""){
//                       	str = str.replace("父权限","无父权限");
//                       }else{
// 					str = str.replace("父权限",fatherAuthority[data.data.parentId]);
//                       };
//                       if(data.data.orderNo==null){
//                       		str = str.replace("深圳市"," ");
//                       	}else{
//                       		str = str.replace("深圳市",data.data.orderNo);
//                       	};
//                       str = str.replace("正常",type1[data.data.type]);
//                       if(data.data.createDate==null){
//                       	str = str.replace("2017-02-14"," ");
//                       }else{
//                       	str = str.replace("2017-02-14",data.data.createDate);
//                       };
//                       $tbody.append(str);
//                }else{
//                	alert("查询失败，请确认查询信息是否有误！！！");
//                };
// 		}
// 	});
// }

/*
 * 序列化表单
 */
// $.fn.serializeJson = function () {
//     var serializeObj = {};
//     var array = this.serializeArray();
//     var str = this.serialize();
//     $(array).each(function () {
//         if (serializeObj[this.name]) {
//             if ($.isArray(serializeObj[this.name])) {
//                 serializeObj[this.name].push(this.value);
//             } else {
//                 serializeObj[this.name] = [serializeObj[this.name], this.value];
//             }
//         } else {
//             serializeObj[this.name] = this.value;
//         }
//     });
//     return serializeObj;
// };
