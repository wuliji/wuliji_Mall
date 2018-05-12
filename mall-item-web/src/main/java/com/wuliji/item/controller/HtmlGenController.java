package com.wuliji.item.controller;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * 生成静态页面测试Controller
 * @author Administrator
 *
 */
@Controller
public class HtmlGenController {
	
	@Autowired
	private FreeMarkerConfigurer freeMarkerConfigurer;
	
	@RequestMapping("/genhtml")
	@ResponseBody
	public String genHtml() throws Exception{
		Configuration configuration = freeMarkerConfigurer.getConfiguration();
		//加载模版对象
		Template template = configuration.getTemplate("hello.ftl");
		//创建一个数据集
		Map data = new HashMap<>();
		data.put("hello", 123);
		//指定文件输出的路径及文件名
		Writer out = new FileWriter(new File("E:\\Documents\\Desktop\\code\\temp\\freemarker\\hello2.html"));
		//输出文件
		template.process(data, out);
		//关闭流
		out.close();
		return "OK";
	}
}
