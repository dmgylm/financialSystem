package cn.springmvc.test;

public class HtmlMerged1 {

	private Integer firstRowNum;
	private Integer lastRowNum;
	private String text;

	public HtmlMerged1(Integer firstRowNum, Integer lastRowNum, String text) {
		this.firstRowNum = firstRowNum;
		this.lastRowNum = lastRowNum;
		this.text = text;
	}

	public Integer getFirstRowNum() {
		return firstRowNum;
	}

	public void setFirstRowNum(Integer firstRowNum) {
		this.firstRowNum = firstRowNum;
	}

	public Integer getLastRowNum() {
		return lastRowNum;
	}

	public void setLastRowNum(Integer lastRowNum) {
		this.lastRowNum = lastRowNum;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}
