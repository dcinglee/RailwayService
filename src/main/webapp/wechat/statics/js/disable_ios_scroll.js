/**
 * Created by zy on 2017/3/28.
 */

$(document).ready(function () {
    // document.body.addEventListener('touchmove', function (evt) {
    //     if (!evt._isScroller) {
    //         evt.preventDefault();
    //     }
    // });

    // 禁止上拉、下拉
    function overscroll (el) {
        el.addEventListener('touchstart', function () {
            var top = el.scrollTop,
                totalScroll = el.scrollHeight,
                currentScroll = top + el.offsetHeight;
            if (top === 0) {
                el.scrollTop = 1;
            } else if (currentScroll === totalScroll) {
                el.scrollTop = top - 1;
            }
        });
        el.addEventListener('touchmove', function (evt) {
            if (el.offsetHeight < el.scrollHeight) {
                evt._isScroller = true;
            } else {
                evt._isScroller = false;
            }
        });
    }

    $.each(document.querySelectorAll('.scroll'), function () {
        overscroll($(this)[0]);
    });
});