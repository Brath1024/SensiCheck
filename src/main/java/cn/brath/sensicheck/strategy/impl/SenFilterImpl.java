package cn.brath.sensicheck.strategy.impl;

import cn.brath.sensicheck.strategy.SensiCheckStrategy;
import cn.brath.sensicheck.strategy.SensiHolder;
import cn.hutool.extra.spring.SpringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Modifier;
import java.util.List;
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
        try {
            this.sensiHolder = SpringUtil.getBean(SensiHolder.class);
            logger.info("Spring Beans are currently used for Holder instantiation.");
        } catch (Exception e) {
            this.sensiHolder = new SensiHolder();
        }
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
            logger.warn("Sensitive Words Parameters filtered: Parameters: {}, Type：Interface||Abstract.", value);
            return value;
        }
        value = this.handleString((String) value);
        return value;
    }

    @Override
    public boolean contains(String value) {
        return sensiHolder.exists(value);
    }

    @Override
    public List<String> list() {
        return sensiHolder.list();
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
