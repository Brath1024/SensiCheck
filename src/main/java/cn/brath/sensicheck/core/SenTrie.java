package cn.brath.sensicheck.core;

import java.io.*;
import java.util.*;

public class SenTrie implements Serializable {
    private static final long serialVersionUID = 7464998650081881647L;
    private final SenNode root;

    public SenTrie() {
        this.root = new SenNode(0);
    }

    public SenTrie(Set<String> keywords) {
        this.root = new SenNode(0);
        this.addKeywords(keywords);
    }

    public SenTrie(String... keywords) {
        this.root = new SenNode(0);
        this.addKeywords(keywords);
    }

    public SenTrie(InputStream src) {
        this.root = new SenNode(0);
        this.addKeywords(src);
    }

    public SenTrie addKeywords(Set<String> keywords) {
        for (String keyword : keywords) {
            if (keyword != null && !keyword.isEmpty()) {
                root.addNode(keyword).addKeyword(keyword);
            }
        }
        Queue<SenNode> nodes = new LinkedList<>();
        root.getSuccess().forEach((ignored, state) -> {
            state.setFailure(root);
            nodes.add(state);
        });
        while (!nodes.isEmpty()) {
            SenNode state = nodes.poll();
            state.getSuccess().forEach((c, next) -> {
                SenNode f = state.getFailure();
                SenNode fn = f.nextState(c);
                while (fn == null) {
                    f = f.getFailure();
                    fn = f.nextState(c);
                }
                next.setFailure(fn);
                next.addKeywords(fn.getKeywords());
                nodes.add(next);
            });
        }
        return this;
    }

    public void addKeywords(String... keywords) {
        if (keywords == null || keywords.length == 0) {
            return;
        }
        Set<String> keywordSet = new HashSet<>();
        Collections.addAll(keywordSet, keywords);
        addKeywords(keywordSet);
    }

    public void addKeywords(InputStream src) {
        Set<String> keywords = new HashSet<>();
        try (InputStreamReader inputStreamReader = new InputStreamReader(src);
             BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                keywords.add(line);
            }
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
        addKeywords(keywords);
    }

    public SenKeys findAll(CharSequence text, boolean ignoreCase) {
        SenKeys senKeys = new SenKeys(text);
        SenNode state = root;
        for (int i = 0, len = text.length(); i < len; i++) {
            state = nextState(state, text.charAt(i), ignoreCase);
            for (String keyword : state.getKeywords()) {
                senKeys.add(new SenKey(i - keyword.length() + 1, i + 1, keyword));
            }
        }
        return senKeys;
    }

    public SenKeys findAll(CharSequence text) {
        return findAll(text, false);
    }

    public SenKeys findAllIgnoreCase(CharSequence text) {
        return findAll(text, true);
    }

    public SenKey findFirst(CharSequence text, boolean ignoreCase) {
        SenNode state = root;
        for (int i = 0, len = text.length(); i < len; i++) {
            state = nextState(state, text.charAt(i), ignoreCase);
            String keyword = state.getFirstKeyword();
            if (keyword != null) {
                return new SenKey(i - keyword.length() + 1, i + 1, keyword);
            }
        }
        return null;
    }

    public SenKey findFirst(CharSequence text) {
        return findFirst(text, false);
    }

    public SenKey findFirstIgnoreCase(CharSequence text) {
        return findFirst(text, true);
    }

    private SenNode nextState(SenNode state, char c, boolean ignoreCase) {
        SenNode next = state.nextState(c, ignoreCase);
        while (next == null) {
            state = state.getFailure();
            next = state.nextState(c, ignoreCase);
        }
        return next;
    }
}