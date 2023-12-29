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

    //默认敏感词地址
    private String filepath = "/senwords.txt";

    //核心Trie结构
    private SenTrie senTrie;

    //当前敏感词列表
    private List<String> lines;

    /**
     * 默认构造
     */
    public SensiHolder() {
        try {
            List<String> lines = readLines();
            this.senTrie = new SenTrie(new HashSet<>(lines));
            this.lines = lines;
        } catch (IOException e) {
            logger.error("Failed to initialize the sensitive word instance - {}", e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 自定义FilePath
     *
     * @param filepath
     */
    public SensiHolder(String filepath) {
        this.filepath = filepath;
        try {
            List<String> lines = readLines();
            this.senTrie = new SenTrie(new HashSet<>(lines));
            this.lines = lines;

            logger.info("The current sensitive word text source comes from - {}", filepath);
        } catch (IOException e) {
            logger.error("Failed to initialize the sensitive word instance - {}", e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 读取敏感词Base64文本
     *
     * @return
     * @throws IOException
     */
    private List<String> readLines() throws IOException {
        List<String> lines = new ArrayList<>();
        try (InputStream inputStream = getClass().getResourceAsStream(filepath)) {
            assert inputStream != null;
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    byte[] decode = Base64.getDecoder().decode(line);
                    String decodedString = new String(decode, StandardCharsets.UTF_8);
                    lines.add(decodedString);
                }
            }
        }
        return lines;
    }

    /**
     * 是否存在敏感词
     *
     * @param input
     * @param ifLog
     * @return
     */
    public boolean exists(String input, boolean ifLog) {
        input = standardizeInput(input);
        SenKeys senKeys = this.senTrie.findAll(input);
        if (!senKeys.isEmpty() && ifLog) {
            logger.warn("存在敏感词：{}", String.join(",", senKeys.stream().map(SenKey::getKeyword).collect(Collectors.toList())));
        }
        return !senKeys.isEmpty();
    }

    /**
     * 是否存在敏感词
     *
     * @param input
     * @return
     */
    public String existsStr(String input) {
        SenKeys senKeys = this.senTrie.findAll(input);
        if (!senKeys.isEmpty()) {
            return senKeys.stream().map(SenKey::getKeyword).collect(Collectors.joining(","));
        }
        return null;
    }

    /**
     * 是否存在敏感词
     *
     * @param input
     * @return
     */
    public boolean exists(String input) {
        return exists(input, true);
    }

    /**
     * 是否存在敏感词
     *
     * @param data
     * @return
     */
    public boolean exists(Object data) {
        Objects.requireNonNull(data, "Data cannot be null");
        return exists(JSON.toJSONString(data));
    }

    /**
     * 替换
     *
     * @param input
     * @param replaceValue
     * @return
     */
    public String replace(String input, String replaceValue) {
        input = standardizeInput(input);
        SenKeys senKeys = this.senTrie.findAllIgnoreCase(input);
        if (senKeys.isEmpty()) {
            return input;
        }
        if (StringUtil.isEmpty(replaceValue)) {
            replaceValue = "*";
        }
        return senKeys.replaceWith(replaceValue);
    }

    /**
     * 标准化字符串
     *
     * @param input
     * @return
     */
    public String standardizeInput(String input) {
        StringBuilder standardized = new StringBuilder(input.length());
        for (char ch : input.toCharArray()) if (Character.isLetterOrDigit(ch)) standardized.append(ch);
        return standardized.toString();
    }

    /**
     * 列出所有敏感词
     *
     * @return
     */
    public List<String> list() {
        return this.lines;
    }
}

