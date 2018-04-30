package com.wuliji.solrj;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

public class TestSolrJ {
	
	@Test
	public void addDocument() throws Exception{
		//创建一个SolrServer对象，创建一个连接。参加solr服务的url
		SolrServer solrServer = new HttpSolrServer("http://192.168.25.133:8080/solr/collection1");
		//创建一个文档对象SolrInputDocument
		SolrInputDocument document = new SolrInputDocument();
		//向文档对象中添加域。文档中必须包含一个id域，所有的域名称必须在schema.xml中定义
		document.addField("id", "doc01");
		document.addField("item_title", "测试商品01");
		document.addField("item_price", 1000);
		//把文档写入索引库
		solrServer.add(document);
		//提交
		solrServer.commit();
	}
	
	@Test
	public void deleteDocument() throws Exception {
		//创建一个SolrServer对象，创建一个连接。参加solr服务的url
		SolrServer solrServer = new HttpSolrServer("http://192.168.25.133:8080/solr/collection1");
		//删除文档
		solrServer.deleteById("doc01");
		//提交
		solrServer.commit();
	}
}
