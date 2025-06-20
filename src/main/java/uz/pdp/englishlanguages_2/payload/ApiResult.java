package uz.pdp.englishlanguages_2.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.ToString;

import java.util.Collections;
import java.util.List;

@Getter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResult<T> {
    private boolean success;
    private String message;
    private T data;
    private List<ErrorData> errors;

    private ApiResult() {
        this.success = true;
    }

    private ApiResult(String message) {
        this();
        this.message = message;
    }

    private ApiResult(String message, T data) {
        this(message);
        this.data = data;
    }

    private ApiResult(T data, boolean isData) {
        this();
        this.data = data;
    }

    private ApiResult(List<ErrorData> errors) {
        this.errors = errors;
    }

    private ApiResult(String errorMsg, Integer errorCode) {
        this.errors = Collections.singletonList(new ErrorData(errorMsg, errorCode));
    }

    public static <T> ApiResult<T> success() {
        return new ApiResult<T>();
    }

    public static <T extends CharSequence> ApiResult<T> successMessage(T message) {
        return new ApiResult<>(message, true);
    }

    public static <T> ApiResult<T> success(String message) {
        return new ApiResult<>(message);
    }

    public static <T> ApiResult<T> success(T data) {
        return new ApiResult<>(data, true);
    }

    public static <T> ApiResult<T> success(String message, T data) {
        return new ApiResult<>(message, data);
    }

    public static ApiResult<List<ErrorData>> errorResponse(String errorMsg, Integer errorCode) {
        return new ApiResult<>(errorMsg, errorCode);
    }

    public static ApiResult<List<ErrorData>> errorResponse(List<ErrorData> errors) {
        return new ApiResult<>(errors);
    }
}
