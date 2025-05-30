package com.ezee.store.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ServiceException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	private ErrorCode errorCode;
	private Object object;
	
	public ServiceException(ErrorCode errorCode) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
	}

	public ServiceException(ErrorCode errorCode, Object object) {
		this.errorCode = errorCode;
		this.object = object;
	}
	
	public ServiceException(Object object) {
		this.object = object;
	}

	public String getMessage() {
		return errorCode.getMessage();
	}

	public int getstatusCode() {
		return errorCode.getCode();
	}
	
	public Object getObject() {
		return object;
	}

	public ErrorCode getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(ErrorCode errorCode) {
		this.errorCode = errorCode;
	}

}
