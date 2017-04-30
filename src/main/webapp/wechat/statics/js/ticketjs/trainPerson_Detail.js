$(function () {
    // 初始化picker
    $(document).ready(function () {
        var person_arr = ['成人票', '儿童票', '学生票', '军残票'],
            ID_arr = ['港澳通行证', '二代身份证', '台湾通行证', '护照'],
            sex_arr = ['男', '女'];

        // 乘客类型
        $.scrEvent({
            data: person_arr,
            evEle: '#person',
            title: '乘客类型',
            defValue: '成人票',
            afterAction: function (data) {
                var code;
                if (data === '成人票') {
                    code = '1';
                } else if (data === '儿童票') {
                    code = 'C';
                } else if (data === '学生票') {
                    code = 'G';
                } else {
                    code = 'B';
                }
                $("#person").data('code', code).val(data).change();
            }
        });

        // 证件类型
        $.scrEvent({
            data: ID_arr,
            evEle: '#ID',
            title: '证件类型',
            defValue: '二代身份证',
            afterAction: function (data) {
                var code;
                if (data === '二代身份证') {
                    code = '1';
                } else if (data === '港澳通行证') {
                    code = 'C';
                } else if (data === '台湾通行证') {
                    code = 'G';
                } else {
                    code = 'B';
                }
                $("#ID").data('code', code).val(data).change();
            }
        });

        // 性别类型
        $.scrEvent({
            data: sex_arr,
            evEle: '#sex',
            title: '性别',
            defValue: '男',
            afterAction: function (data) {
                var code;
                if (data === '男') {
                    code = 'M';
                } else {
                    code = 'F';
                }
                $("#sex").data('code', code).val(data);
            }
        });

        // 出生日期
        var nowYear = new Date().getFullYear();
        $.dateSelector({
            evEle: '#date',
            startYear: String(nowYear - 100),
            endYear: String(nowYear),
            timeBoo: false,
            afterAction: function (d1, d2, d3) {
                $('#date').val(d1 + '-' + d2 + '-' + d3);
            }
        });

        // 切换车票类型
        $('#person').change(function () {
            if ($(this).val() === '儿童票') {
                $('.child_titcket').removeClass('hide');
            } else {
                $('.child_titcket').addClass('hide');
            }
        });

        // 切换证件类型
        $('#ID').change(function () {
            if ($(this).val() !== '二代身份证') {
                $('#date_type').removeClass('hide');
            } else {
                $('#date_type').addClass('hide');
            }
        });
    });

    var ticket = $.fn.ticket;

    // 验证身份证号码有效性的方法
    function issfz(value) {
        var card_number = value.toLowerCase();
        var aCity = {
            11: "北京",
            12: "天津",
            13: "河北",
            14: "山西",
            15: "内蒙古",
            21: "辽宁",
            22: "吉林",
            23: "黑龙江",
            31: "上海",
            32: "江苏",
            33: "浙江",
            34: "安徽",
            35: "福建",
            36: "江西",
            37: "山东",
            41: "河南",
            42: "湖北",
            43: "湖南",
            44: "广东",
            45: "广西",
            46: "海南",
            50: "重庆",
            51: "四川",
            52: "贵州",
            53: "云南",
            54: "西藏",
            61: "陕西",
            62: "甘肃",
            63: "青海",
            64: "宁夏",
            65: "新疆",
            71: "台湾",
            81: "香港",
            82: "澳门",
            91: "国外"
        };
        if (!/^\d{17}(\d|x)$/i.test(card_number)) {
            //身份证不能为空
            return false;
        } else {
            card_number = card_number.replace(/x$/i, "a");
            if (aCity[parseInt(card_number.substr(0, 2))] === null) {
                //你的身份证地区非法
                return false;
            } else {
                var sBirthday = card_number.substr(6, 4) + "-" + Number(card_number.substr(10, 2)) + "-" + Number(card_number.substr(12, 2));
                var d = new Date(sBirthday.replace(/-/g, "/"));
                if (sBirthday !== (d.getFullYear() + "-" + (d.getMonth() + 1) + "-" + d.getDate())) {
                    //身份证上的出生日期非法
                    return false;
                } else {
                    var iSum = 0;
                    for (var i = 17; i >= 0; i--) {
                        iSum += (Math.pow(2, i) % 11) * parseInt(card_number.charAt(17 - i), 11);
                    }
                    if (iSum % 11 !== 1) {
                        //你输入的身份证号非法
                        return false;
                    }
                    return true;
                }
            }
        }
    }

    //护照验证
    function isPassport(value) {
        var c = /\S{8,14}/;
        return c.test(value);
    }

    //验证港澳通行证
    function isHkongMacao(value) {
        var a = /\S{9,12}/;
        return a.test(value);
    }

    //验证台胞证
    function isTaiw(value) {
        var d = /\d{8,11}/;
        return d.test(value);
    }

    // 验证手机号码
    function checkMobile(str) {
        var re = /^1\d{10}$/;
        if (re.test(str)) {
            return true;
        } else {
            return false;
        }
    }

    // 渲染表单
    if (sessionStorage.info) {
        var data = ticket.storage.get('info'),
            id_type = data.passenger_id_type_name;
        $('#username').val(data.passenger_name);
        $('#person').val(data.passenger_type_name + '票').change();
        $('#ID').val(id_type).change();
        if (id_type !== '二代身份证') {
            $('#date').val('2017-01-01');
            $('#sex').val('男');
        }
        $('#NO').val(data.passenger_id_no);
        $('#phone').val(data.mobile_no);
    }

    // 提交表单
    $('#submit').click(function () {
        var pass = false,
            name = $('#username').val().trim(),
            phone = $('#phone').val().trim(),
            ticket_type = $('#person').val(),
            card_type = $('#ID').val(),
            card_no = $('#NO').val().trim(),
            birthday = $('#date').val(),
            sex = $('#sex').data('code'),
            ticket = $.fn.ticket;

        switch (card_type) {
            case '二代身份证':
                pass = issfz(card_no);
                break;
            case '港澳通行证':
                pass = isHkongMacao(card_no);
                break;
            case '台湾通行证':
                pass = isTaiw(card_no);
                break;
            case '护照':
                pass = isPassport(card_no);
                break;
        }

        if (!checkMobile(phone)) {
            return $.alert('手机号码输入错误');
        }

        if (!pass) {
            return $.alert('证件号码输入错误');
        }

        if (card_type !== '二代身份证' && birthday === '') {
            return $.alert('请选择出生日期');
        }

        if (sex === '') {
            return $.alert('请选择性别');
        }

        ticket.doAjax({
            url: ticket.url.passengers.add,
            data: {
                passenger_name: name,
                mobile_no: phone,
                sex_code: sex,
                passenger_id_type_code: $('#ID').data('code'),
                passenger_id_no: card_no
//                    passenger_type: $('#person').data('code')
            },
            beforeSend: function () {
                $.showLoading();
            }
        }).done(function (resb) {
            $.hideLoading();
            if (resb.status) {
                ticket.storage.remove(['userinfo', 'passengers']);
                window.location.href = 'trainPerson.html';
            } else {
                $.alert('添加新乘车人失败');
            }
        })
    });
});