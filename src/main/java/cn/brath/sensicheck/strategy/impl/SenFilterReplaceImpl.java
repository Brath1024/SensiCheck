package cn.brath.sensicheck.strategy.impl;

import cn.brath.sensicheck.constants.SensiCheckType;

/**
 * @author Brath
 * @since 2023/7/28 15:43
 */
public class SenFilterReplaceImpl extends SenFilterImpl {

    @Override
    public SensiCheckType type() {
        return SensiCheckType.REPLACE;
    }

    /**
     * 处理字符串类型值
     *
     * @param value
     * @return
     */
    @Override
    protected String handleString(String value) {
        return super.sensiCheckHolder.replace(value, this.replaceValue);
    }
}
