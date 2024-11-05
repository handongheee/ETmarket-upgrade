package kr.co.sist.etmarket.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.concurrent.Callable;

@RestController
public class AsyncController {

    @GetMapping("/async")
    public Callable<String> asyncMethod() {
        return () -> {
            // 비동기 작업 수행
            Thread.sleep(5000); // 예시: 5초 지연
            return "Async response";
        };
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<String> handleIOException(IOException ex) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("IOException occurred: " + ex.getMessage());
    }
}
