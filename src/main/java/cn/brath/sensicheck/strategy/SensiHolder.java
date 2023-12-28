package cn.brath.sensicheck.strategy;

import cn.brath.sensicheck.core.SenKey;
import cn.brath.sensicheck.core.SenKeys;
import cn.brath.sensicheck.core.SenTrie;
import cn.brath.sensicheck.utils.StringUtil;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 敏感词处理
 *
 * @author Brath
 * @since 2023-07-28 14:17:54
 * <p>
 * 使用AC自动机实现敏感词处理
 * </p>
 */
public class SensiHolder {

    private static final Logger logger = LoggerFactory.getLogger(SensiHolder.class);

    private SenTrie senTrie;

    public SensiHolder() {
        try {
            List<String> lines = readLines();
            this.senTrie = new SenTrie(new HashSet<>(lines));
        } catch (IOException e) {
            logger.error("敏感词实例初始化失败 - {}", e.getMessage());
            e.printStackTrace();
        }
    }

    private List<String> readLines() throws IOException {
        List<String> lines = new ArrayList<>();
        try (InputStream inputStream = getClass().getResourceAsStream("/senwrods.txt");
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                byte[] decode = Base64.getDecoder().decode(line);
                String decodedString = new String(decode, StandardCharsets.UTF_8);
                lines.add(decodedString);
            }
        }
        return lines;
    }

    public boolean exists(String input, boolean ifLog) {
        SenKeys senKeys = this.senTrie.findAll(input);
        if (!senKeys.isEmpty() && ifLog) {
            logger.warn("存在敏感词：{}", String.join(",", senKeys.stream().map(SenKey::getKeyword).collect(Collectors.toList())));
        }
        return !senKeys.isEmpty();
    }

    public String existsStr(String input) {
        SenKeys senKeys = this.senTrie.findAll(input);
        if (!senKeys.isEmpty()) {
            return senKeys.stream().map(SenKey::getKeyword).collect(Collectors.joining(","));
        }
        return null;
    }

    public boolean exists(String input) {
        return exists(input, true);
    }

    public boolean exists(Object data) {
        Objects.requireNonNull(data, "Data cannot be null");
        return exists(JSON.toJSONString(data));
    }

    public String replace(String input, String replaceValue) {
        SenKeys senKeys = this.senTrie.findAllIgnoreCase(input);
        if (senKeys.isEmpty()) {
            return input;
        }
        if (StringUtil.isEmpty(replaceValue)) {
            replaceValue = "*";
        }
        return senKeys.replaceWith(replaceValue);
    }

    public String replaceCommon(String input) {
        return replace(input, "*");
    }
}

