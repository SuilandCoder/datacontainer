package njnu.opengms.container.exception;

import njnu.opengms.container.enums.ResultEnum;

/**
 * @ClassName MyException
 * @Description 自定义的异常，结合ResultEnum中定义的错误信息，对外暴露异常。
 * @Author sun_liber
 * @Date 2018/11/28
 * @Version 1.0.0
 */
public class MyException extends RuntimeException {
    private Integer code;

    public MyException(ResultEnum resultEnum) {
        super(resultEnum.getMsg());
        this.code = resultEnum.getCode();
    }

    public MyException(String message) {
        super(message);
        this.code = -9999;
    }

    public Integer getCode() {
        return code;
    }
}