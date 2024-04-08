package plus.suja.teach.teachshop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import plus.suja.teach.teachshop.exception.HttpException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ErrorHandlerController {
    ObjectMapper objectMapper = new ObjectMapper();

    @ExceptionHandler({HttpException.class})
    public void handle(HttpServletResponse response, HttpException ex) throws IOException {
        response.setStatus(ex.getStatusCode());
        Map<String, Object> jsonObject = new HashMap<>();
        jsonObject.put("message", ex.getMessage());
        response.getOutputStream().write(objectMapper.writeValueAsBytes(jsonObject));
        response.getOutputStream().flush();
    }
}
