package com.ydjk.msdemo.exception;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.ydjk.msdemo.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.ShiroException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;
import java.sql.SQLIntegrityConstraintViolationException;

//捕获全局异常的处理
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandle {

    // 捕捉shiro的异常
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(ShiroException.class)
    public Result handle401(ShiroException e) {
        log.error("ShiroException:"+e.getMessage());
        return Result.fail(HttpStatus.UNAUTHORIZED.value(), "没有操作权限", null);
    }

    // 捕捉shiro的异常
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UnauthenticatedException.class)
    public Result handle401(UnauthenticatedException e) {
        return Result.fail(HttpStatus.UNAUTHORIZED.value(), "没有权限访问", null);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    public Result handler(BindException e){
        BindingResult bindingResult = e.getBindingResult();
        //这一步是把异常的信息最简化
        ObjectError objectError = bindingResult.getAllErrors().stream().findFirst().get();
        String errorMsg = objectError.getDefaultMessage();
        return Result.fail(HttpStatus.BAD_REQUEST.value(), errorMsg,null);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = SQLIntegrityConstraintViolationException.class)
    public Result handler(SQLIntegrityConstraintViolationException e) throws IOException {
        log.error("SQLIntegrityConstraintViolationException:",e);
        return Result.fail(HttpStatus.BAD_REQUEST.value(), "值重复，操作失败",null);
    }
    /**
     * 实体校验错误异常处理
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public Result handler(MethodArgumentNotValidException e) throws IOException {
//        log.error("运行时异常:-------------->",e);
        BindingResult bindingResult = e.getBindingResult();
        //这一步是把异常的信息最简化
        ObjectError objectError = bindingResult.getAllErrors().stream().findFirst().get();
        return Result.fail(HttpStatus.BAD_REQUEST.value(),objectError.getDefaultMessage(),null);
    }

    /**
     * 处理Assert的异常
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = IllegalArgumentException.class)
    public Result handler(IllegalArgumentException e) throws IOException {
//        log.error("Assert异常:-------------->{}",e.getMessage());
        return Result.fail(HttpStatus.BAD_REQUEST.value(),e.getMessage(),null);
    }

    /**
     * 处理 sql异常
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = UncategorizedSQLException.class)
    public Result handler(UncategorizedSQLException e){
        log.error(e.getMessage());
        return Result.fail(HttpStatus.BAD_REQUEST.value(),"数据异常",null);
    }

    //运行时错误处理
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = RuntimeException.class)
    public Result handle(RuntimeException e){
        log.error(e.getMessage());
        return Result.fail(HttpStatus.BAD_REQUEST.value(),"服务异常",null);
    }

    /**
     * token过期异常
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = TokenExpiredException.class)
    public Result handler(TokenExpiredException e) throws IOException {
        return Result.fail(HttpStatus.BAD_REQUEST.value(),"token已经过期，请重新登录",null);
    }
}