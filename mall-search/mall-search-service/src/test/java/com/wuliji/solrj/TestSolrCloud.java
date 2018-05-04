package com.wuliji.solrj;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

public class TestSolrCloud {
	
	@Test
	public void testAddDocument() throws Exception{
		//创建一个集群的连接
		CloudSolrServer solrServer = new CloudSolrServer("192.168.25.133:2181,192.168.25.133:2182,192.168.25.133:2183");
		//zkHost参数(zookeeper的地址列表)
		//设置一个默认的collection
		solrServer.setDefaultCollection("collection2");
		//创建一个文档对象
		SolrInputDocument document = new SolrInputDocument();
		//向文档中添加域
		document.setField("id", "solrcloud01");
		document.setField("item_title", "测试商品");
		document.setField("item_price", 123);
		solrServer.add(document);
		solrServer.commit();
	}
	
	@Test
	public void testQueryDocument() throws Exception{
		//创建一个CloudSolrServer对象
		CloudSolrServer solrServer = new CloudSolrServer("192.168.25.133:2181,192.168.25.133:2182,192.168.25.133:2183");
		//设置一个默认的collection
		solrServer.setDefaultCollection("collection2");
		//创建一个查询对象
		SolrQuery query = new SolrQuery();
		//设置查询条件
		query.setQuery("*:*");
		//取查询结果
		QueryResponse response = solrServer.query(query);
		SolrDocumentList results = response.getResults();
		System.out.println("总记录数:" + results.getNumFound());
		for (SolrDocument solrDocument : results) {
			System.out.println(solrDocument.get("id"));
			System.out.println(solrDocument.get("item_title"));
			System.out.println(solrDocument.get("item_price"));
		}
		
	}
}
