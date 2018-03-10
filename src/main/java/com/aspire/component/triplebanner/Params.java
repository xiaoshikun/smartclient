package com.aspire.component.triplebanner;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
@JsonInclude(Include.NON_NULL)
public class Params implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6653677913063921620L;
	private List<Banner> items;
	public List<Banner> getItems() {
		return items;
	}
	public void setItems(List<Banner> items) {
		this.items = items;
	}
}
