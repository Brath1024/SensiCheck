package cn.brath.sensicheck.exception;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BusinessException extends RuntimeException {
    private String codeMsg;

    public BusinessException(String codeMsg) {
        this.codeMsg = codeMsg;
    }
}
