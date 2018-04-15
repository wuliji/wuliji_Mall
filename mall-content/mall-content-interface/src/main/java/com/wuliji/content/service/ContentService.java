package com.wuliji.content.service;

import java.util.List;

import com.wuliji.common.utils.MallResult;
import com.wuliji.pojo.TbContent;

public interface ContentService {
	
	MallResult addContent(TbContent content);
	List<TbContent> getContentListByCid(long cid);
}
