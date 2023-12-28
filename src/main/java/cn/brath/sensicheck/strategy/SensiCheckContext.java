package cn.brath.sensicheck.strategy;

import cn.brath.sensicheck.constants.SensiCheckType;
import cn.brath.sensicheck.strategy.impl.SenFilterErrorImpl;
import cn.brath.sensicheck.strategy.impl.SenFilterNonImpl;
import cn.brath.sensicheck.strategy.impl.SenFilterReplaceImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 策略器上下文处理
 *
 * @author Brath
 * @since 2023/7/28 15:44
 */
public class SensiCheckContext {

    private final Map<SensiCheckType, SensiCheckStrategy> strategyMap;

    private static SensiCheckContext instance = null;

    private SensiCheckContext() {
        this.strategyMap = new HashMap<>();
        this.strategyMap.put(SensiCheckType.REPLACE, new SenFilterReplaceImpl());
        this.strategyMap.put(SensiCheckType.ERROR, new SenFilterErrorImpl());
        this.strategyMap.put(SensiCheckType.NON, new SenFilterNonImpl());
    }

    public static synchronized SensiCheckContext getInstance() {
        if (instance == null) {
            instance = new SensiCheckContext();
        }

        return instance;
    }

    /**
     * 获取业务实现
     *
     * @return
     */
    public SensiCheckStrategy getStrategyService() {
        return strategyMap.get(SensiCheckType.ERROR);
    }

    /**
     * 获取业务实现
     *
     * @param type
     * @return
     */
    public SensiCheckStrategy getStrategyService(SensiCheckType type) {
        return strategyMap.get(type);
    }

}
