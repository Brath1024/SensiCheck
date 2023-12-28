package cn.brath.sensicheck;

import cn.brath.sensicheck.core.Emit;
import cn.brath.sensicheck.core.Emits;
import cn.brath.sensicheck.core.Trie;
import cn.brath.sensicheck.utils.AssertUtil;
import java.util.Base64;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 敏感词处理
 *
 * @author Brath
 * @since 2023-07-28 14:17:54
 * <p>
 * 使用AC自动机实现敏感词处理
 * 核心代码源自Github：Leego Yih，找不到源仓库地址了
 * </p>
 */
public class SensiCheckHolder {

    private static final Logger logger = LoggerFactory.getLogger(SensiCheckHolder.class);

    private Trie trie;

    public SensiCheckHolder() {
        try {
            List<String> lines = readLines();
            this.trie = new Trie(new HashSet<>(lines));
            logger.info("敏感词实例初始化成功");
        } catch (IOException e) {
            logger.error("敏感词实例初始化失败 - {}", e.getMessage());
            e.printStackTrace();
        }
    }

    private List<String> readLines() throws IOException {
        List<String> lines = new ArrayList<>();
//        try (InputStream inputStream = getClass().getResourceAsStream("/sensitive/dict.txt");
        try (InputStream inputStream = getClass().getResourceAsStream("/sensitive/senwrods.txt");
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
        Emits emits = this.trie.findAll(input);
        if (!emits.isEmpty() && ifLog) {
            logger.warn("存在敏感词：{}", String.join(",", emits.stream().map(Emit::getKeyword).collect(Collectors.toList())));
        }
        return !emits.isEmpty();
    }

    public String existsStr(String input) {
        Emits emits = this.trie.findAll(input);
        if (!emits.isEmpty()) {
            return emits.stream().map(Emit::getKeyword).collect(Collectors.joining(","));
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
        Emits emits = this.trie.findAllIgnoreCase(input);
        if (emits.isEmpty()) {
            return input;
        }
        if (AssertUtil.isEmpty(replaceValue)) {
            replaceValue = "*";
        }
        return emits.replaceWith(replaceValue);
    }

    public String replaceCommon(String input) {
        return replace(input, "*");
    }
}

