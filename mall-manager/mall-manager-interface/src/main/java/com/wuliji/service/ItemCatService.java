package com.wuliji.service;

import java.util.List;

import com.wuliji.common.pojo.EasyUITreeNode;

public interface ItemCatService {
	
	List<EasyUITreeNode> getItemCatlist(long parentId);
}
