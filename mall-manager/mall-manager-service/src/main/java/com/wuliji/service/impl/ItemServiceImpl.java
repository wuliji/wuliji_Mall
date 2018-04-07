package com.wuliji.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wuliji.common.pojo.EasyUIDataGridResult;
import com.wuliji.mapper.TbItemMapper;
import com.wuliji.pojo.TbItem;
import com.wuliji.pojo.TbItemExample;
import com.wuliji.service.ItemService;

/**
 * 商品管理
 * @author Administrator
 *
 */
@Service
public class ItemServiceImpl implements ItemService{

	@Autowired
	private TbItemMapper itemMapper;
	
	@Override
	public TbItem getItemById(long itemId) {
		return itemMapper.selectByPrimaryKey(itemId);
	}

	@Override
	public EasyUIDataGridResult getItemList(int page, int rows) {
		//设置分页信息
		PageHelper.startPage(page, rows);
		//执行查询
		TbItemExample example = new TbItemExample();
		List<TbItem> list = itemMapper.selectByExample(example);
		//创建一个返回对象
		EasyUIDataGridResult result = new EasyUIDataGridResult();
		result.setRows(list);
		//取分页结果
		PageInfo<TbItem> pageInfo = new PageInfo<>(list);
		result.setTotal(pageInfo.getTotal());
		return result;
	}
	
}
