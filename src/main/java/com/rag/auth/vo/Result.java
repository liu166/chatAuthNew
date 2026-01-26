package com.rag.auth.vo;

import lombok.Data;

@Data
public class Result<T> {

    private int status;
    private String message;
    private T data;
    private Long id;

    /* ===== 成功 ===== */
    public static <T> Result<T> ok() {
        Result<T> r = new Result<>();
        r.status = 200;
        r.message = "success";
        return r;
    }

    public static <T> Result<T> ok(T data) {
        Result<T> r = new Result<>();
        r.status = 200;
        r.message = "success";
        r.data = data;
        return r;
    }

    public static <T> Result<T> ok(Long id) {
        Result<T> r = new Result<>();
        r.status = 200;
        r.message = "success";
        r.id = id;
        return r;
    }


    /* ===== 失败 ===== */
    public static <T> Result<T> error(String message) {
        Result<T> r = new Result<>();
        r.status = 500;
        r.message = message;
        return r;
    }

    public static <T> Result<T> error() {
        Result<T> r = new Result<>();
        r.status = 500;
        r.message = "error";
        return r;
    }

    public static <T> Result<T> error(int status, String message) {
        Result<T> r = new Result<>();
        r.status = status;
        r.message = message;
        return r;
    }

}