package com.aspire.pojo.out;

import java.io.Serializable;
import java.util.List;

public class Pagelayout implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4303592244674446636L;

	private Integer pageId;
	private String caption;
	private List<?> items;
	public Integer getPageId() {
		return pageId;
	}
	public void setPageId(Integer pageId) {
		this.pageId = pageId;
	}
	public String getCaption() {
		return caption;
	}
	public void setCaption(String caption) {
		this.caption = caption;
	}
	public List<?> getItems() {
		return items;
	}
	public void setItems(List<?> items) {
		this.items = items;
	}
	@Override
	public String toString() {
		return "Pagelayout [pageId=" + pageId + ", caption=" + caption + ", items=" + items + "]";
	}

	
}
