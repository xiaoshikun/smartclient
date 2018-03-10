package com.aspire.component.dividerline;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.io.Serializable;
@JsonInclude(Include.NON_NULL)
public class Params implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4355433034599607778L;
	private Integer height;
	private String color;
	public Integer getHeight() {
		return height;
	}
	public void setHeight(Integer height) {
		this.height = height;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
}
