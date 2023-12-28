package cn.brath.sensicheck.strategy;

import cn.brath.sensicheck.constants.SensiCheckType;

/**
 * @author Brath
 * @since 2023/7/28 15:42
 */
public interface SensiCheckStrategy {

    String errorText = "文本内容检测到敏感词，已进行删除处理。为了维护社区网络环境，请不要出现带有敏感政治、暴力倾向、不健康色彩的内容!";

    SensiCheckType type();

    /**
     * 默认的替换方法
     *
     * @param value
     * @return
     */
    String defaultReplaceValue(String value, String replaceValue);

    boolean contains(String value);

}
