package com.aspire.component.tabnavigator;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class Params implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1972261855372312189L;
	private boolean aligntop;
	private boolean horizscroll;
	private boolean underlinetab;
	private Integer defaulttab;
	private List<TabSpec> tabs;
	public boolean isAligntop() {
		return aligntop;
	}
	public void setAligntop(boolean aligntop) {
		this.aligntop = aligntop;
	}
	public boolean isHorizscroll() {
		return horizscroll;
	}
	public void setHorizscroll(boolean horizscroll) {
		this.horizscroll = horizscroll;
	}
	public boolean isUnderlinetab() {
		return underlinetab;
	}
	public void setUnderlinetab(boolean underlinetab) {
		this.underlinetab = underlinetab;
	}
	public Integer getDefaulttab() {
		return defaulttab;
	}
	public void setDefaulttab(Integer defaulttab) {
		this.defaulttab = defaulttab;
	}
	public List<TabSpec> getTabs() {
		return tabs;
	}
	public void setTabs(List<TabSpec> tabs) {
		this.tabs = tabs;
	}
	@Override
	public String toString() {
		return "Params [aligntop=" + aligntop + ", horizscroll=" + horizscroll + ", underlinetab=" + underlinetab
				+ ", defaulttab=" + defaulttab + ", tabs=" + tabs + "]";
	}



}
