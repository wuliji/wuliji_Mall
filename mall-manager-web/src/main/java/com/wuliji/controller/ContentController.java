package com.wuliji.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wuliji.common.utils.MallResult;
import com.wuliji.content.service.ContentService;
import com.wuliji.pojo.TbContent;

/**
 * 内容管理Controller
 * @author Administrator
 *
 */
@Controller
public class ContentController {

	@Autowired
	private ContentService contentService;
	
	@RequestMapping(value="/content/save",method=RequestMethod.POST)
	@ResponseBody
	public MallResult addContent(TbContent content) {
		return contentService.addContent(content);
	}
}
