package com.ezee.store.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.ezee.store.util.DateTimeUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class ResponseException<T> {
	private Integer status;
	private Integer errorCode;
	private String message;
	private Object description;
	private String responseTime;
	private T data;

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(Integer errorCode) {
		this.errorCode = errorCode;
	}

	public String getResponseTime() {
		return responseTime;
	}

	public void setResponseTime(String timestamp) {
		this.responseTime = DateTimeUtil.dateTimeFormat(LocalDateTime.now());
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public Object getDescription() {
		return description;
	}

	public void setDescription(Object description) {
		this.description = description;
	}

	public static <T> ResponseEntity<?> success(T data) {
		ResponseException<T> responseException = new ResponseException<>();
		responseException.setData(data);
		responseException.setResponseTime(DateTimeUtil.dateTimeFormat(LocalDateTime.now()));
		responseException.setStatus(1);

		return ResponseEntity.status(HttpStatus.OK).body(responseException);
	}

	public static <T> ResponseEntity<?> failure(ErrorCode errorCode) {
		ResponseException<T> responseException = new ResponseException<>();
		responseException.setStatus(0);
		responseException.setErrorCode(errorCode.getCode());
		responseException.setMessage(errorCode.getMessage());
		responseException.setResponseTime(DateTimeUtil.dateTimeFormat(LocalDateTime.now()));

		return ResponseEntity.status(HttpStatus.OK).body(responseException);
	}

	public static <T> ResponseEntity<?> failure(String message) {
		ResponseException<T> responseException = new ResponseException<>();
		responseException.setStatus(0);
		responseException.setErrorCode(400);
		responseException.setMessage(message);
		responseException.setResponseTime(DateTimeUtil.dateTimeFormat(LocalDateTime.now()));

		return ResponseEntity.status(HttpStatus.OK).body(responseException);
	}

	public static <T> ResponseEntity<?> failure(ErrorCode errorCode, String message) {
		ResponseException<T> responseException = new ResponseException<>();
		responseException.setStatus(0);
		responseException.setErrorCode(errorCode.getCode());
		responseException.setMessage(errorCode.getMessage() + message);
		responseException.setResponseTime(DateTimeUtil.dateTimeFormat(LocalDateTime.now()));

		return ResponseEntity.status(HttpStatus.OK).body(responseException);
	}
}