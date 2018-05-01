package com.wuliji.common.pojo;

import java.io.Serializable;
import java.util.List;

public class SearchResult implements Serializable{
	
	private Long recordCount;
	private Integer totalPage;
	private List<SearchItem> itemList;
	public Long getRecordCount() {
		return recordCount;
	}
	public void setRecordCount(Long recordCount) {
		this.recordCount = recordCount;
	}
	public Integer getTotalPage() {
		return totalPage;
	}
	public void setTotalPage(Integer totalPage) {
		this.totalPage = totalPage;
	}
	public List<SearchItem> getItemList() {
		return itemList;
	}
	public void setItemList(List<SearchItem> itemList) {
		this.itemList = itemList;
	}
	
}
