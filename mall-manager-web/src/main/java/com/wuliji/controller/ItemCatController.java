package com.wuliji.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wuliji.common.pojo.EasyUITreeNode;
import com.wuliji.service.ItemCatService;

/**
 * 商品分类管理
 * @author Administrator
 *
 */
@Controller
public class ItemCatController {
	
	@Autowired
	private ItemCatService itemCatService;
	
	@RequestMapping("/item/cat/list")
	@ResponseBody
	public List<EasyUITreeNode> getItemCatList(
			@RequestParam(name="id",defaultValue="0") Long parentId){
		//调用服务查询节点列表
		return itemCatService.getItemCatlist(parentId);
	}
}
