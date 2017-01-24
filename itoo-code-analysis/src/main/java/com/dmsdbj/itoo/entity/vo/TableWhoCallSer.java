package com.dmsdbj.itoo.entity.vo;

import java.util.List;

public class TableWhoCallSer {
	private String target;
	private String targetPackage;
	private List<String> source;
	private List<String> sourcePackage;
	private int sercount;

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getTargetPackage() {
		return targetPackage;
	}

	public void setTargetPackage(String targetPackage) {
		this.targetPackage = targetPackage;
	}

	public List<String> getSource() {
		return source;
	}

	public void setSource(List<String> source) {
		this.source = source;
	}

	public List<String> getSourcePackage() {
		return sourcePackage;
	}

	public void setSourcePackage(List<String> sourcePackage) {
		this.sourcePackage = sourcePackage;
	}

	public int getSercount() {
		return sercount;
	}

	public void setSercount(int sercount) {
		this.sercount = sercount;
	}

}