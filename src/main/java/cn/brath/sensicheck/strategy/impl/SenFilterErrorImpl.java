package cn.brath.sensicheck.strategy.impl;


import cn.brath.sensicheck.constants.SensiCheckType;
import cn.brath.sensicheck.exception.SenException;


/**
 * @author Brath
 * @since 2023/7/28 15:43
 */
public class SenFilterErrorImpl extends SenFilterImpl {

    @Override
    public SensiCheckType type() {
        return SensiCheckType.ERROR;
    }

    /**
     * 处理字符串类型值
     *
     * @param value
     * @return
     */
    @Override
    protected String handleString(String value) {
        String sens = super.sensiHolder.existsStr(value);
        if (sens != null) throw new SenException(errorText + " 可能涉及到的敏感词：[" + sens + "]", value);
        return value;
    }

}
