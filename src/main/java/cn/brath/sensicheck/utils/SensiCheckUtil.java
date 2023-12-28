package cn.brath.sensicheck.utils;

import cn.brath.sensicheck.constants.SensiCheckType;
import cn.brath.sensicheck.strategy.SensiCheckContext;
import cn.brath.sensicheck.strategy.SensiCheckStrategy;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;
import java.util.stream.Collectors;

public class SensiCheckUtil {

    private static final String DEFAULT_REPLACE_VALUE = "*";

    /**
     * 单字符串检测，默认替换值为"*"
     */
    public static String check(String text) {
        return check(text, SensiCheckType.REPLACE);
    }

    /**
     * 单字符串检测，自定义替换值
     */
    public static String check(String text, String replaceValue) {
        return check(text, replaceValue, SensiCheckType.REPLACE);
    }

    /**
     * 单字符串检测，自定义过滤策略
     */
    public static String check(String text, SensiCheckType type) {
        return check(text, DEFAULT_REPLACE_VALUE, type);
    }

    /**
     * 单字符串检测，自定义替换值和过滤策略
     */
    public static String check(String text, String replaceValue, SensiCheckType type) {
        SensiCheckStrategy strategyService =
                SensiCheckContext.getInstance().getStrategyService(type);

        return strategyService.defaultReplaceValue(text, replaceValue);
    }


    /**
     * 多字符串检测，默认替换值为"*"，默认过滤策略为REPLACE
     */
    public static List<String> multiStringChecks(List<String> texts) {
        return multiStringChecks(texts, DEFAULT_REPLACE_VALUE, SensiCheckType.REPLACE);
    }

    /**
     * 多字符串检测，自定义过滤策略，默认替换值为"*"
     */
    public static List<String> multiStringChecks(List<String> texts, SensiCheckType type) {
        return multiStringChecks(texts, DEFAULT_REPLACE_VALUE, type);
    }

    /**
     * 多字符串检测，自定义替换值，默认过滤策略为REPLACE
     */
    public static List<String> multiStringChecks(List<String> texts, String replaceValue) {
        return multiStringChecks(texts, replaceValue, SensiCheckType.REPLACE);
    }

    /**
     * 多字符串检测，自定义替换值和过滤策略
     */
    public static List<String> multiStringChecks(List<String> texts, String replaceValue, SensiCheckType type) {
        SensiCheckStrategy strategyService =
                SensiCheckContext.getInstance().getStrategyService(type);

        return texts.stream()
                .map(t -> strategyService.defaultReplaceValue(t, replaceValue))
                .collect(Collectors.toList());
    }

    /**
     * 字符串是否包含敏感词
     */
    public static boolean contains(String value) {
        SensiCheckStrategy strategyService =
                SensiCheckContext.getInstance().getStrategyService();

        return strategyService.contains(value);
    }
}