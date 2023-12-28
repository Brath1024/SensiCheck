package cn.brath.sensicheck;

import cn.brath.sensicheck.constants.SensiCheckType;
import cn.brath.sensicheck.strategy.SensiCheckContext;
import cn.brath.sensicheck.strategy.SensiCheckStrategy;

/**
 * @author: 2718
 * @date: 2023/12/28 9:14
 * @description:
 */
public class Main {
    public static void main(String[] args) {
        SensiCheckStrategy strategyService = SensiCheckContext.getStrategyService(SensiCheckType.REPLACE);
        String testWord = strategyService.defaultReplaceValue("去死吧你64", "*");
        System.out.println(testWord);
    }
}
