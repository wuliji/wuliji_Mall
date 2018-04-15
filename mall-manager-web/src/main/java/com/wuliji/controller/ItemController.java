package com.wuliji.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wuliji.common.pojo.EasyUIDataGridResult;
import com.wuliji.common.utils.MallResult;
import com.wuliji.pojo.TbItem;
import com.wuliji.service.ItemService;

/**
 * 商品管理Controller
 * @author Administrator
 *
 */
@Controller
public class ItemController {
	
	@Autowired
	private ItemService itemService;
	
	@RequestMapping("/item/{itemId}")
	@ResponseBody
	public TbItem getItemBtId(@PathVariable Long itemId) {
		TbItem tbItem = itemService.getItemById(itemId);
		return tbItem;
	}
	
	/**
	 * 商品分页显示
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping("/item/list")
	@ResponseBody
	public EasyUIDataGridResult getItemList(Integer page, Integer rows) {
		//调用服务查询商品列表
		EasyUIDataGridResult result = itemService.getItemList(page, rows);
		return result;
	}
	
	/**
	 * 商品添加功能
	 */
	@RequestMapping(value="/item/save",method=RequestMethod.POST)
	@ResponseBody
	public MallResult addItem(TbItem item, String desc) {
		MallResult result = itemService.addItem(item, desc);
		return result;
	}
	
	/**
	 * 商品删除功能
	 */
	@RequestMapping("/rest/item/delete")
	@ResponseBody
	public MallResult deleteItem(String[] ids) {
		MallResult result = itemService.deleteItem(ids);
		return result;
	}
	
	/**
	 * 根据id查询商品信息
	 */
	@RequestMapping("/rest/item/param/item/query/{id}")
	@ResponseBody
	public MallResult editItem(@PathVariable Long id) {
		MallResult result = itemService.editItem(id);
		return result;
	}
	
	/**
	 * 根据id查询描述
	 */
	@RequestMapping("/rest/item/query/item/desc/{id}")
	@ResponseBody
	public MallResult editItemDesc(@PathVariable Long id) {
		MallResult result = itemService.editItemDesc(id);
		return result;
	}
	
	/**
	 * 更新商品信息
	 */
	@RequestMapping(value="/rest/item/update",method=RequestMethod.POST)
	@ResponseBody
	public MallResult updateItem(TbItem item, String desc) {
		MallResult result = itemService.updateItem(item, desc);
		return result;
	}
	
	/**
	 * 商品下架
	 */
	@RequestMapping("/rest/item/instock")
	@ResponseBody
	public MallResult instock(String[] ids) {
		MallResult result = itemService.instock(ids);
		return result;
	}
	
	/**
	 * 商品上架
	 */
	@RequestMapping("/rest/item/reshelf")
	@ResponseBody
	public MallResult reshelf(String[] ids) {
		MallResult result = itemService.reshelf(ids);
		return result;
	}
}
