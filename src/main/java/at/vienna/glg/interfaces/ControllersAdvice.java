package at.vienna.glg.interfaces;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllersAdvice {
	private static final Logger logger = LoggerFactory.getLogger(ControllersAdvice.class);

	@ExceptionHandler(Throwable.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public String handleServerErrors(Throwable e) {

		logger.error("Unhandled exception caught: {}", e);
		return "Internal server error";
	}
}