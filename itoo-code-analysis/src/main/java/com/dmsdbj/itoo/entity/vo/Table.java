package com.dmsdbj.itoo.entity.vo;

import java.util.List;

public class Table {
	private String source;
	private String sourcePackage;
	private List<String> target;
	private List<String> targetPackage;
	private int tarcount;
	
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getSourcePackage() {
		return sourcePackage;
	}
	public void setSourcePackage(String sourcePackage) {
		this.sourcePackage = sourcePackage;
	}
	public List<String> getTarget() {
		return target;
	}
	public void setTarget(List<String> target) {
		this.target = target;
	}
	public List<String> getTargetPackage() {
		return targetPackage;
	}
	public void setTargetPackage(List<String> targetPackage) {
		this.targetPackage = targetPackage;
	}
	public int getTarcount() {
		return tarcount;
	}
	public void setTarcount(int tarcount) {
		this.tarcount = tarcount;
	}
		
}
