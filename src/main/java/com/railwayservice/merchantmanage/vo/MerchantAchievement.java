package com.railwayservice.merchantmanage.vo;

import com.railwayservice.application.vo.PageData;

import java.math.BigDecimal;

/**
 * 统计总营业额、总利润等。
 *
 * @author Ewing
 * @date 2017/3/13
 */
public class MerchantAchievement {

    // 标签描述
    private String label;
    // 总营业额
    private BigDecimal total;

    private PageData dailyAchievement;

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public PageData getDailyAchievement() {
        return dailyAchievement;
    }

    public void setDailyAchievement(PageData dailyAchievement) {
        this.dailyAchievement = dailyAchievement;
    }
}
