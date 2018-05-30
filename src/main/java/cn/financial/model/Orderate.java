package cn.financial.model;

public class Orderate {

	private String id;    
	
    private String sum;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSum() {
		return sum;
	}

	public void setSum(String sum) {
		this.sum = sum;
	}

	public Orderate(String id, String sum) {
		super();
		this.id = id;
		this.sum = sum;
	}

	public Orderate() {
		super();
		// TODO Auto-generated constructor stub
	}
    
    
}
