package com.railwayservice.productmanage.entity;

import com.railwayservice.application.config.AppConfig;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.util.List;

/**
 * 商品分类
 * 该表由商户维护
 *
 * @author xuyu
 */
@Entity
public class ProductCategory {
    /**
     * 该分类下的产品信息。
     */
    @Transient
    List<Product> products;
    /**
     * 主键
     */
    @Id
    @GenericGenerator(name = "idGenerator", strategy = AppConfig.UUID_GENERATOR)
    @GeneratedValue(generator = "idGenerator")
    private String productCategoryId;
    /**
     * 商户ID
     */
    private String merchantId;
    /**
     * 分类名称
     */
    private String name;
    /**
     * 分类描述
     */
    private String category;
    /**
     * 排序
     */
    private Integer sorter;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProductCategoryId() {
        return productCategoryId;
    }

    public void setProductCategoryId(String productCategoryId) {
        this.productCategoryId = productCategoryId;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Integer getSorter() {
        return sorter;
    }

    public void setSorter(Integer sorter) {
        this.sorter = sorter;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}
