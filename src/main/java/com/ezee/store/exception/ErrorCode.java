package com.ezee.store.exception;

public enum ErrorCode {

	ID_NOT_FOUND_EXCEPTION(111, "Id not Found"), EMPTY_RESULT_DATA_EXCEPTION(112, "List is Empty"),
	KEY_NOT_FOUND_EXCEPTION(113, "Key not Found"),
	CREATE_FAILED_EXCEPTION(114, "An Error Occured on creating data"),
	UPDATE_FAILED_EXCEPTION(115, "An Error Occured on updating data."),
	DELETE_FAILED_EXCEPTION(116, "An Error Occured on deleting data."),
	VALUE_NOT_BE_NULL_EXCEPTION(116, "A Value should not be null or default value"),
	ID_ALREADY_EXISTS(117, "Id Already EXists in the database."), DATABASE_ERROR(118, "Database operation failed.");

	private int code;
	private String message;

	ErrorCode(int code, String message) {
		this.code = code;
		this.message = message;
	}

	public int getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}
}
