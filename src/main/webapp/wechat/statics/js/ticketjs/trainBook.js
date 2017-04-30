$(function () {
        'use strict';

        // 渲染车次信息
        var ticket = $.fn.ticket,
            infos = ticket.storage.get('infos'),
            paras = ticket.storage.get('paras');

        $('#dateTime').text(infos.startDate.replace('-', '年').replace('-', '月') + '日');
        $('#week').text(infos.week);
        $('#start_station').text(paras.from_station_name);
        $('#start_time').text(paras.start_time);
        $('#line').text(paras.station_train_code);
        $('#end_station').text(paras.to_station_name);
        $('#end_time').text(paras.arrive_time);

        // 获取用户12306信息
        var userInfo = ticket.storage.get('userInfo'),
            userInfo = {
                "userId": "8a9d4f085afa1705015afa2383400024",
                "subscribeType": 1,
                "openid": "ooD1YwTQBJ3eApmj7JLA0q45IQUY",
                "nickName": "秀秀",
                "country": "长沙",
                "province": "湖南",
                "city": "长沙",
                "language": "zh_CN",
                "headimgUrl": "http://wx.qlogo.cn/mmopen/RpVQCM0220KggM435diam0icZmVs2Dy8QxhsMG6E3Chur3RaQGSEcOCB6ktgkfvFwICsKBgRLFo1dzCYicPT3QibexdciaxiaA7HRib/0",
                "subscribeTime": "1490255315",
                "unionid": null,
                "groupId": "0",
                "passWord": null,
                "name": "秀秀",
                "gender": 1,
                "phoneNo": "18229899455",
                "createDate": "2017-03-23"
            };
        if (userInfo) {
            ticket.doAjax({
                //TODO 用户信息
                url: '/RailwayService/passenger/getKyfwByUser?userId=' + userInfo.userId,
                success: function (resb) {
                    if (resb.success) {
                        $('#bound').hide();
                        $('#logged,#modify,.weui-btn-area').removeClass('hide');
                        ticket.storage.set('passengers', resb.data.passengers);
                        ticket.storage.set('username', resb.data.userName);
                        ticket.storage.set('password', resb.data.passWord);
                    } else {
                        ticket.storage.remove(['userList', 'passengers']);
                    }
                }
            });
        }

        // 12306登录弹窗
        $('#bound,#logged').click(function () {
            $('section').append($("#clone_12306").clone().text());
            setTimeout(function () {
                $('#login').addClass('login-show');
            }, 100);
        });

        $(document)
        // 关闭12306登录弹窗
            .on('click', '.icon-guanbi1', function () {
                $('#login').removeClass('login-show');
                $('.train-mask').fadeOut('fast', function () {
                    $('.train-mask,#login').remove();
                });
            })

            // 绑定12306帐号
            .on('click', '#a_login_12306', function () {
                $('.icon-guanbi1').click();
                sessionStorage.setItem('username', $('#account').val());
                sessionStorage.setItem('password', $('#pwd').val());
                ticket.login({
                    username: $('#account').val(),
                    password: $('#pwd').val()
                });
            })

            // 删除乘车人信息
            .on('click', 'i.icon-jianshao', function () {
                $(this).parents('.userinfo').remove();
                userList.splice(Number($(this).data('idx')), 1);
                ticket.storage.set('userList', userList);
            });

        // 渲染乘车人信息
        var userList = ticket.storage.get('userList');
        if (userList) {
            $.each(userList, function (idx) {
                $('#modify').before($('#clone_userInfo').clone().text().replace('乘客姓名', this.passenger_name).replace(
                    '车票类型', this.passenger_type_name + '票').replace('证件号码', this.passenger_id_no).replace(
                    '座位类型', infos.seatStyle).replace('车票单价', infos.price).replace(/-1/g, idx));
            });
            if (userList.length !== 0) {
                $('.weui-btn-area').removeClass('hide');
            }
        }

        // 乘车人详情页跳转
        $('#modify').click(function () {
            window.location.href = 'trainPerson.html';
        });

        var submit = false;
        // 表单提交
        $('#submit').click(function () {
            $.showLoading('订票中......');
            if (!submit) {
                ticket.doAjax({
                    url: ticket.url.submit_order,
                    data: {traininfo: JSON.stringify(ticket.storage.get('data')[ticket.storage.get('ind')])},
                    success: function (resb) {
                        submit = true;
                        check_order();
                    },
                    error: function (xhr) {
                        console.log(xhr);
                        $.alert(xhr.statusText);
                        submit = false;
                    },
                    complete: function () {
                        $.hideLoading();
                    }
                });
            } else {
                check_order();
            }
        });

        // 订票
        function check_order() {
            var data = $.map(userList, function (obj) {
                obj['seat_type'] = infos.seatStyle_code;
                return obj;
            });
            return ticket.doAjax({
                url: "http://thisa:8888/check_order",
                data: {passengers: JSON.stringify(data)},
                success: function (resb) {
                    $.hideLoading();
                    ticket.storage.remove(['userList']);
                    window.location.href = 'bookSuccess.html';
                },
                error: function (xhr) {
                    $.hideLoading();
                    $.alert(xhr.statusText);
                }
            });
        }
    }
);