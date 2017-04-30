/**
 * Created by xz on 2017/4/17.
 */
(function () {
    //获取base中的方法
    var ticket = $.fn.ticket;
    //请求userid
    var openid = "ooD1YwcJJwcWxaY3ncgfeXNTMFpE";
    var url = "http://desktop-a48j4op:8080/RailwayService/";
    ticket.doAjax({
       url:"/RailwayService/entrance/test?openid="+openid+"&redirectUrl="+url,
       success:function (resb) {
           console.log(resb);
       }
    });

    //请求用户数据
    // var userId = ticket.storage.get("userInfo").userId;
    var userInfo = {
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
    if(sessionStorage.username){
        $('#bound,#logged').toggleClass('hide');
        $('#modify>dd.weui-flex__item').toggleClass('color-orange');
    }else{
        ticket.doAjax({
            url: '/RailwayService/passenger/getKyfwByUser?userId='+userInfo.userId,
            success: function (resb) {
                if (resb.success==true) {
                    $('#bound,#logged').toggleClass('hide');
                    $('#modify>dd.weui-flex__item').toggleClass('color-orange');
                    ticket.storage.set('passengers', resb.data.passengers);
                    ticket.storage.set('username', resb.data.userName);
                    ticket.storage.set('password', resb.data.passWord);
                }else{
                    console.log(resb);
                }
            }
        });
    };

    //获取页面临时保存的信息
    if(sessionStorage.toPreTrain){
        var obj = JSON.parse(sessionStorage.toPreTrain);
        $('#trainNum_type').find('p span').html(obj.trainNo);
        $('#set_type').find('p span').html(obj.seatType);
        $('#ticketsDate').find('p span').html(obj.trainDay);
        $('.input-control').val(obj.phone);
        sessionStorage.removeItem("toPreTrain");
    };
    if(sessionStorage.isGeabTicket){
        var obj = JSON.parse(sessionStorage.isGeabTicket);
        $('#trainNum_type').find('p span').html(obj.trainNo);
        $('#set_type').find('p span').html(obj.seatType);
        $('#ticketsDate').find('p span').html(obj.trainDay);
        $('.input-control').val(obj.phone);
        sessionStorage.removeItem("isGeabTicket");
    };
    if(sessionStorage.trainNo){
        $("#trainNum_type p span").text(sessionStorage.trainNo);
        sessionStorage.removeItem("trainNo");
    };

    //弹窗的出现与隐藏
    $('#show_login').on('click',function(){
        $('#login').show();
        $('#login').animate({'opacity':100,'marginTop':-16},400,function(){
            $('.grab_tickets').hide();
        });
    });

    function close_login(){
        $('.grab_tickets').show();
        $('#login').animate({'opacity':0,'marginTop':1000},400,function(){
            $('#login').hide();
        });
    }

    $('#to_login').on('click',function(){
        close_login();
        $('#show_login .hide').removeClass('hide');
        $('.no_login').addClass('hide');
    });

    $("#set_type").on('click',function () {
        show_content('set_show');
    });
    $("#ticketsDate").on('click',function () {
        show_content('date_show');
    });

    $(".seatCancle").on('click',function () {
        set_close('set_show');
    });

    $(".seatConfirm").on('click',function () {
        set_ok('set_show','set_type');
    });

    $(".dayCancle").on('click',function () {
        set_close('date_show');
    });

    $(".dayConfirm").on('click',function () {
        set_ok('date_show','ticketsDate');
    });

    function show_content(className){
//        $('.'+className).find('.weui-mask').addClass('clear-mask');
        $('.'+className).find('.weui-mask').addClass('weui-animate-fade-in').addClass('clear-mask');
        $('.'+className).find('.weui-picker').addClass('weui-animate-slide-up');
    }

    function set_ok(className,id){
        var arr='';
        var l=false;
        $('.'+className+' input').each(function(i,obj){
            if($(this).is(':checked')){
                if(l){
                    arr+=','+$(this).parent('.ft_right').prev('.txt_left').text();
                }else{
                    arr+=$(this).parent('.ft_right').prev('.txt_left').text();
                    l=true;
                }
            }
        });
        if(arr!=null && arr!=''){
            $('#'+id+' .weui-cell__bd span').html(arr);
        }else{
            $('#'+id+' .weui-cell__bd span').html("提高抢票成功率");
        }
        set_close(className);
    }

    function set_close(className){
        $('.'+className+' .weui-mask').removeClass('clear-mask');
        $('.'+className+' .weui-mask').removeClass('weui-animate-fade-in');
        $('.'+className+' .weui-picker').removeClass('weui-animate-slide-up');
    };

    //解决安卓弹出键盘问题
    var oHeight = $(document).height(); //屏幕当前的高度
    $(window).resize(function () {
        if ($(document).height() < oHeight) {
            $("#fix-hegiht").css("display", "block");
        } else {
            $("#fix-hegiht").css("display", "none");
        }
    });

    //12306登录提示
    $('#bound, #switch').click(function () {
        $('section').append($("#clone_12306").clone().text());
        setTimeout(function () {
            $('#login').addClass('login-show');
        }, 100);
    });

    $(document).on('click', '.icon-guanbi1', function () {
        // 关闭12306登录弹窗
        $('#login').removeClass('login-show');
        setTimeout(function () {
            $('.weui-mask').fadeOut('fast', function () {
                $('.weui-mask,#login').remove();
            });
        }, 300);
    }).on('click', '#a_login_12306', function () {
        // 绑定12306帐号
        $('.icon-guanbi1').click();
        // $('#bound,#logged').toggleClass('hide');
        // $('#modify>dd.weui-flex__item').toggleClass('color-orange');
        //12306登录
        sessionStorage.setItem('username', $('#account').val());
        sessionStorage.setItem('password', $('#pwd').val());
        ticket.login({
            username: $('#account').val(),
            password: $('#pwd').val()
        });
    }).on('click','#trainNum_type',function () {
        //跳转到备选车次并将页面数据存入session中
        var data = {
            trainNo   : $('#trainNum_type').find('p span').html(),
            seatType : $('#set_type').find('p span').html(),
            trainDay : $('#ticketsDate').find('p span').html(),
            phone    :  $('.input-control').val()
        };
        sessionStorage.setItem("toPreTrain",JSON.stringify(data));
        window.location.href = "pre_trainNum.html";
    }).on("click",".reduce",function () {
        //删除乘车人
        console.log($(this).parent().parent().index());
        var arr = JSON.parse(sessionStorage.userList);
        arr.splice($(this).parent().parent().index()-2,1);
        sessionStorage.setItem("userList",JSON.stringify(arr));
        $(this).parent().parent().remove();
    }).on("click","#modify",function () {
        //跳转到添加乘车人页面
        var data = {
            trainNo   : $('#trainNum_type').find('p span').html(),
            seatType : $('#set_type').find('p span').html(),
            trainDay : $('#ticketsDate').find('p span').html(),
            phone    :  $('.input-control').val()
        };
        sessionStorage.setItem("isGeabTicket",JSON.stringify(data));
        window.location.href = "trainPerson.html";
    });

   // 获取session中的值
    var infos = JSON.parse(sessionStorage.infos),
          paras  = JSON.parse(sessionStorage.paras);
    //获取预定的日期
    var day = infos.startDate;
    //获取当前日期
    var nowDay = getNowFormatDate();
    var timestamp = Date.parse(nowDay);
    var timestamp1 = Date.parse(day);

    var weeks = {'1':'星期一','2':'星期二','3':'星期三','4':'星期四','5':'星期五','6':'星期六','0':'星期天'};
    var myDate = new Date(Date.parse(day.replace(/-/g, "/")));
    var lastDay = Date.parse(JSON.parse(sessionStorage.infos).startDate),
          detract,
          changeDay,
          changed,
          changeToo,
          dataArr,
          dates;
    for(var i = 0;i<4;i++){
        myDate = new Date(Date.parse(format(timestamp1+86400000*(i+1)).replace(/-/g, "/")));
        // var dataStr = (new Date(parseInt(timestamp1+86400000*(i+1))).toLocaleString()).split(' ')[0];
        detract = new Date(lastDay + 24 * 60 * 60 * 1000*(i+1));
        changeDay = [detract.getFullYear(), detract.getMonth() + 1, detract.getDate()];
        if (changeDay[1] < 10) {
            changeDay[1] = '0' + changeDay[1]
        };
        if (changeDay[2] < 10) {
            changeDay[2] = '0' + changeDay[2]
        };
        changed = changeDay[0] + '-' + changeDay[1] + '-' + changeDay[2];
        changeToo = new Date(changed);
        dataArr = changed.split('-');
        dates = dataArr[1].replace(/\b(0+)/gi, "") + '月' + dataArr[2].replace(/\b(0+)/gi, "") + '日('+weeks[myDate.getDay()]+")";
        // var data = dates = date[1].replace(/\b(0+)/gi, "") + '月' + date[2].replace(/\b(0+)/gi, "") +'日('+weeks[myDate.getDay()]+")";

        $(".daySelect .weui-cell").eq(i).find('span').html(dates);
        if((timestamp1+86400000*(i+1))>(timestamp+86400000*29)){
            $(".daySelect .weui-cell").eq(i).find('span').css('color','gray');
            $(".daySelect .weui-cell").eq(i).find('input').attr('disabled','disabled');
        }
    };

    //从session中获取乘车人信息并添加到页面中
    $(".passengerInfo").remove();
    if(sessionStorage.userList){
        var arr = JSON.parse(sessionStorage.userList);
        for(var i = 0;i<arr.length;i++){
          var numArr = arr[i].passenger_id_no.split('');
          numArr.splice(6,8,"*","*","*","*","*","*","*","*");
          var temp = $("#passengerTemp").clone().html();
          var html = temp.replace("姓名",arr[i].passenger_name);
          html = html.replace("类型",arr[i].passenger_id_type_name).replace("证件号码",numArr.join(""));
          $("#modify").before(html);
        };
    };


    //点击跳转到抢票页并将数据传给后台
    $('.link').on('click',function () {
        var trainNo   = $('#trainNum_type').find('p span').html(),
               seatType = $('#set_type').find('p span').html(),
               trainDay = $('#ticketsDate').find('p span').html(),
               phone    = $('.input-control').val();

        //判断电话号码是否正确，正确存入session中
        if(!(/^1[3|4|5|8][0-9]\d{4,8}$/.test(phone))){
            $.alert("请输入正确的通知电话！");
            return false;
        };
        sessionStorage.setItem('phone',phone);

        var dayArr = [];
        for(var i = 0;i<trainDay.split(',').length;i++){
            dayArr.push((new Date).getFullYear()+'年'+trainDay.split(',')[i].substring(0,trainDay.split(',')[i].length-5));
        };
        var dayStr = dayArr.join(',');

        trainNo = trainNo.indexOf("提高抢票")>-1?" ":trainNo;
        seatType = seatType.indexOf("提高抢票")>-1?" ":seatType;
        dayStr = dayStr.indexOf("提高")>-1?" ":dayStr;

        //添加乘客信息到后台
        if(sessionStorage.userList){
            var arr = JSON.parse(sessionStorage.userList);
            var passengerArr = [];
            for(var i = 0;i<arr.length;i++){
                passengerArr.push(arr[i].passenger_id_no);
            };
        };
        var data = {
            "aboardPlace": sessionStorage.fromStation,
            "arrivedPlace": sessionStorage.toStation,
            "estimatedAboardTime": infos.startDate,
            "alternativeAboardTime": dayChange(dayStr),
            "aboardStation": paras.from_station_name,
            "arrivedStation": paras.to_station_name,
            "lineNo": paras.station_train_code,
            "aboardTimeFromStation": paras.start_time,
            "arrivedTimeToStation": paras.arrive_time,
            "estimatedLineNo": trainNo,
            "seatType": infos.seatStyle,
            "estimatedSeatType": seatType,
            "userName": sessionStorage.username,
            "passWord": sessionStorage.password,
            "noticePhoneNo": phone,
            "passengerIdentityCardNo": passengerArr
            // "passengerIdentityCardNo": ["123","321"]
        };
        // var data = {
        //     "aboardPlace": "长沙",
        //     "arrivedPlace": "深圳",
        //     "estimatedAboardTime": "2017年4月20日",
        //     "alternativeAboardTime": "2017年4月21日,2017年4月22日,",
        //     "aboardStation": "长沙南",
        //     "arrivedStation": "深圳北",
        //     "aboardTimeFromStation": "07:45",
        //     "arrivedTimeToStation": "11:25",
        //     "lineNo": "G101",
        //     "estimatedLineNo": "G102,G103,G104,",
        //     "seatType": "商务座",
        //     "estimatedSeatType": "一等座,特等座,",
        //     "userName": "12306",
        //     "passWord": "654321",
        //     "noticePhoneNo": "17375808066",
        //     "passengerIdentityCardNo": ["123","321"]
        // };
        getUserInfo("../../ticketOrder/createGrabOrder",JSON.stringify(data));
    });


})();

/*
 * @param url          url地址
 * @param param   需要提交的数据，对象格式
 * @param type       类型post或get，默认get
 */
function ajax(url, param, type) {
    // 利用了jquery延迟对象回调的方式对ajax封装，使用done()，fail()，always()等方法进行链式回调操作
    return $.ajax({
        url: url,
        data: param || {},
        type: type || 'GET',
        contentType: "application/json"
    });
};

function getUserInfo(url,param) {
  ajax(url,param,'POST').done(function (resp) {
      var data = JSON.parse(resp);
      console.log(data);
      if(data.data){
         var ticketOrderId = data.data.ticketOrderId;
         sessionStorage.setItem("ticketOrderId",ticketOrderId);
         sessionStorage.removeItem('trainInfo');
          window.location.href = 'grabTicketing.html';
      }else{
          $.alert("订单生成失败！");
      };
  }).fail(function (err) {
    console.log(err);
  });
};

/*
* 获取当前日期，格式为 yyyy-MM-dd
 */
function getNowFormatDate() {
    var date = new Date();
    var seperator1 = "-";
    // var seperator2 = ":";
    var month = date.getMonth() + 1;
    var strDate = date.getDate();
    if (month >= 1 && month <= 9) {
        month = "0" + month;
    }
    if (strDate >= 0 && strDate <= 9) {
        strDate = "0" + strDate;
    }
    var currentdate = date.getFullYear() + seperator1 + month + seperator1 + strDate;
        // + " " + date.getHours() + seperator2 + date.getMinutes()
        // + seperator2 + date.getSeconds();
    return currentdate;
}

//将时间戳转换成日期时间
function add0(m){return m<10?'0'+m:m }
function format(shijianchuo)
{
//shijianchuo是整数，否则要parseInt转换
    var time = new Date(shijianchuo);
    var y = time.getFullYear();
    var m = time.getMonth()+1;
    var d = time.getDate();
    return y+'-'+add0(m)+'-'+add0(d);
};

//将yyyy年mm月dd日转换成yyyy-MM-dd
function dayChange(dayStr) {
    dayStr = dayStr.replace(/年/g, "-");
    dayStr = dayStr.replace(/月/g, "-");
    dayStr = dayStr.replace(/日/g, "");
    return dayStr;
};