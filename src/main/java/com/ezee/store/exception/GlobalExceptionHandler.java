package com.ezee.store.exception;

import java.time.LocalDateTime;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.ezee.store.util.DateTimeUtil;

@RestControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(ServiceException.class)
	public ResponseEntity<?> handleServiceException(ServiceException exception) {
		ResponseException<ServiceException> responseException = new ResponseException<>();
		responseException.setStatus(0);
		responseException.setErrorCode(exception.getstatusCode());
		responseException.setMessage(exception.getMessage());
		responseException.setDescription(exception.getObject());
		responseException.setResponseTime(DateTimeUtil.dateTimeFormat(LocalDateTime.now()));

		return ResponseEntity.ok(responseException);
	}
	
//	@ExceptionHandler(SQLException.class)
//	public ResponseEntity<?> handleSqlException(SQLException exception) {
//		ResponseException<ServiceException> responseException = new ResponseException<>();
//		responseException.setStatus(0);
//		responseException.setErrorCode(400);
//		responseException.setMessage(exception.getMessage());
//		responseException.setResponseTime(DateTimeUtil.dateTimeFormat(LocalDateTime.now()));
//
//		return ResponseEntity.ok(responseException);
//	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> handleException(Exception exception) {
		ResponseException<ServiceException> responseException = new ResponseException<>();
		responseException.setStatus(0);
//		responseException.setDescription(exception.getStackTrace());
		responseException.setMessage(exception.getMessage());
		responseException.setResponseTime(DateTimeUtil.dateTimeFormat(LocalDateTime.now()));

		return ResponseEntity.ok(responseException);
	}
}
