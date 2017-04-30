$(function () {
    // 获取联系人信息
    var passengers, ticket = $.fn.ticket;
    if (sessionStorage.passengers && !sessionStorage.sync_passenger) {
        passengers = ticket.storage.get('passengers');
        render_passengers(passengers);
    } else {
        ticket.doAjax({
            url: ticket.url.passengers.get,
            xhrFields: {
                withCredentials: true
            },
            crossDomain: true,
            beforeSend: function () {
                $.showLoading();
            },
            success: function (resb) {
                $.hideLoading();
                if (resb.error === 1 || resb.data.data.noLogin) {
                    ticket.storage.set('refresh', true);
                    ticket.login({
                        username: ticket.storage.get('username'),
                        password: ticket.storage.get('password')
                    });
                } else {
                    passengers = resb.data.data.normal_passengers;
                    ticket.storage.set('passengers', passengers);
                    render_passengers(passengers);
                }
            },
            error: function (xhr) {
                $.hideLoading();
                $.alert(xhr.statusText);
            }
        });
    }

    // 渲染联系人信息
    function render_passengers(data) {
        var userList = ticket.storage.get('userList');
        $.each(data, function (idx) {
            var status, pass = false, id = Math.random(), that = this,
                text = $('#clone_userInfo').clone().text().replace('用户姓名', this.passenger_name).replace(
                    '证件类型', this.passenger_id_type_name).replace(/单选按钮/g, id).replace(/-1/g, idx).replace('证件号码',
                    function () {
                        var code = that.passenger_id_no;
                        switch(that.passenger_id_type_name) {
                            case '二代身份证':
                                code = code.substr(0, 6) + '********' + code.substr(14, 4);
                                break;
                            case '港澳通行证':
                                code = code.substr(0, 5) + '*****';
                                break;
                            case '台湾通行证':
                                code = code.substr(0, 3) + '****' + code.substr(7, 2);
                                break;
                            case '护照':
                                code = code.substr(0, 3) + '****' + code.substr(7, 2);
                                break;
                        }
                        return code;
                    });
            if (this.passenger_id_type_code === '2' || ['94', '96'].indexOf(this.total_times) !== -1) {
                status = '未通过';
            } else {
                if (['95', '97'].indexOf(this.total_times) !== -1) {
                    status = '已通过';
                    pass = true;
                } else if (['93', '99'].indexOf(this.total_times) !== -1) {
                    pass = true;
                    (this.passenger_id_type_code === '1') ? status = '已通过' : status = '预通过';
                } else if (['92', '98'].indexOf(this.total_times) !== -1) {
                    if (this.passenger_id_type_code === "B" ||
                        this.passenger_id_type_code === "H" ||
                        this.passenger_id_type_code === "C" ||
                        this.passenger_id_type_code === "G") {
                        status = '请报验';
                    } else {
                        status = '待核验';
                    }
                } else if (this.total_times === '91') {
                    if (this.passenger_id_type_code === "B" ||
                        this.passenger_id_type_code === "H" ||
                        this.passenger_id_type_code === "C" ||
                        this.passenger_id_type_code === "G") {
                        status = '请报验';
                    }
                } else {
                    status = '请报验';
                }
            }
            $('dl').append(text.replace('状态', status).replace('status-normal', pass ? 'status-access' : 'status-deny'));

            if (userList) {
                $.each(userList, function () {
                    if (this.passenger_name === that.passenger_name) {
                        $('label[for="' + id + '"]').click();
                    }
                });
            }
        });
    }

    // 页面跳转
    $('#submit').click(function () {
        var url;
        if (ticket.storage.get('isGeabTicket')) {
            url = 'grabTicket.html';
        } else {
            url = 'trainBook.html';
        }
        ticket.storage.set('userList',
            $.map($(':checked'), function (input) {
                return passengers[$(input).data('idx')];
            }));
        window.location.href = url;
    });

    // 修改用户信息
//        $(document).on('click', '.icon-bianji', function () {
//            ticket.set('info', passengers[$(this).data('idx')])
//            window.location.href = 'trainPerson_Detail.html';
//        });

    // 核验信息
    $('#verification').click(function () {
        $('.alert').show();
    });

    $('.train_mask').click(function () {
        $('.alert').hide();
    });
});