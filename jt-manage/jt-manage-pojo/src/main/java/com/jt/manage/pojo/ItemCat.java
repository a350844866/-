package com.jt.manage.pojo;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.jt.common.po.BasePojo;


//商品分类对象
@Table(name="tb_item_cat")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ItemCat extends BasePojo{

	@Id  //主键信息
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;	//表示商品分类Id号
	private Long parentId;	//上级分类Id
	private String name;	//商品的名称
	private Integer status;	//1正常，2删除
	private Integer sortOrder;	//排序号
	private Boolean isParent;	//是否为上级类目 1级或2级菜单
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getSortOrder() {
		return sortOrder;
	}
	public void setSortOrder(Integer sortOrder) {
		this.sortOrder = sortOrder;
	}
	public Boolean getIsParent() {
		return isParent;
	}
	public void setIsParent(Boolean isParent) {
		this.isParent = isParent;
	}

	//为了满足EasyUI树形结构 添加get方法
	public String getText(){
		return name;
	}

    //如果是上级分类菜单"closed"  否则"open"
	//state: 节点状态,“open”或“closed”。
    public String getState() {

		return isParent ? "closed" : "open";
	}


}
