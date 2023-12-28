package cn.brath.sensicheck;

import cn.brath.sensicheck.constants.SensiCheckType;
import cn.brath.sensicheck.strategy.SensiCheckContext;
import cn.brath.sensicheck.strategy.SensiCheckStrategy;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
public class ApiTest {
    public static void main(String[] args) {
        SensiCheckStrategy strategyService = SensiCheckContext.getStrategyService(SensiCheckType.ERROR);
        strategyService.defaultReplaceValue("滚粗", "*");
    }
}
