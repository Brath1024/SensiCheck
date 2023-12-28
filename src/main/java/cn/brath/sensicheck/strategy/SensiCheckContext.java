package cn.brath.sensicheck.strategy;

import cn.brath.sensicheck.constants.SensiCheckType;
import cn.brath.sensicheck.exception.BusinessException;
import cn.brath.sensicheck.strategy.impl.SenFilterErrorImpl;
import cn.brath.sensicheck.strategy.impl.SenFilterNonImpl;
import cn.brath.sensicheck.strategy.impl.SenFilterReplaceImpl;

import java.util.List;

/**
 * 策略器上下文处理
 *
 * @author Brath
 * @since 2023/7/28 15:44
 */
public class SensiCheckContext {

    private List<SensiCheckStrategy> strategyList;

    SensiCheckStrategy strategy;

    private SensiCheckContext() {
        this.strategyList = List.of(
                new SenFilterErrorImpl(),
                new SenFilterNonImpl(),
                new SenFilterReplaceImpl()
        );
    }

    /**
     * 获取业务实现
     *
     * @param type
     * @return
     */
    public static SensiCheckStrategy getStrategyService(SensiCheckType type) {
        SensiCheckContext service = new SensiCheckContext();
        return service.strategy = service.strategyList.stream()
                .filter(item -> item.type().equals(type))
                .findFirst()
                .orElseThrow(() -> new BusinessException("敏感词过滤策略器上下文类处理失败，未找到匹配的过滤类型"));
    }

}
