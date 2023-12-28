package cn.brath.sensicheck;

import cn.brath.sensicheck.constants.SensiCheckType;
import cn.brath.sensicheck.strategy.SensiCheckContext;
import cn.brath.sensicheck.strategy.SensiCheckStrategy;
import cn.brath.sensicheck.utils.SensiCheckUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class SensiCheckTest {

    /**
     * Sensi敏感词检测库使用AC自动机算法实现在100+字符串 & 100万次替换的情况下，耗时:1317ms
     * 大约可以达到759,884 QPS（每秒查询数）≈ 70万 QPS
     */
    @Test
    public void test1() {
        String randomText = RandomUtil.randomString("1234567890bcdefghiJKLMNOPQRSTUVWXYZ你他妈的", 100);

        long start = System.currentTimeMillis();
        for (int i = 0; i < 100_0000; i++) {
            SensiCheckUtil.check(randomText);
        }
        long end = System.currentTimeMillis();

        System.err.println("------------------ COST: " + (end - start));
        //------------------ COST: 1317
    }

    /**
     * Sensi敏感词检测库使用AC自动机算法实现在4字符串 & 100万次替换的情况下，耗时:525ms
     * 大约可以达到1,904,762 QPS（每秒查询数）≈ 190万 QPS
     */
    @Test
    public void test2() {
        String randomText = "你他妈的";

        long start = System.currentTimeMillis();
        for (int i = 0; i < 100_0000; i++) {
            SensiCheckUtil.check(randomText);
        }
        long end = System.currentTimeMillis();

        System.err.println("------------------ COST: " + (end - start));
        //------------------ COST: 525
    }

    @Test
    public void test3() {
        String text = "你妹的";
        SensiCheckUtil.contains(text);
    }

    @Test
    public void test4() {
        String text = "你妹的";
        String check = SensiCheckUtil.check(text, "#");
        System.out.println(check);
    }

    @Test
    public void test5() {
        String text = "你妹的";
        String check = SensiCheckUtil.check(text, "#", SensiCheckType.ERROR);
        System.out.println(check);
    }
}
