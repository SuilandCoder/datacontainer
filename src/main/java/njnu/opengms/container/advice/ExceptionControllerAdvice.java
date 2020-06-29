package njnu.opengms.container.advice;

import njnu.opengms.container.bean.JsonResult;
import njnu.opengms.container.exception.MyException;
import njnu.opengms.container.utils.ResultUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;


/**
 * @ClassName ExceptionControllerAdvice
 * @Description 注意这里只有服务器内部产生的错误，才返回了500的状态码，其他被捕获到了的异常的状态码都是200
 * @Author sun_liber
 * @Date 2018/11/28
 * @Version 1.0.0
 */
@ControllerAdvice
@ResponseBody
public class ExceptionControllerAdvice {

    /**
     * 404 HttpStatus.NOT_FOUND
     * attention : 如果想捕获NoHandlerFoundException，必须将静态资源的映射给屏蔽掉
     * 因为Spring Boot 中, 当用户访问了一个不存在的链接时, Spring 默认会将页面重定向到   /error 上, 而不会抛出异常
     *
     * @param e 未找到资源
     *
     * @return
     */
    @ResponseStatus (HttpStatus.NOT_FOUND)
    @ExceptionHandler ({NoHandlerFoundException.class})
    public JsonResult handleNoHandlerFoundException(NoHandlerFoundException e) {
        return ResultUtils.error(HttpStatus.NOT_FOUND.value(), e.getMessage());
    }

    /**
     * 400 HttpStatus.BAD_REQUEST
     *
     * @param e 参数验证失败
     *
     * @return 注意这里BindingResult的验证错误可能有多个，这里默认返回了第一个错误。
     */
    @ResponseStatus (HttpStatus.BAD_REQUEST)
    @ExceptionHandler ({MethodArgumentNotValidException.class})
    public JsonResult handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        return ResultUtils.error(HttpStatus.BAD_REQUEST.value(), bindingResult.getFieldError().getDefaultMessage());
    }

    /**
     * 400 HttpStatus.BAD_REQUEST
     *
     * @param e 缺少PathVariable
     *
     * @return
     */
    @ResponseStatus (HttpStatus.BAD_REQUEST)
    @ExceptionHandler ({MissingPathVariableException.class})
    public JsonResult handleMissingPathVariableException(MissingPathVariableException e) {
        return ResultUtils.error(HttpStatus.BAD_REQUEST.value(), e.getMessage());
    }

    /**
     * 400 HttpStatus.BAD_REQUEST
     *
     * @param e 缺少RequestParameter
     *
     * @return
     */
    @ResponseStatus (HttpStatus.BAD_REQUEST)
    @ExceptionHandler ({MissingServletRequestParameterException.class})
    public JsonResult handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        return ResultUtils.error(HttpStatus.BAD_REQUEST.value(), e.getMessage());
    }


    /**
     * 400 HttpStatus.BAD_REQUEST
     *
     * @param e 参数解析失败
     *
     * @return
     */
    @ResponseStatus (HttpStatus.BAD_REQUEST)
    @ExceptionHandler ({HttpMessageNotReadableException.class})
    public JsonResult handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        return ResultUtils.error(HttpStatus.BAD_REQUEST.value(), e.getMessage());
    }

    /**
     * 400 HttpStatus.BAD_REQUEST
     *
     * @param e 参数绑定失败
     *
     * @return
     */
    @ResponseStatus (HttpStatus.BAD_REQUEST)
    @ExceptionHandler ({BindException.class})
    public JsonResult handleBindException(BindException e) {
        return ResultUtils.error(HttpStatus.BAD_REQUEST.value(), e.getMessage());
    }


    /**
     * 405
     *
     * @param e
     *
     * @return
     */
    @ResponseStatus (HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler ({HttpRequestMethodNotSupportedException.class})
    public JsonResult handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        return ResultUtils.error(HttpStatus.METHOD_NOT_ALLOWED.value(), e.getMessage());
    }


    /**
     * 自定义的异常 或者服务器内部异常500
     *
     * @param e
     *
     * @return 当是自定义异常时，其状态码是200，若是服务器内部异常，其状态码为500
     */
    @ExceptionHandler (value = Exception.class)
    public ResponseEntity<JsonResult> defaultErrorHandler(Exception e) {
        //自定义的异常
        if (e instanceof MyException) {
            MyException myException = (MyException) e;
            return ResponseEntity.status(HttpStatus.OK).body(ResultUtils.error(myException.getCode(), myException.getMessage()));
        } else {//未定义的其他 服务器内部的异常
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResultUtils.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()));
        }
    }
}
