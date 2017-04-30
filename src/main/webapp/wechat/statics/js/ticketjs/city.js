$(function () {
    'use strict';

    var fromStation, toStation;

    // 渲染所有城市
    $(document).ready(function () {
        var renderSearchList = function (data) {
            $.each(data, function () {
                var text = $('#hotCity').clone().text(),
                    spell = this.spell,
                    abbr = this.stationNameAbbr.toLowerCase(),
                    p = '<p data-spell="' + this.spell + '" data-abbr="' + abbr + '">' + this.stationName + '(' + abbr + ')</p>';
                $('#searchResult').append(text.replace('实时搜索文本', p));
            });
        };

        //获取城市
        if (!sessionStorage.citylist) {
            $.ajax({
                url: '/RailwayService/railwayStation/findAllStationForBooking',
                type: 'GET',
                dataType: 'json',
                success: function (resb) {
                    if (resb.success) {
                        sessionStorage.citylist = JSON.stringify(resb.data);
                        renderSearchList(resb.data);
                    }
                },
                error: function (XMLHttpRequest) {
                    $.toast('获取全部城市信息列表失败');
                    console.log(XMLHttpRequest.statusText);
                }
            });
        } else {
            renderSearchList(JSON.parse(sessionStorage.citylist));
        }

        // 默认值
        fromStation = sessionStorage.fromStation;
        toStation = sessionStorage.toStation;
        $('#fromStation').val(sessionStorage.fromStation);
        $('#toStation').val(sessionStorage.toStation);
        $('.search-bar__label:last-child').click();
    });

    // 隐藏label
    $('.search-bar__label').on('click', function () {
        var searchBar = $(this).parents('.search-bar'),
            $input = searchBar.find('input');
        searchBar.addClass('focus');
        $input.focus();
    });

    // 隐藏input
    $('.weui-icon-clear').on('click', function () {
        var $input = $(this).prev('input'),
            searchBar = $(this).parents('.search-bar');
        $input.val('').removeClass('active');
        $input.attr('id') === 'fromStation' ? fromStation = undefined : toStation = undefined;
        searchBar.toggleClass('focus');
        $('.searchbar-result').addClass('hide');
    });

    // input框标记active类
    $(".search-bar__input").focus(function () {
        $(".search-bar__input.active").removeClass('active');
        $(this).addClass('active');
    });

    // 找到当前点击的dt下的span标签并获取其内容
    $("dl>dt").click(function () {
        var text = $(this).find("span").text(),
            $input = $('input.active');

        if ($input.length === 0) {
            $('.search-bar__label:not(:hidden):first').click();
            $input = $('input.active');
        }

        $(".search-bar__input.active").val(text);
        $input.attr('id') === 'fromStation' ? fromStation = text : toStation = text;
        $('.search-bar__label:not(:hidden):first').click();
    });

    $('#submit').click(function () {
        if (!fromStation) {
            return $.alert('请选择出发站');
        } else if (!toStation) {
            return $.alert('请选择目的站');
        } else {
            sessionStorage.setItem('fromStation', fromStation);
            sessionStorage.setItem('toStation', toStation);
            window.location.href = "index.html";
        }
    });

    // 模糊搜索
    $(".search-bar__input").keyup(function () {
        var text = $(".search-bar__input.active").val().trim().toLowerCase(),
            $items = $('.search__item'),
            find = false;

        $items.hide().removeClass('hide-bottom');
        $.each($items, function () {
            var $p = $(this).find('p'),
                name = $p.text(),
                spell = $p.data('spell'),
                abbr = $p.data('abbr');

            if (name.indexOf(text) !== -1 || spell.indexOf(text) !== -1 || abbr.indexOf(text) !== -1) {
                $(this).show();
                find = true;
            }
        });

        if (find) {
            $('.no-record').addClass('hide');
        } else {
            $('.no-record').removeClass('hide');
        }

        $('#searchResult').removeClass('hide');
        $('.search__item:not(:hidden):last').addClass('hide-bottom');
    });

    // 点击搜索项后隐藏全部结果
    $(document).on('click', '.search__item', function () {
        var text = $(this).text().trim().split('(')[0],
            $input = $('input.active');

        $input.val(text);
        $input.attr('id') === 'fromStation' ? fromStation = text : toStation = text;
        $('#searchResult').addClass('hide');
        $('.search-bar__label:not(:hidden):first').click();
    });
});

