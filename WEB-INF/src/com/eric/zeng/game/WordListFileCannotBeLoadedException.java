package com.eric.zeng.game;

public class WordListFileCannotBeLoadedException extends Exception {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public WordListFileCannotBeLoadedException(String errorMsg){
    	_errorcode = ErrorCode.FILE_NOT_FOUND;
    	setErrormsg(errorMsg);
    }
	public void setErrormsg(String errormsg) {
		this._errormsg = errormsg;
	}
	public String getErrormsg() {
		return _errormsg;
	}
	public int getErrorcode(){
		return _errorcode;
	}
	private String _errormsg;
	private int _errorcode;
}
