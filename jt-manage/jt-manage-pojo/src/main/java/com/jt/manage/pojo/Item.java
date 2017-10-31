package com.jt.manage.pojo;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.jt.common.po.BasePojo;

//商品信息
@Table(name="tb_item")  //表示将对象和数据表一一对应
public class Item extends BasePojo{
	@Id 	//表示当前属性为数据表中的主键字段
	@GeneratedValue(strategy=GenerationType.IDENTITY)  //表示主键自增
	private Long id;  //商品的编号
	private String title; //商品的标题
	
	//@Column(name="sell_point")  没有开启驼峰映射时 添加@Column 指定属性和字段的对应关系
	private String sellPoint;  //商品的卖点
	private Long price;			//商品的价格
	private Integer num;		//商品数量
	private String barcode;		//条形码
	private String image;		//商品图片url信息
	private Long cid;			//通过ajax发起请求  商品分类id号
	private Integer status;		//1正常，2下架，3删除'
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getSellPoint() {
		return sellPoint;
	}
	public void setSellPoint(String sellPoint) {
		this.sellPoint = sellPoint;
	}
	public Long getPrice() {
		return price;
	}
	public void setPrice(Long price) {
		this.price = price;
	}
	public Integer getNum() {
		return num;
	}
	public void setNum(Integer num) {
		this.num = num;
	}
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public Long getCid() {
		return cid;
	}
	public void setCid(Long cid) {
		this.cid = cid;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	
	
}
