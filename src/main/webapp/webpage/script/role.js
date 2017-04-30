var $tbody = $("#sample-table-1 tbody"),
    zTreeObj,
    authority = [];

// 初始化table
$("#role_table").bootstrapTable({
    'url': '../role/query',
    'toolbar': $('div[data-name="btnCtl"]'),
    'striped': true,
    'pagination': true,
    'pageSize': globalPageSize,
    'pageList': [],
    'clickToSelect': true,
    'singleSelect': true,
    'smartDisplay': true,
    'onCheck': function () {
        $("button[data-target='#updateModal'], button[data-target='#deleteModal']").removeAttr("disabled");
    },
    'onUncheck': function () {
        $("button[data-target='#updateModal'], button[data-target='#deleteModal']").attr("disabled", 'true');
    },
    'onPageChange': function () {
        if ($("#role_table").bootstrapTable('getSelections')[0]) {
            $("#role_table").bootstrapTable('uncheckBy', {
                field: 'roleId',
                values: [$("#role_table").bootstrapTable('getSelections')[0].roleId]
            });
        }
    },
    'columns': [
        {
            'checkbox': true,
           // 'class': 'hide'
        },
        {
            'field': 'name',
            'title': '角色',
            'align': 'center',
            'sortable': true
        },
        {
            'field': 'createDate',
            'title': '创建时间',
            'align': 'center'
        }
        ]
});


/**
 * 角色查询ajax
 * @param name 请求查询的角色名
 */
function roleQuery(name) {
	if(($("#inputStation").val()== '')){
		$('#role_table').bootstrapTable('refreshOptions',{pageNumber:1,pageSize:globalPageSize});
	}else{
    $.ajax({
        type: "POST",
        url: "../role/getRoleByName",
        data: {
            "name": name
        },
        dataType: 'json',
        success: function (resb) {
            if (resb.data.length != 0) {
                $("#role_table").bootstrapTable('load', resb.data);
            } else {
                $("#role_table").bootstrapTable('removeAll');
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
 *               [delete 数据结构 {roleId: int}]
 * @param async/scync [default async]
 */
function roleManagement(action, data, async) {
    var url = null;
    (async === false) ? async = false : async = true;
    if (action === 'add') {
        url = '../role/add';
    } else if (action === 'update') {
        url = '../role/update';
    } else {
        url = '../role/delete';
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
 * 查询所有权限
 * @param authority array, 所有权限
 */
function authorityQuery() {
    return $.ajax({
        type: "POST",
        url: "../authority/queryAuthorityTree",
        dataType: 'json',
        success: function (data) {
            if (data.total != 0) {
                return authority = data.data;
            } else {
                return;
            }
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            alert(textStatus);
            return;
        }
    });
};

/**
 * 查询当前id的最新权限
 * @param  {[int]} id [角色id]
 * @return {[array]}  [权限列表]
 */
function getAuthorityByRoleId(id) {
    return $.ajax({
        type: "POST",
        url: "../role/getAuthorityByRoleId",
        dataType: 'json',
        data: {
            'roleId': id
        },
        async: false,
        success: function (resb) {
            return resb;
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            alert(textStatus);
            return {};
        }
    });
}

/**
 * 查询表单提交
 */
$("#stationFormId").submit(function () {
    return roleQuery($("#inputStation").val());
});


/**
 * 动态渲染权限选择(zTree版)
 * @param  {[element]} elem [添加元素的对象($(div))]
 */
function autoRenderAuth(elem) {
    var html = null,
        $elem = $(elem).find('ul.ztree');
    $elem.empty();

    // 初始化zTree
    return authorityQuery().done(function () {
        var setting = {
                'check': {
                    'enable': true,
                    'chkboxType': {
                        "Y": "p",
                        "N": "ps"
                    }
                }
            }, // 开启chkbox, enable时选父不关联子, disabled时父关联子
            zNodes = [];

        // 遍历所有权限, 组成zTree需要的json格式
        $.each(authority, function () {
            var nodes = {
                'name': this.name,
                'id': this.authorityId
            }
            if (this.subMenus) {
                nodes['open'] = true;
                nodes['children'] = [];
                $.each(this.subMenus, function () {
                    nodes['children'].push({
                        'name': this.name,
                        'id': this.authorityId
                    });
                })
            }
            ;
            zNodes.push(nodes)
        });

        // 初始化
        zTreeObj = $.fn.zTree.init($elem, setting, zNodes);
    });
}

/**
 * 新增模态框显示
 */
$("#addModal").on('show.bs.modal', function () {
    autoRenderAuth($(this).find('div#perms'));

    $(this).find('input[type=text]').val("");
})

/**
 * 删除模态框显示
 */
$("#deleteModal").on('show.bs.modal', function () {
    var name = $("#role_table").bootstrapTable('getSelections')[0].name,
        html = '你确定要删除角色: <span>' + name + '</span>?';
    $(this).find('div.modal-body').empty().append(html);
});

/**
 * 更新模态框显示
 */
$("#updateModal").on('show.bs.modal', function () {
    var $that = $(this),
        $inputs = $that.find("input[type=text]"),
        tableData = $("#role_table").bootstrapTable("getSelections")[0],
        roleAuth = {};

    // 设置角色名、描述
    $inputs[0].value = tableData.name;
    $inputs[1].value = tableData.description;

    // 获取所有最新权限
    autoRenderAuth($("#updatePerms")).done(function () {
        try {
            roleAuth = getAuthorityByRoleId(tableData.roleId).responseJSON.data;
        } catch (e) {
            console.log(e);
            return false;
        }

        // 获取当前角色的最新权限
        // 遍历所有权限和当前用户权限, 在相同的权限上enable
        (function loop(node) {
            $.each(node, function () {
                if (this.children) {
                    loop(this.children);
                }

                var that = this;
                $.each(roleAuth, function () {
                    if (that.id == this.authorityId) {
                        zTreeObj.checkNode(that, true, false);
                    }
                });
            });
        })(zTreeObj.getNodes());
    });
});

/**
 * 新增,更新,删除角色表单提交
 */
$("#addForm,#updateForm,#deleteForm").submit(function () {
    var type = $(this).data("type"),
        $modal = $("#" + type + "Modal"),
        $inputs = $(this).find('input[type="text"]'),
        tableData = $("#role_table").bootstrapTable("getSelections")[0],
        data = null,
        $that = $(this);

    // 模式选择,设置不同的data和url
    if (type === 'delete') {
        data = {
            'roleId': tableData.roleId
        }
    } else {
        var listAuthorityId = [];
        $.each(zTreeObj.getCheckedNodes(true), function () {
            listAuthorityId.push(this.id);
        });
        if (listAuthorityId.length == 0) {
            alert("请选择权限");
            return false;
        }
        ;
        data = {
            'name': $inputs[0].value,
            'description': $inputs[1].value,
            'listAuthorityId': listAuthorityId
        }
        if (type === 'update') {
            data['roleId'] = tableData.roleId;
        }
        ;

        ;
    }
    ;

    $modal.modal('hide');
    // 完成ajax后刷新表格数据
    roleManagement(type, data).done(function () {
        $("#role_table").bootstrapTable("refresh");
        $that[0].reset();
    });

    // 阻止表格冒泡事件
    return false;
});
