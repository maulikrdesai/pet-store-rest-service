package com.petstore.utils;

import java.net.BindException;
import java.text.MessageFormat;
import java.text.ParseException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;

import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.petstore.servlet.exception.AuthenticationFailedException;
import com.petstore.servlet.exception.AuthenticationRequiredException;

/***
 * This class can be used to build REST response for an exception
 * 
 * @author mdesai
 *
 */
public class ExceptionUtils {

	private static final String EXCEPTION_MSG_FORMAT = "{0} Caused By:[{1}]";
	private static final String CONSTRAINT_VIOLATION_MSG_FORMAT = "{0} : {1}";

	public static HttpStatus resolveStatus(Throwable ex) {
		ex = getRootCause(ex);

		if (ex instanceof HttpStatusCodeException)
			return ((HttpStatusCodeException) ex).getStatusCode();

		if (ex instanceof IllegalArgumentException || ex instanceof IllegalAccessException
				|| ex instanceof ParseException || ex instanceof ValidationException
				|| ex instanceof MethodArgumentNotValidException || ex instanceof MissingServletRequestPartException
				|| ex instanceof BindException || ex instanceof MissingServletRequestParameterException
				|| ex instanceof ServletRequestBindingException || ex instanceof TypeMismatchException
				|| ex instanceof HttpMessageNotReadableException)
			return HttpStatus.BAD_REQUEST;
		if (ex instanceof AuthenticationFailedException | ex instanceof AuthenticationRequiredException)
			return HttpStatus.UNAUTHORIZED;
		// JSON generation is not bad request it is internal server error
		if (ex instanceof JsonProcessingException && !(ex instanceof JsonGenerationException))
			return HttpStatus.BAD_REQUEST;
		if (ex instanceof HttpRequestMethodNotSupportedException)
			return HttpStatus.METHOD_NOT_ALLOWED;
		else if (ex instanceof HttpMediaTypeNotSupportedException)
			return HttpStatus.UNSUPPORTED_MEDIA_TYPE;
		else if (ex instanceof HttpMediaTypeNotAcceptableException)
			return HttpStatus.NOT_ACCEPTABLE;
		else if (ex instanceof MissingPathVariableException || ex instanceof ConversionNotSupportedException
				|| ex instanceof HttpMessageNotWritableException)
			return HttpStatus.INTERNAL_SERVER_ERROR;
		else if (ex instanceof NoHandlerFoundException)
			return HttpStatus.NOT_FOUND;
		else if (ex instanceof AsyncRequestTimeoutException)
			return HttpStatus.SERVICE_UNAVAILABLE;

		return HttpStatus.INTERNAL_SERVER_ERROR;
	}

	/***
	 * Formulate response string from provided exception based on type of exception
	 * it is
	 * 
	 * @param t
	 * @return
	 */
	public static String buildExceptionMessage(Throwable t) {
		t = getRootCause(t);
		if (t instanceof ConstraintViolationException) {
			ConstraintViolationException e = (ConstraintViolationException) t;
			StringBuffer message = new StringBuffer(
					e.getConstraintViolations().size() + " constraint violation(s) occurred.\n");
			for (ConstraintViolation<?> cv : e.getConstraintViolations())
				message.append(
						MessageFormat.format(CONSTRAINT_VIOLATION_MSG_FORMAT, cv.getPropertyPath(), cv.getMessage())
								+ " \n");
			return message.toString();
		}
		if (t instanceof MethodArgumentNotValidException) {
			MethodArgumentNotValidException e = (MethodArgumentNotValidException) t;
			StringBuffer message = new StringBuffer(
					e.getBindingResult().getAllErrors().size() + " constraint violation(s) occurred.\n");

			for (ObjectError error : e.getBindingResult().getAllErrors())
				message.append(MessageFormat.format(CONSTRAINT_VIOLATION_MSG_FORMAT,
						error.getCodes()[0].replace("NotNull.", ""), error.getDefaultMessage()) + " \n");

			return message.toString();
		}

		return t.getMessage();
	}

	/***
	 * Traverse thru all causes and returns the root cause of the Exception
	 * 
	 * @param cause
	 * @return
	 */
	private static Throwable getRootCause(Throwable cause) {
		if (cause.getCause() == null)
			return cause;
		return getRootCause(cause.getCause());
	}

	/***
	 * Recursively build message by appending every cause to the initial message
	 * 
	 * @param message
	 * @param cause
	 * @return
	 */
	public static String buildRecursiveMessage(String message, Throwable cause) {
		if (cause == null)
			return message;
		message = MessageFormat.format(EXCEPTION_MSG_FORMAT, message, cause.getMessage());
		return buildRecursiveMessage(message, cause.getCause());
	}
}
