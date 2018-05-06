package com.wuliji.search.message;

import java.io.IOException;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;

import com.wuliji.common.pojo.SearchItem;
import com.wuliji.search.mapper.ItemMapper;

/**
 * 监听商品添加信息，接收消息后，将对应的商品信息同步到索引库
 * @author Administrator
 *
 */
public class ItemAddMessageListener implements MessageListener{

	@Autowired
	private ItemMapper itemMapper;
	
	@Autowired
	private SolrServer solrServer;
	
	@Override
	public void onMessage(Message message) {
		//从消息中取商品id
		TextMessage textMessage = (TextMessage) message;
		try {
			String text = textMessage.getText();
			Long itemId = new Long(text);
			//等待事务提交
			Thread.sleep(100);
			//根据商品id查询商品信息
			SearchItem searchItem = itemMapper.getItemById(itemId);
			//创建一个文档对象
			SolrInputDocument document = new SolrInputDocument();
			//向文档对象中添加域
			document.addField("id", searchItem.getId());
			document.addField("item_title", searchItem.getTitle());
			document.addField("item_sell_point", searchItem.getSell_point());
			document.addField("item_price", searchItem.getPrice());
			document.addField("item_image", searchItem.getImage());
			document.addField("item_category_name", searchItem.getCategory_name());
			//把文档对象写入索引库
			solrServer.add(document);
			//提交
			solrServer.commit();
		} catch (JMSException | SolrServerException | IOException | InterruptedException e) {
			e.printStackTrace();
		}
		
	}

}
