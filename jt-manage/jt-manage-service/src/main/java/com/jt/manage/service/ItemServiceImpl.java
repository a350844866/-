package com.jt.manage.service;

import java.util.Date;
import java.util.List;

import com.jt.manage.mapper.ItemDescMapper;
import com.jt.manage.pojo.ItemDesc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jt.common.vo.EasyUIResult;
import com.jt.manage.mapper.ItemMapper;
import com.jt.manage.pojo.Item;

@Service
public class ItemServiceImpl implements ItemService {

	@Autowired
	private ItemMapper itemMapper;

    @Autowired
    private ItemDescMapper itemDescMapper;

    @Override
	public List<Item> findAll() {
		
		return itemMapper.findAll();
	}

	@Override
	public List<Item> findMapper() {
		/**
		 * 总结:
		 * 	1.当使用通用Mapper时,如果传入的对象为null 或者是一个空对象 这是通用Mapper将不会添加where条件
		 *    select * from tb_item
		 *  2.如果传入的对象某些属性不为null 这会以这些属性的值为where条件进行查询
		 *    select * from tb_item where cid = 12
		 *  3.多条件查询
		 *    对象中有多个不为null的属性,这时通用mapper会进行对条件查询
		 *    select * from tb_item where id = 1474391952 and cid = 12
		 */
		Item item = new Item();
		item.setCid(12L);
		item.setId(1474391952L);
		return itemMapper.select(item);
	}
	
	/**
	 * 由于前台页面采用EasyUI的方式进行调用 所以返回值应该满足EasyUI的要求
	 * {"total":2000,"rows":[{},{},{}]}   total 是记录总数   rows表示查询的数据
	 * 准备一个对象 对象中有属性:
	 * int total
	 * List<Item>   rows 
	 */
	/*@Override
	public EasyUIResult findItemList(int page, int rows) {
		*//**
		 * 分页的业务逻辑
		 * page 页数  rows 行数
		 * 第1页   SELECT * FROM tb_item ORDER BY updated DESC LIMIT 0,20
		 * 第2页   SELECT * FROM tb_item ORDER BY updated DESC LIMIT 20,20
		 * 第3页   SELECT * FROM tb_item ORDER BY updated DESC LIMIT 40,20
		 * 第n页   SELECT * FROM tb_item ORDER BY updated DESC LIMIT (page-1)*20,20
		 *//*
		int begin = (page-1)*rows;  //起始页数
		List<Item> itemList = itemMapper.findItemList(begin,rows);
		int count = itemMapper.selectCount(null);
		//sql:select count(*) from tb_item
		return new EasyUIResult(count, itemList);
	}*/
	
	
	/**
	 * page:当前的页数
	 * rows:展现的记录数
	 */
	@Override
	public EasyUIResult findItemList(int page, int rows) {
	    //表示开始使用分页工具
	    PageHelper.startPage(page,rows);


	    //表示查询全部记录  3100 当前的查询操作,必须为之分页开始之后一行 否则报错
        List<Item> itemList = itemMapper.findAll();

        PageInfo<Item> itemInfo = new PageInfo<Item>(itemList);

        return new EasyUIResult(itemInfo.getTotal(), itemList);

	}
	
	

	@Override
	public String findItemCatName(Long cid) {
		
		return itemMapper.findItemName(cid);
	}

	@Override
	public int findCount() {
		
		//查询的是tb_item表的记录数
		return itemMapper.findCount();
	}

	@Override
	public void saveItem(Item item,String desc) {
		
		item.setCreated(new Date());
		item.setUpdated(item.getCreated());
		item.setStatus(1);  //表示正常
		
		//调用通用Mapper实现入库操作
		itemMapper.insert(item);

		//通过查询方式获取itemId select * from tb_item where

		//商品描述信息中的id号应该和itemId保持一致
        ItemDesc itemDesc = new ItemDesc();
        System.out.println(item.getId()+"测试是否能够获取数据");
        itemDesc.setItemDesc(desc);
        itemDesc.setCreated(item.getCreated());
        itemDesc.setUpdated(item.getCreated());
        itemDescMapper.insert(itemDesc);
    }
	@Override
	public void updateItem(Item item,String desc) {
		
		item.setUpdated(new Date());
		//全部的修改  不管是否有数据
        //itemMapper.updateByPrimaryKey(record)
		
		//动态修改
		itemMapper.updateByPrimaryKeySelective(item);
        ItemDesc itemDesc = new ItemDesc();
        itemDesc.setItemDesc(desc);
        itemDesc.setItemId(item.getId());
        itemDesc.setUpdated(item.getUpdated());
        itemDescMapper.updateByPrimaryKey(itemDesc);
    }

	@Override
	public void deleteItems(Long[] ids) {
	    //关联删除
        itemDescMapper.deleteByIDS(ids);
        itemMapper.deleteByIDS(ids);
		
	}

	@Override
	public void updateStatus(Long[] ids, int status) {
		
		itemMapper.updateStatus(ids,status);

		
	}
    @Override
    public ItemDesc findItemDescById(Long itemId) {
        ItemDesc itemDesc = new ItemDesc();
        itemDesc.setItemId(itemId);
        return itemDescMapper.selectByPrimaryKey(itemDesc);
    }
}
