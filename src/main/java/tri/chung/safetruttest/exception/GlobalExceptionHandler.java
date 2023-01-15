package tri.chung.safetruttest.exception;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<?> resourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
		logger.error(ex.getMessage());
		ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(), request.getDescription(false));
		return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
      List<String> errors = ex.getBindingResult().getFieldErrors()
      .stream().map(FieldError::getDefaultMessage).collect(Collectors.toList());
      List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
      
      Map<String, String> errorMap = new HashMap<>();
      for (FieldError error: fieldErrors) {
    	  errorMap.put(error.getField(), error.getDefaultMessage());
      }
      Map<String, Map<String, String>>  errorsMaps = getErrorsMap(errorMap);
      //Map<String, List<String>>  errorsMap = getErrorsMap(errors);
		//ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(), request.getDescription(false));
      logger.error(errorsMaps);
		return new ResponseEntity<>(errorsMaps, HttpStatus.BAD_REQUEST);
	}
//	@ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<Map<String, List<String>>> handleValidationErrors(MethodArgumentNotValidException ex) {
//        List<String> errors = ex.getBindingResult().getFieldErrors()
//                .stream().map(FieldError::getDefaultMessage).collect(Collectors.toList());
//        return new ResponseEntity<>(getErrorsMap(errors), new HttpHeaders(), HttpStatus.BAD_REQUEST);
//    }
//
//    private Map<String, List<String>> getErrorsMap(List<String> errors) {
//        Map<String, List<String>> errorResponse = new HashMap<>();
//        errorResponse.put("errors", errors);
//        return errorResponse;
//    }
    
    private Map<String, Map<String, String>> getErrorsMap(Map<String, String> errors) {
        Map<String, Map<String, String>> errorResponse = new HashMap<>();
        errorResponse.put("errors", errors);
        return errorResponse;
    }
    
    
	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> globleExcpetionHandler(Exception ex, WebRequest request) {
		logger.error(ex.getMessage());
		ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(), request.getDescription(false));
		return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	
}
