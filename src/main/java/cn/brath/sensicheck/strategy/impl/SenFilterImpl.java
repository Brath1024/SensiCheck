package cn.brath.sensicheck.strategy.impl;

import cn.brath.sensicheck.SensiHolder;
import cn.brath.sensicheck.strategy.SensiCheckStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Modifier;
import java.util.Objects;

/**
 * @author Brath
 * @since 2023/7/28 15:55
 */
public abstract class SenFilterImpl implements SensiCheckStrategy {
    private static final Logger logger = LoggerFactory.getLogger(SenFilterImpl.class);

    protected SensiHolder sensiHolder;

    protected String replaceValue;

    public SenFilterImpl() {
        this.sensiHolder = new SensiHolder();
    }

    /**
     * 默认的替换方法
     *
     * @param value
     * @return
     */
    @Override
    public String defaultReplaceValue(String value, String replaceValue) {
        if (Objects.isNull(value)) {
            return null;
        }
        this.replaceValue = replaceValue;
        Class<?> valueClass = value.getClass();
        if (
                Modifier.isInterface(valueClass.getModifiers()) ||
                        Modifier.isAbstract(valueClass.getModifiers())
        ) {
            logger.warn("敏感词 参数已过滤: 参数：{},类型：Interface||Abstract", value);
            return value;
        }
        value = this.handleString((String) value);
        return value;
    }

    /**
     * 处理字符串类型值
     *
     * @param value
     * @return
     */
    protected abstract String handleString(String value);

    public String getReplaceValue() {
        return replaceValue;
    }
}
