package cn.brath.sensicheck.exception;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BaseRuntimeException extends RuntimeException {

    private String message;

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public <T extends BaseRuntimeException> T error(String message) {
        this.message = message;
        return (T) this;
    }

}