package com.aspire.component.horizScrollbanner;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.io.Serializable;
import java.util.List;
@JsonInclude(Include.NON_NULL)
public class Params implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7122577458693038200L;
	private List<Banner> items;
	public List<Banner> getItems() {
		return items;
	}
	public void setItems(List<Banner> items) {
		this.items = items;
	}

}
