package com.petstore;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.petstore.models.ApiResponse;
import com.petstore.utils.ExceptionUtils;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

	private static final Log logger = LogFactory.getLog(RestExceptionHandler.class);

	@ExceptionHandler
	protected ResponseEntity<Object> handleException(Throwable ex) {
		logger.error(ex.getMessage(), ex);
		HttpStatus resolvedStatus = ExceptionUtils.resolveStatus(ex);
		return new ResponseEntity<Object>(
				new ApiResponse<Void>(resolvedStatus, ExceptionUtils.buildExceptionMessage(ex)), resolvedStatus);
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		logger.error(ex.getMessage(), ex);
		HttpStatus resolvedStatus = HttpStatus.BAD_REQUEST;
		return new ResponseEntity<Object>(
				new ApiResponse<Void>(resolvedStatus, ExceptionUtils.buildExceptionMessage(ex)), resolvedStatus);
	}

}
