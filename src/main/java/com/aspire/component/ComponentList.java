package com.aspire.component;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
/**
 * 这个类是与客户端输出工程包页面控件交互的类
 * @author xiaos
 *
 * @param <T>
 */
public class ComponentList<T>  implements Serializable{

	private static final long serialVersionUID = -7946916427221803285L;
	private String name;//控件名
	private String type;//控件类型
	private T params;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public T getParams() {
		return params;
	}
	public void setParams(T params) {
		this.params = params;
	}

	
}
