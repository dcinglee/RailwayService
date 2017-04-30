var domain,
    code = 0; // 0是猿动力(生产环境)、1是以为科技(测试环境)

if (code === 0) {
    domain = 'szeiv.com';
} else {
    var url = window.location.href;
    var urlArr = url.split('/');
    domain = urlArr[2];
}

var base_url = 'http://' + domain + '/RailwayService',
        login_url  = base_url + '/entrance/index.do';