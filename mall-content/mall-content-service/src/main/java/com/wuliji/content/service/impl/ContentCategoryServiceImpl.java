package com.wuliji.content.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wuliji.common.pojo.EasyUITreeNode;
import com.wuliji.common.utils.MallResult;
import com.wuliji.content.service.ContentCategoryService;
import com.wuliji.mapper.TbContentCategoryMapper;
import com.wuliji.pojo.TbContentCategory;
import com.wuliji.pojo.TbContentCategoryExample;
import com.wuliji.pojo.TbContentCategoryExample.Criteria;

/**
 * 内容分类管理Service
 * @author Administrator
 *
 */
@Service
public class ContentCategoryServiceImpl implements ContentCategoryService{
	
	@Autowired
	private TbContentCategoryMapper contentCategoryMapper;

	@Override
	public List<EasyUITreeNode> getContentCatList(long parentId) {
		//根据parentId查询子节点列表
		TbContentCategoryExample example = new TbContentCategoryExample();
		Criteria criteria = example.createCriteria();
		//设置查询条件
		criteria.andParentIdEqualTo(parentId);
		//执行查询
		List<TbContentCategory> catList = contentCategoryMapper.selectByExample(example);
		//转换成EasyUI列表
		List<EasyUITreeNode> nodeList = new ArrayList<EasyUITreeNode>();
		for (TbContentCategory tbContentCategory : catList) {
			EasyUITreeNode node = new EasyUITreeNode();
			node.setId(tbContentCategory.getId());
			node.setText(tbContentCategory.getName());
			node.setState(tbContentCategory.getIsParent()?"closed":"open");
			//添加到列表
			nodeList.add(node);
		}
		return nodeList;
	}

	@Override
	public MallResult addContentCategory(long parentId, String name) {
		//创建一个表对应的pojo
		TbContentCategory contentCategory = new TbContentCategory();
		//设置pojo的属性
		contentCategory.setParentId(parentId);
		contentCategory.setName(name);
		contentCategory.setIsParent(false);//新添加的节点一定是叶子节点
		contentCategory.setStatus(1);//1为正常，2为删除状态
		contentCategory.setSortOrder(1);//默认排序为1
		contentCategory.setCreated(new Date());
		contentCategory.setUpdated(new Date());
		//插入到数据库中
		contentCategoryMapper.insert(contentCategory);
		//判断父节点的isparentId属性，如果不是true改为true
		TbContentCategory parent = contentCategoryMapper.selectByPrimaryKey(parentId);
		if(!parent.getIsParent()) {
			parent.setIsParent(true);
			//更新数据库
			contentCategoryMapper.updateByPrimaryKey(parent);
		}
		
		//返回结果，包含pojo
		return MallResult.ok(contentCategory);
	}
	
	
}
