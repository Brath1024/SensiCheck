package cn.brath.sensicheck.utils;

public class AssertUtil {

    public static boolean isEmpty(String str) {
        if (null == str) {
            return true;
        } else {
            return "".equals(str);
        }
    }
}
