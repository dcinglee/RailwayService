package com.railwayservice.productmanage.service;

/**
 * 商品类型常量值
 *
 * @author lixs
 */
public class ProductStatic {

    /**===================================================
     * 商品类型
     * ===================================================**/
    /**
     * 该商品是精选美食
     */
    public static final int Product_Recommend_True = 10000;

    /**
     * 该商品未推荐
     */
    public static final int Product_Recommend_False = 10001;

    /**
     * 该商品是热卖商品
     */
    public static final int Product_Recommend_Hot = 10002;

    /**
     * ===================================================
     * 大型站点
     * ===================================================
     **/
    public static final String SZB = "深圳北";

    /**
     * ===================================================
     * 商品状态 开售 停售 等
     * ===================================================
     **/
    public static final int STATE_SALE_ON = 15001;
    public static final int STATE_SALE_OFF = 15002;

}
