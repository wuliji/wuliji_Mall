package com.wuliji.pagehelper.test;

import java.util.List;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wuliji.mapper.TbItemMapper;
import com.wuliji.pojo.TbItem;
import com.wuliji.pojo.TbItemExample;

public class PageHeloerTest {
	
	@Test
	public void testPageHelper() {
		//初始化spring容器
		ApplicationContext ap = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-dao.xml");
		//从容器中获得Mapper代理对象
		TbItemMapper itemMapper = ap.getBean(TbItemMapper.class);
		//执行sql语句设置分页信息使用PageHelper的startPage方法
		PageHelper.startPage(1, 10);
		//执行查询
		TbItemExample example = new TbItemExample();
		List<TbItem> list = itemMapper.selectByExample(example);
		//取分页信息，PageInfo。总记录数
		PageInfo<TbItem> pageInfo = new PageInfo<>(list);
		System.out.println(list.size());
	}
}
