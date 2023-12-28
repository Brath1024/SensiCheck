package cn.brath.sensicheck.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SenException extends BaseRuntimeException {
    private static final long serialVersionUID = 1L;

    private String message;
    private String value;
    private int code;

    public SenException() {
    }

    public SenException(String message, String value) {
        this.value = value;
        this.message = message;
        super.setMessage(message);
    }

    public SenException(String message, int code) {
        this.message = message;
        this.code = code;
        super.setMessage(message);
    }
}