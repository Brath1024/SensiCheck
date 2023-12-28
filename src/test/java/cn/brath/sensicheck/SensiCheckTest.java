package cn.brath.sensicheck;

import cn.brath.sensicheck.constants.SensiCheckType;
import cn.brath.sensicheck.strategy.SensiCheckContext;
import cn.brath.sensicheck.strategy.SensiCheckStrategy;
import cn.hutool.core.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class SensiCheckTest {

    /**
     * Sensi敏感词检测库使用AC自动机算法实现在100万次替换的情况下，耗时:1603ms,大约可以达到623,832 QPS（每秒查询数）≈ 60万QPS
     */
    @Test
    public void test() {
        SensiCheckStrategy strategyService = SensiCheckContext.getStrategyService(SensiCheckType.REPLACE);
        String randomText = RandomUtil.randomString("1234567890bcdefghiJKLMNOPQRSTUVWXYZ你他妈的", 100);

        long start = System.currentTimeMillis();
        for (int i = 0; i < 100_0000; i++) {
            strategyService.defaultReplaceValue(randomText, "*");
        }
        long end = System.currentTimeMillis();

        System.err.println("------------------ COST: " + (end - start));
        //------------------ COST: 1603
    }
}
