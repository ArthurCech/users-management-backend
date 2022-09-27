package dev.arthurcech.supportportal.exception;

import static dev.arthurcech.supportportal.constant.ExceptionConstant.ACCOUNT_DISABLED;
import static dev.arthurcech.supportportal.constant.ExceptionConstant.ACCOUNT_LOCKED;
import static dev.arthurcech.supportportal.constant.ExceptionConstant.ERROR_PROCESSING_FILE;
import static dev.arthurcech.supportportal.constant.ExceptionConstant.INCORRET_CREDENTIALS;
import static dev.arthurcech.supportportal.constant.ExceptionConstant.INTERNAL_SERVER_ERROR_MSG;
import static dev.arthurcech.supportportal.constant.ExceptionConstant.METHOD_IS_NOT_ALLOWED;
import static dev.arthurcech.supportportal.constant.ExceptionConstant.NOT_ENOUGH_PERMISSION;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.METHOD_NOT_ALLOWED;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import java.io.IOException;
import java.util.Objects;

import javax.persistence.NoResultException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.auth0.jwt.exceptions.TokenExpiredException;

import dev.arthurcech.supportportal.domain.HttpResponse;
import dev.arthurcech.supportportal.exception.domain.EmailExistException;
import dev.arthurcech.supportportal.exception.domain.EmailNotFoundException;
import dev.arthurcech.supportportal.exception.domain.NotAnImageFileException;
import dev.arthurcech.supportportal.exception.domain.UserNotFoundException;
import dev.arthurcech.supportportal.exception.domain.UsernameExistException;

@RestControllerAdvice
public class ExceptionHandling implements ErrorController {

	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	@ExceptionHandler(DisabledException.class)
	public ResponseEntity<HttpResponse> accountDisabledException() {
		return createHttpResponse(BAD_REQUEST, ACCOUNT_DISABLED);
	}

	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<HttpResponse> badCredentialsException() {
		return createHttpResponse(BAD_REQUEST, INCORRET_CREDENTIALS);
	}

	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<HttpResponse> accessDeniedException() {
		return createHttpResponse(FORBIDDEN, NOT_ENOUGH_PERMISSION);
	}

	@ExceptionHandler(LockedException.class)
	public ResponseEntity<HttpResponse> lockedException() {
		return createHttpResponse(UNAUTHORIZED, ACCOUNT_LOCKED);
	}

	@ExceptionHandler(TokenExpiredException.class)
	public ResponseEntity<HttpResponse> tokenExpiredException(TokenExpiredException e) {
		return createHttpResponse(UNAUTHORIZED, e.getMessage());
	}

	@ExceptionHandler(EmailExistException.class)
	public ResponseEntity<HttpResponse> emailExistException(EmailExistException e) {
		return createHttpResponse(BAD_REQUEST, e.getMessage());
	}

	@ExceptionHandler(UsernameExistException.class)
	public ResponseEntity<HttpResponse> usernameExistException(UsernameExistException e) {
		return createHttpResponse(BAD_REQUEST, e.getMessage());
	}

	@ExceptionHandler(EmailNotFoundException.class)
	public ResponseEntity<HttpResponse> emailNotFoundException(EmailNotFoundException e) {
		return createHttpResponse(BAD_REQUEST, e.getMessage());
	}

	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<HttpResponse> userNotFoundException(UserNotFoundException e) {
		return createHttpResponse(BAD_REQUEST, e.getMessage());
	}

//	@ExceptionHandler(NoHandlerFoundException.class)
//	public ResponseEntity<HttpResponse> noHandlerFoundException(NoHandlerFoundException e) {
//		return createHttpResponse(BAD_REQUEST, e.getMessage());
//	}

	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<HttpResponse> httpRequestMethodNotSupportedException(
			HttpRequestMethodNotSupportedException e) {
		HttpMethod supportedMethod = Objects.requireNonNull(e.getSupportedHttpMethods()).iterator().next();
		return createHttpResponse(METHOD_NOT_ALLOWED, String.format(METHOD_IS_NOT_ALLOWED, supportedMethod));
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<HttpResponse> internalServerErrorException(Exception e) {
		LOGGER.error(e.getMessage());
		return createHttpResponse(INTERNAL_SERVER_ERROR, INTERNAL_SERVER_ERROR_MSG);
	}

	@ExceptionHandler(NoResultException.class)
	public ResponseEntity<HttpResponse> notFoundException(NoResultException e) {
		LOGGER.error(e.getMessage());
		return createHttpResponse(NOT_FOUND, e.getMessage());
	}

	@ExceptionHandler(IOException.class)
	public ResponseEntity<HttpResponse> iOException(IOException e) {
		LOGGER.error(e.getMessage());
		return createHttpResponse(INTERNAL_SERVER_ERROR, ERROR_PROCESSING_FILE);
	}

	@ExceptionHandler(NotAnImageFileException.class)
	public ResponseEntity<HttpResponse> notAnImageFileException(NotAnImageFileException e) {
		return createHttpResponse(BAD_REQUEST, e.getMessage());
	}

	private ResponseEntity<HttpResponse> createHttpResponse(HttpStatus httpStatus, String message) {
		HttpResponse httpResponse = new HttpResponse(httpStatus.value(), httpStatus,
				httpStatus.getReasonPhrase().toUpperCase(), message);
		return new ResponseEntity<>(httpResponse, httpStatus);
	}

}
