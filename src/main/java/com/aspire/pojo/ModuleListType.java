package com.aspire.pojo;

import java.io.Serializable;

public class ModuleListType implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3314120817457701862L;

	private Integer id;
	private String name;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public String toString() {
		return "ModuleListType [id=" + id + ", name=" + name + "]";
	}
}
