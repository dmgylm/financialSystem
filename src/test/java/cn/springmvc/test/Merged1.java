package cn.springmvc.test;

public class Merged1 {
	private Integer colspan;
	private Integer rowspan;
	private String text;

	public Merged1(Integer colspan, Integer rowspan) {
		this.colspan = colspan;
		this.rowspan = rowspan;
	}

	public Integer getColspan() {
		return colspan;
	}

	public void setColspan(Integer colspan) {
		this.colspan = colspan;
	}

	public int getRowspan() {
		return rowspan;
	}

	public void setRowspan(Integer rowspan) {
		this.rowspan = rowspan;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}
