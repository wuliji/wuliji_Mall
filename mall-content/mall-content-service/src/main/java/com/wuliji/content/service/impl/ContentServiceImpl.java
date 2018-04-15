package com.wuliji.content.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wuliji.common.utils.MallResult;
import com.wuliji.content.service.ContentService;
import com.wuliji.mapper.TbContentMapper;
import com.wuliji.pojo.TbContent;
import com.wuliji.pojo.TbContentExample;
import com.wuliji.pojo.TbContentExample.Criteria;

/**
 * 内容管理Service
 * @author Administrator
 *
 */
@Service
public class ContentServiceImpl implements ContentService{

	@Autowired
	private TbContentMapper contentMapper;
	
	@Override
	public MallResult addContent(TbContent content) {
		//将内容数据插入到内容表
		content.setCreated(new Date());
		content.setUpdated(new Date());
		//插入到数据库
		contentMapper.insert(content);
		return MallResult.ok();
	}

	@Override
	/**
	 * 根据内容分类id查询内容列表
	 */
	public List<TbContent> getContentListByCid(long cid) {
		TbContentExample example = new TbContentExample();
		Criteria criteria = example.createCriteria();
		//设置查询条件
		criteria.andCategoryIdEqualTo(cid);
		List<TbContent> list = contentMapper.selectByExampleWithBLOBs(example);
		return list;
	}

}
