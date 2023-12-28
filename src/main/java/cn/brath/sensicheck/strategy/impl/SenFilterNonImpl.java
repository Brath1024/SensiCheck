package cn.brath.sensicheck.strategy.impl;

import cn.brath.sensicheck.constants.SensiCheckType;

/**
 * @author Brath
 * @since 2023/7/28 15:43
 */
public class SenFilterNonImpl extends SenFilterImpl {

    @Override
    public SensiCheckType type() {
        return SensiCheckType.NON;
    }

    /**
     * 处理字符串类型值
     *
     * @param value
     * @return
     */
    @Override
    protected String handleString(String value) {
        return value;
    }

}
