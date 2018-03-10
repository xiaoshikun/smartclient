package com.aspire.component.entries;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.io.Serializable;
import java.util.List;
@JsonInclude(Include.NON_NULL)
public class Params implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1932992944794467423L;
	private Integer iconstyle;
	private List<Entry> items;
	public Integer getIconstyle() {
		return iconstyle;
	}
	public void setIconstyle(Integer iconstyle) {
		this.iconstyle = iconstyle;
	}
	public List<Entry> getItems() {
		return items;
	}
	public void setItems(List<Entry> items) {
		this.items = items;
	}


}
