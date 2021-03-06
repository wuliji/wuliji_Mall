package com.wuliji.item.listener;

import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import com.wuliji.item.pojo.Item;
import com.wuliji.pojo.TbItem;
import com.wuliji.pojo.TbItemDesc;
import com.wuliji.service.ItemService;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * 监听商品添加消息，生成对应静态页面
 * @author Administrator
 *
 */
public class HtmlGenListener implements MessageListener {
	
	@Autowired
	private ItemService itemService;
	@Autowired
	private FreeMarkerConfigurer freeMarkerConfigurer;
	@Value("${HTML_GEN_PATH}")
	private String HTML_GEN_PATH;
	

	@Override
	public void onMessage(Message message) {
		
		try {
			//创建一个模板
			//从消息中取商品id
			TextMessage textMessage = (TextMessage) message;
			String text = textMessage.getText();
			Long itemId = new Long(text);
			//等待事务提交
			Thread.sleep(1000);
			//根据商品id查询信息，商品基本信息和商品描述
			TbItem tbItem = itemService.getItemById(itemId);
			Item item = new Item(tbItem);
			TbItemDesc tbItemDesc = itemService.getItemDescById(itemId);
			//创建一个数据集，把商品数据封装到Map中
			Map data = new HashMap<>();
			data.put("item", item);
			data.put("itemDesc", tbItemDesc);
			//加载模板对象，创建一个输出流，指定输出目录及文件名
			Configuration configuration = freeMarkerConfigurer.getConfiguration();
			Template template = configuration.getTemplate("item.ftl");
			Writer out = new FileWriter(HTML_GEN_PATH + itemId+ ".html");
			//生成静态页面
			template.process(data, out);
			//关闭流
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
