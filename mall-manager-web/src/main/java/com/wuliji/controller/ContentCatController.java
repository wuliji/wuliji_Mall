package com.wuliji.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wuliji.common.pojo.EasyUITreeNode;
import com.wuliji.common.utils.MallResult;
import com.wuliji.content.service.ContentCategoryService;

/**
 * 内容分类管理
 * @author Administrator
 *
 */
@Controller
public class ContentCatController {
	
	@Autowired
	private ContentCategoryService contentCategoryService;
	
	/**
	 * 显示分类信息
	 * @param parentId
	 * @return
	 */
	@RequestMapping("/content/category/list")
	@ResponseBody
	public List<EasyUITreeNode> getContentCatList(
			@RequestParam(name="id",defaultValue="0")Long parentId){
		List<EasyUITreeNode> list = contentCategoryService.getContentCatList(parentId);
		return list;
	}
	
	/**
	 * 添加分类节点
	 */
	@RequestMapping(value="/content/category/create", method=RequestMethod.POST)
	@ResponseBody
	public MallResult createContentCategory(Long parentId, String name) {
		MallResult mallResult = contentCategoryService.addContentCategory(parentId, name);
		return mallResult;
	}
}
