package com.jt.order.pojo;

import java.io.Serializable;

//订单下的商品信息
public class OrderItem implements Serializable {
    private String itemId;//商品号
    private String orderId;//订单号
    private Integer num; //商品数量
    private String title;//商品的标题
    private Long price;//商品的单价
    private String totalFee; //商品总价
    private String picPath;//图片地址

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public String getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(String totalFee) {
        this.totalFee = totalFee;
    }

    public String getPicPath() {
        return picPath;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }

    public String getItemId() {

        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }
}
