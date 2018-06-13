package cn.financial.exception;

public class FormulaAnalysisException extends Exception{

	

	/**
	 * 
	 */
	private static final long serialVersionUID = 6545135760252811417L;

	public FormulaAnalysisException(String msg){
		super(msg);
	}
	
	public FormulaAnalysisException(String msg,Throwable cause){
		super(msg,cause);
	}
}
