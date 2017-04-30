package com.railwayservice.common.entity;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * 字典类，用于记录可增加的枚举类型。
 *
 * @author Ewing
 */
@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class Dictionary {
    // 字典值
    @Id
    private Integer value;
    // 字面值
    private String name;
    // 分类描述符
    private String type;
    // 详细说明
    private String detail;

    public Dictionary() {
    }

    public Dictionary(Integer value) {
        this.value = value;
    }

    public Dictionary(Integer value, String name, String type, String detail) {
        this.value = value;
        this.name = name;
        this.type = type;
        this.detail = detail;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

}
