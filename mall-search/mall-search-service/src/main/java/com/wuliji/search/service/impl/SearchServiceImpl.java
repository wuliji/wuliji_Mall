package com.wuliji.search.service.impl;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wuliji.common.pojo.SearchResult;
import com.wuliji.search.dao.SearchDao;
import com.wuliji.search.service.SearchService;

/**
 * 商品搜索Service
 * @author Administrator
 *
 */
@Service
public class SearchServiceImpl implements SearchService{

	@Autowired
	private SearchDao searchDao;
	
	@Override
	public SearchResult search(String keyword, int page, int rows) throws SolrServerException {
		//创建一个SolrQuery对象
		SolrQuery query = new SolrQuery();
		//设置查询条件
		query.setQuery(keyword);
		//设置分页条件
		if(page <= 0) page = 1;
		query.setStart((page-1)*rows);
		query.setRows(rows);
		//设置默认搜索域
		query.set("df", "item_title");
		//开启高亮显示
		query.setHighlight(true);
		query.addHighlightField("item_title");
		query.setHighlightSimplePre("<em style=\"color:red\">");
		query.setHighlightSimplePost("</em>");
		//调用dao执行查询
		SearchResult result = searchDao.search(query);
		//需要计算总页数
		Long recordCount = result.getRecordCount();
		int totalPage = (int) (recordCount / rows);
		if(recordCount % rows > 0) totalPage ++;
		//添加到返回结果
		result.setTotalPage(totalPage);
		//返回结果
		return result;
	}
	
}
