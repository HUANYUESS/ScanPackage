package com.sugoos.scanpackage.dto;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

/**
 * 通用Rest接口结果类
 */
public class RestResult<T> {

    private final Integer code;
    private final String msg;
    private final T data;
    private final LocalDateTime timestamp = LocalDateTime.now();
    
   /* 常用静态字段 */
	public static final RestResult<?> OK = new RestResult<>(HttpStatus.OK);
    public static final RestResult<?> FAIL = new RestResult<>(HttpStatus.INTERNAL_SERVER_ERROR);

    /* 常用静态方法 */
    public static <R> RestResult<R> ok(R data) {
        return new RestResult<>(HttpStatus.OK, data);
    }

    public static RestResult<?> fail(String msg) {
        return new RestResult<>(500, msg);
    }

    /* 构造方法 */
	public RestResult(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }
    
    public RestResult(Integer code, String msg) {
        this(code, msg, null);
    }

    public RestResult(HttpStatus httpStatus) {
        this(httpStatus.value(), httpStatus.getReasonPhrase());
    }

    public RestResult(HttpStatus httpStatus, T data) {
        this(httpStatus.value(), httpStatus.getReasonPhrase(), data);
    }

    /* Getter */
    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
    
    public Object getData() {
        return data;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "Result{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                ", timestamp=" + timestamp +
                '}';
    }
}

