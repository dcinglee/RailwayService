/**
 * Created by xz on 2017/3/1.
 */
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
        type: type || 'GET'
    });
};

/*
* 获取用户信息
 */

ajax("../entrance/getCurrentUser",'','POST').done(function(resp){
    var data = $.parseJSON(resp);
    console.log(data);
    //微信头像
    $(".userInfo img").attr('src',data.data.headimgUrl);
    //微信用户名
    $(".userInfo .title").html(data.data.nickName);
});