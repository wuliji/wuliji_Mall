package com.wuliji.content.service;

import java.util.List;

import com.wuliji.common.pojo.EasyUITreeNode;
import com.wuliji.common.utils.MallResult;

public interface ContentCategoryService {
	
	List<EasyUITreeNode> getContentCatList(long parentId);
	MallResult addContentCategory(long parentId, String name);
}
