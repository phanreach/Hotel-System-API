package com.example.hotel_system.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL) // omit null fields in JSON
public class ApiResponse<T> {

    private final boolean success;
    private final String message;
    private final String errorCode;
    private final int status;
    private final T data;
    private final Map<String, Object> meta;
    @JsonFormat(
            shape = JsonFormat.Shape.STRING,
            pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX",
            timezone = "Asia/Phnom_Penh"
    )
    private final Instant timestamp;
    private List<Map<String, String>> errors;

    @Builder
    public ApiResponse(boolean success,
                       String message,
                       String errorCode,
                       int status,
                       T data,
                       Map<String, Object> meta,
                       Instant timestamp,List<Map<String, String>> errors) {
        this.success = success;
        this.message = message;
        this.errorCode = errorCode;
        this.status = status;
        this.data = data;
        this.meta = meta;
        this.errors = errors;
        this.timestamp = timestamp != null ? timestamp : Instant.now();
    }

    // --- Factory Methods for Success Responses ---

    public static <T> ApiResponse<T> success(T data, String message) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .status(200)
                .data(data)
                .timestamp(Instant.now())
                .build();
    }

    public static <T> ApiResponse<T> success(T data, String message, Map<String, Object> meta) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .status(200)
                .data(data)
                .meta(meta)
                .timestamp(Instant.now())
                .build();
    }

    // --- Factory Methods for Error Responses ---

    public static <T> ApiResponse<T> error(String message, String errorCode, int status) {
        return ApiResponse.<T>builder()
                .success(false)
                .message(message)
                .errorCode(errorCode)
                .status(status)
                .timestamp(Instant.now())
                .build();
    }
    public static <T> ApiResponse<T> error(String message, String errorCode, int status, List<Map<String, String>> errors) {
        return ApiResponse.<T>builder()
                .success(false)
                .message(message)
                .errorCode(errorCode)
                .status(status)
                .timestamp(Instant.now())
                .errors(errors) // ✅ only set when provided
                .build();
    }

    public static <T> ApiResponse<T> error(String message, String errorCode, int status, Map<String, Object> meta) {
        return ApiResponse.<T>builder()
                .success(false)
                .message(message)
                .errorCode(errorCode)
                .status(status)
                .meta(meta)
                .timestamp(Instant.now())
                .build();
    }
}

