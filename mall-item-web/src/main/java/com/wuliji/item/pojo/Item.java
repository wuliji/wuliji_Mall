package com.wuliji.item.pojo;

import com.wuliji.pojo.TbItem;

public class Item extends TbItem{
	
	/**
	 * 初始化参数属性
	 * @param tbItem
	 */
	public Item(TbItem tbItem) {
		this.setId(tbItem.getId());
		this.setSellPoint(tbItem.getSellPoint());
		this.setTitle(tbItem.getTitle());
		this.setPrice(tbItem.getPrice());
		this.setNum(tbItem.getNum());
		this.setBarcode(tbItem.getBarcode());
		this.setImage(tbItem.getImage());
		this.setCid(tbItem.getCid());
		this.setStatus(tbItem.getStatus());
		this.setCreated(tbItem.getCreated());
		this.setUpdated(tbItem.getUpdated());
	}
	
	public String[] getImages() {
		String image2 = this.getImage();
		if(image2 != null && !"".equals(image2)) {
			String[] string = image2.split(",");
			return string;
		}
		return null;
	}
}
