package com.railwayservice.common.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.railwayservice.application.config.AppConfig;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

/**
 * 在没有图片服务器之前，使用数据库存储图片。
 *
 * @author Ewing
 * @date 2017/2/8
 */
@Entity
public class ImageInfo {
    // 图片ID
    @Id
    @Column(length = 32)
    @GenericGenerator(name = "idGenerator", strategy = AppConfig.UUID_GENERATOR)
    @GeneratedValue(generator = "idGenerator")
    private String imageId;

    // 图片格式
    @Column(length = 10)
    private String format;

    // 图片所在路径
    private String url;

    // 创建时间
    @DateTimeFormat(pattern = AppConfig.DATE_FORMAT)
    @JsonFormat(pattern = AppConfig.DATE_FORMAT, timezone = AppConfig.TIMEZONE)
    private Date createDate;

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
