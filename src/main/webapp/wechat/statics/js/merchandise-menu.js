+function ($) {
    "use strict";

    var Menus = function (elem, options) {
        this.options = $.extend({}, Menus.DEFAULTS, options);
        this.$elem = $(elem);
        this.commodity = {};
        this.init();
    };

    Menus.Version = '0.0.1';
    Menus.DEFAULTS = {
        url: undefined,
        data: undefined,
        method: 'get',
        contentType: 'application/json',
        dataType: 'json',
        async: true
    };

    Menus.prototype.add = function (a, b) {
        var c, d, e;
        try {
            c = a.toString().split(".")[1].length;
        } catch (f) {
            c = 0;
        }
        try {
            d = b.toString().split(".")[1].length;
        } catch (f) {
            d = 0;
        }
        return e = Math.pow(10, Math.max(c, d)), (this.mul(a, e) + this.mul(b, e)) / e;
    };
    Menus.prototype.sub = function (a, b) {
        var c, d, e;
        try {
            c = a.toString().split(".")[1].length;
        } catch (f) {
            c = 0;
        }
        try {
            d = b.toString().split(".")[1].length;
        } catch (f) {
            d = 0;
        }
        return e = Math.pow(10, Math.max(c, d)), (this.mul(a, e) - this.mul(b, e)) / e;
    };
    Menus.prototype.mul = function (a, b) {
        var c = 0,
            d = a.toString(),
            e = b.toString();
        try {
            c += d.split(".")[1].length;
        } catch (f) {
        }
        try {
            c += e.split(".")[1].length;
        } catch (f) {
        }
        return Number(d.replace(".", "")) * Number(e.replace(".", "")) / Math.pow(10, c);
    };
    Menus.prototype.div = function (a, b) {
        var c, d, e = 0,
            f = 0;
        try {
            e = a.toString().split(".")[1].length;
        } catch (g) {
        }
        try {
            f = b.toString().split(".")[1].length;
        } catch (g) {
        }
        return c = Number(a.toString().replace(".", "")), d = Number(b.toString().replace(".", "")), this.mul(c / d, Math.pow(10, f - e));
    };

    Menus.prototype.init = function () {
        this.$header = $('.merchandise-top .weui-flex');
        this.$cdiv = $('.commodity-shopping-cart');

        // url ajax 处理, 获取this.data
        if (this.options.url) {
            var that = this;
            $.ajax({
                type: this.options.method,
                url: this.options.url,
                data: this.options.contentType === 'application/json' && this.options.method === 'post' ? JSON.stringify(this.options.data) : this.options.data,
                dataType: this.options.dataType,
                async: this.options.async,
                success: function (resb) {
                    if (resb.success) {
                        that.options.data = resb.data;
                    } else {
                        $.toast(resb.message, 'forbidden');
                        setTimeout(function () {
                            history.back(-1);
                        }, 2000);
                    }
                },
                error: function (XMLHttpRequest) {
                    console.log(XMLHttpRequest);
                    //错误处理
                }
            })
        }

        // 判断配送员是否在线
        $.ajax({
            type: this.options.method,
            url: '/RailwayService/merchant/isServiceProviderOnline.do?stationId=' + sessionStorage.stationId + '&serviceType=' + sessionStorage.serviceType,
            dataType: this.options.dataType,
            success: function (resb) {
                if (resb.success) {
                    sessionStorage.delivery = resb.data;
                }
            }
        });

        return this.renderMenu();
    };

    Menus.prototype.renderMenu = function () {
        var that = this,
            data = that.options.data;

        // 动态渲染商户信息
        that.$header.append($('#clone_hdiv').clone().text());
        var $logo = that.$header.find('img'),
            $title = that.$header.find('h4'),
            $addr = that.$header.find('p#address'),
            $proc = that.$header.find('p#proc'),
            $star = that.$header.find('.star-g'),
            evaluate = String(data.merchant.evaluate).split('.'),
            img_url = '/RailwayService' + data.merchant.imageUrl;

        $logo.attr('src', img_url);
        $title.text(data.merchant.name);
        $proc.html('公告:&nbsp;' + (data.merchant.announcement ? data.merchant.announcement : '该商家暂无公告'));
        $addr.html('地址: &nbsp;' + (data.merchant.address ? data.merchant.address : '商家暂未填写地址'));

        if (evaluate[0] !== 0) {
            for (var i = 0; i < evaluate[0]; ++i) {
                $star.append('<i class="iconfont icon-xing1"></i>');
            }
            if (evaluate.length > 1) {
                $star.append('<i class="iconfont icon-banxing1"></i>');
            }
        } else {
            $star.append('<span>暂无评分信息</span>');
        }
        that.$header.append('<style>.merchandise-info-center:before{background: url("' + img_url + '") #000000}</style>');

        // 渲染中间块
        if (data.menu.length !== 0) {
            // 渲染分类菜单
            that.$elem.append($('#clone_fdiv').clone().text());
            that.$elem.append($('#clone_sdiv').clone().text());
            var li, $panel, goods_img,
                $left_menu = that.$elem.find('ul'),
                $right_menu = that.$elem.find('.secondMenu');
            $.each(data.menu, function () {
                // 动态渲染左侧菜单li
                li = $('#clone_li').clone().text().replace(/__section-1__/g, this.category.productCategoryId).replace('__第一部分__', this.category.name);
                $left_menu.append(li);

                // 动态渲染右侧商品panel
                $right_menu.append($('#clone_goods').clone().text().replace(
                    '__target__', this.category.productCategoryId).replace(
                    '__标题__', this.category.name).replace(
                    '__描述__', (this.category.category ? this.category.category : '')));
                $panel = $right_menu.find('div.weui-panel_access:last');

                if (this.goods.length === 0) {
                    // 分类未设置商品
                    $panel.append($('#clone_good_bg').clone().text());
                } else {
                    // 填充panel中的商品信息
                    $.each(this.goods, function () {
                        if (this) {
                            if (this.imageUrl) {
                                goods_img = '/RailwayService' + this.imageUrl;
                            } else {
                                goods_img = '/RailwayService/wechat/statics/images/goods_empty.jpg';
                            }
                            $panel.append(
                                $('#clone_good').clone().text().replace(
                                    'url', goods_img).replace('__标题__', this.name.length > 8 ? this.name.substr(0, 8) + '...' : this.name).replace(
                                    '__内容__', this.introduction.length > 10 ? this.introduction.substr(0, 10) + '...' : this.introduction).replace(
                                    '__价格__', this.price ? this.price : 0).replace('__sales__', this.sales ? this.sales : 0).replace(/__商品id__/g, this.productId)
                            );
                        }
                    });
                }
            });
        } else {
            // 商家未设置分类
            $('.merchandise-menu').css('bottom', '0');
            $('div.empty').show();
        }

        // 渲染购物车
        that.$cdiv.append($('#clone_shopCart').clone().text());

        // 商家休息中
        if (that.options.data.merchant.status === 8003) {
            that.$elem.find('div.btn-ctl').remove();
            $('#num_0').hide();
            $('#b_end').show();
        }

        // 动态加载元素监听事件
        this.$elem.on('click', '[data-name="add"],[data-name="reduce"]', function () {
            // 新增商品， 修改commidy对象
            var type = $(this).data('name'),
                id = $(this).parents('.weui-panel__bd').data('id'),
                $divs = $(this).parents('.secondMenu').find('[data-id="' + id + '"]'),
                $reduce = $divs.find('div[data-name=reduce]'),
                $count = $divs.find('span[data-name=count]'),
                $badge = that.$cdiv.find('span.weui-badge'),
                $money = that.$cdiv.find('#cost'),
                modulus = (type === 'reduce') ? -1 : 1,
                price = that.goods[id].price * modulus,
                num = Number($count[0].innerText) + modulus,
                badge_num = Number($badge.text()) + modulus;

            if (num !== 0) {
                $reduce.fadeIn(500);
                $count.text(num).fadeIn(500);
            } else {
                $reduce.fadeOut(100);
                $count.fadeOut(100, function () {
                    $(this).text(num)
                });
            }

            if (badge_num !== 0) {
                $badge.text(badge_num).fadeIn(100);
                $('#num_0').hide();
                $('#submit').show();
            } else {
                $badge.fadeOut(100, function () {
                    $(this).text(badge_num)
                });
                $('#submit').hide();
                $('#num_0').show();
            }
            $money.text(that.add(Number($money.text()), price));

            if (type === 'add') {
                if (id in that.commodity) {
                    that.commodity[id].num += 1;
                } else {
                    that.commodity[id] = {
                        num: 1,
                        data: that.goods[id],
                    }
                }
            } else {
                if (id in that.commodity) {
                    that.commodity[id].num -= 1;
                    if (that.commodity[id].num === 0) {
                        delete that.commodity[id];
                    }
                }
            }
        }).on('click', '.secondMenu img,.secondMenu .weui-media-box__title,.secondMenu span.weui-media-box__desc', function (e) {
            // 蒙板弹窗操作
            if ($(e.target).hasClass('btn_ctl') || $(e.target).hasClass('iconfont')) {
                return false;
            }

            var id = $(this).parents('.goods-info').data('id'),
                data = that.goods[id],
                goods_img;

            if (data.imageUrl) {
                goods_img = '/RailwayService' + data.imageUrl;
            } else {
                goods_img = '/RailwayService/wechat/statics/images/goods_empty2.jpg';
            }

            $('body').append($('#clone_popup').clone().text().replace('__标题__', data.name).replace(
                '__副标题__', $(window).width() <= 320 ? data.introduction.substr(0, 16) : data.introduction.substr(0, 20)).replace(
                '__价格__', data.price ? data.price : 0).replace('__sales__', data.sales ? data.sales : 0).replace(/__商品id__/g, id).replace('__url__', goods_img));

            var $btn = $('.goods-popup-detail a');

            if (that.options.data.merchant.status === 8003) {
                $btn.remove();
            }
            $('div.goods-popup-detail').fadeIn();
        }).on('click', '.firstMenu li', function () {
            // 更改li背景
            var $li = $('.firstMenu li.active');

            if ($li[0] === this) {
                return false;
            }

            location.replace($(this).find('a').attr('href'));
            $li.removeClass('active');
            $(this).addClass('active');
            return false;
        });

        // 购物车上拉事件
        that.$cdiv.on('click', '[data-toggle="show-shopping-detail"]', function () {
            if ($.isEmptyObject(that.commodity)) {
                return false;
            }

            if ($('.weui-actionsheet').length !== 0) {
                return $('div.weui-mask').click();
            }

            $.actions({
                title: $('#clone_detail_title').clone().text(),
                actions: function () {
                    var actions = [{'text': $('#clone_detail_fee').clone().text().replace('__fee__', that.options.data.distributionCosts ? that.options.data.distributionCosts : 0)}];
                    $.each(that.commodity, function (key, value) {
                        actions.push({
                            'text': $('#clone_detail').clone().text().replace(
                                '__商品名称__', value.data.name).replace(
                                '__价格__', value.data.price * value.num).replace(
                                /__商品id__/g, key).replace(
                                '__数量__', value.num)
                        });
                    });
                    return actions;
                }()
            });
        });

        // 全局监听事件
        $(document).on('click', '[data-name="sheet"]', function () {
            // 购物车加减商品
            var type = $(this).data('type'),
                $num = (type === 'add') ? $(this).prev() : $(this).next(),
                $money = $(this).parents('.weui-flex').find('[data-name="price"]'),
                $div = $(this).parents('.weui-actionsheet__cell'),
                id = $(this).data('id'),
                modulus = (type === 'reduce') ? -1 : 1,
                num = Number($num.text()) + modulus,
                cost = that.add(Number($money.text()), that.commodity[id].data.price * modulus);

            if (num === 0) {
                $($('div.weui-panel__bd[data-id="' + id + '"]')[0]).find('div[data-name="reduce"]').click();
                if ($.isEmptyObject(that.commodity)) {
                    return $('div.weui-mask').click();
                } else {
                    return $div.remove();
                }
            } else {
                if (type === 'reduce') {
                    $($('div.weui-panel__bd[data-id="' + id + '"]')[0]).find('div[data-name="reduce"]').click();
                } else {
                    $($('div.weui-panel__bd[data-id="' + id + '"]')[0]).find('div[data-name="add"]').click();
                }
                $money.text(cost);
                $num.text(num);
            }
        }).on('click', '[data-name="clearAll"]', function () {
            $('.weui-mask').click();
            setTimeout(function () {
                var z = $('.commodity-shopping-cart').css('z-index');
                $('.commodity-shopping-cart').css('z-index', 2);
                // 清除所有商品
                $.confirm("您确定要清空购物车吗?", "确认清空?", function () {
                    $.each(that.commodity, function (id, data) {
                        for (var i = this.num - 1; i >= 0; i--) {
                            $($('div[data-name="reduce"][data-id="' + id + '"]')[0]).click();
                        }
                    });
                    $.ajax({
                        url: '/RailwayService/order/changeShoppingCart.do',
                        type: 'post',
                        contentType: that.options.contentType,
                        data: JSON.stringify({merchantId: sessionStorage.merchantId, productList: []}),
                        dataType: that.options.dataType
                    });
                    $.toast("购物车已清空!");
                    setTimeout(function () {
                        $('.commodity-shopping-cart').css('z-index', z);
                    }, 2500);
                }, function () {
                    $('.commodity-shopping-cart').css('z-index', z);
                });
            }, 300);
        }).on('click', '[data-name="pop-mask"]', function () {
            // 隐藏弹窗
            var $div = $('.goods-popup-detail');
            if ($div.length !== 0) {
                $div.remove();
            }
        }).on('click', '[data-name="addToCart"]', function () {
            // 弹窗新增商品
            $($('div.weui-panel__bd[data-id="' + $(this).data('id') + '"]')[0]).find('div[data-name="add"]').click();
            return $('div.weui-mask').click();
        }).on('click', '[data-name="submit"]', function () {
            // 订单确认页跳转
            if ($(this).hasClass('btn-disabled')) {
                return false;
            }

            $('.weui-mask').click();
            //组织购物车信息
            var cart = {'merchantId': null, productList: []};
            $.each(that.commodity, function (id, value) {
                cart.productList.push({
                    productId: id,
                    count: value.num
                });
                cart['merchantId'] = value.data.merchantId;
            });

            $.ajax({
                url: '/RailwayService/order/changeShoppingCart.do',
                type: 'post',
                contentType: that.options.contentType,
                data: JSON.stringify(cart),
                dataType: that.options.dataType,
                beforeSend: function () {
                    $.showLoading();
                },
                success: function (resb) {
                    $('.weui-mask_transparent,.weui_loading_toast').remove();
                    if (resb.success) {
                        sessionStorage.shoppingCart = that.commodity;
                        window.location.href = '/RailwayService/wechat/payOrder.html';
                    } else {
                        $.toast(resb.message, "forbidden");
                    }
                },
                error: function (XMLHttpRequest) {
                    $.hideLoading();
                    $.toast(XMLHttpRequest.statusText, "forbidden");
                }
            });
        }).on('click', 'div.close', function () {
            $('.weui-mask').click();
        });

        // 缓存商品信息
        var goods = {};
        $.each(that.options.data.menu, function () {
            if (this.goods) {
                // var id = this.category.productCategoryId;
                $.each(this.goods, function () {
                    if (this) {
                        goods[this.productId] = this;
                    }
                })
            }
        });
        that.goods = goods;

        // 根据session构建购物车
        this.shoppingCart();

        /**
         * bootstrap-scrollspy插件,功能为滚动监听.
         * 这个版本为源码修改版,添加左侧一级菜单在最上/下方继续滚动时,菜单向上/下滚动一个li高度
         * 具体使用方法看api:  http://v3.bootcss.com/javascript/#scrollspy
         */
        $right_menu.scrollspy({target: 'div.firstMenu', offset: 0, init: true});

        // 非商家页跳转
        if (sessionStorage.productId) {
            $('div[data-id="' + sessionStorage.productId + '"] img:first').click();
            sessionStorage.removeItem('productId');
        }

        // loading
        // $('.loading-bg').fadeOut(750, function () {
        //     $('#content').fadeIn('fast', function () {
        //     });
        // });
    };

    // 读取购物车临时缓存
    Menus.prototype.shoppingCart = function () {
        if (sessionStorage.shoppingCart) {
            $.each(JSON.parse(sessionStorage.shoppingCart), function (id, data) {
                for (var i = 0; i < data.num; ++i) {
                    $('div[data-name="add"][data-id=' + id + '] ').click();
                }
            });
        }
    };

    $.fn.menus = function (option) {
        var value,
            args = Array.prototype.slice.call(arguments, 1);

        this.each(function () {
            var $this = $(this),
                data = $this.data('rswc.menus'),
                options = $.extend({}, Menus.DEFAULTS, $this.data(), typeof option === 'object' && option);

            if (typeof option === 'string') {
                if (!data) {
                    return;
                }
                value = data[option].apply(data, args);
            }

            if (typeof option === 'object') {
                if (!option.url && !option.data) {
                    throw new Error("menus required url or data");
                }
            }

            if (!data) {
                $this.data('rswc.menus', (data = new Menus(this, options)));
            }
        });

        return typeof value === 'undefined' ? this : value;
    };

    $.fn.menus.Constructor = Menus;
    $.fn.menus.version = Menus.Version;
    $.fn.menus.defaults = Menus.DEFAULTS;
}(jQuery);
