package cn.brath.sensicheck.core;

import java.io.*;
import java.util.*;

public class Trie implements Serializable {
    private static final long serialVersionUID = 7464998650081881647L;
    private final AcNode root;

    public Trie() {
        this.root = new AcNode(0);
    }

    public Trie(Set<String> keywords) {
        this.root = new AcNode(0);
        this.addKeywords(keywords);
    }

    public Trie(String... keywords) {
        this.root = new AcNode(0);
        this.addKeywords(keywords);
    }

    public Trie(InputStream src) {
        this.root = new AcNode(0);
        this.addKeywords(src);
    }

    public Trie addKeywords(Set<String> keywords) {
        for (String keyword : keywords) {
            if (keyword != null && !keyword.isEmpty()) {
                root.addNode(keyword).addKeyword(keyword);
            }
        }
        Queue<AcNode> nodes = new LinkedList<>();
        root.getSuccess().forEach((ignored, state) -> {
            state.setFailure(root);
            nodes.add(state);
        });
        while (!nodes.isEmpty()) {
            AcNode state = nodes.poll();
            state.getSuccess().forEach((c, next) -> {
                AcNode f = state.getFailure();
                AcNode fn = f.nextState(c);
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

    public Trie addKeywords(String... keywords) {
        if (keywords == null || keywords.length == 0) {
            return this;
        }
        Set<String> keywordSet = new HashSet<>();
        Collections.addAll(keywordSet, keywords);
        return addKeywords(keywordSet);
    }

    public Trie addKeywords(InputStream src) {
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
        return addKeywords(keywords);
    }

    public Emits findAll(CharSequence text, boolean ignoreCase) {
        Emits emits = new Emits(text);
        AcNode state = root;
        for (int i = 0, len = text.length(); i < len; i++) {
            state = nextState(state, text.charAt(i), ignoreCase);
            for (String keyword : state.getKeywords()) {
                emits.add(new Emit(i - keyword.length() + 1, i + 1, keyword));
            }
        }
        return emits;
    }

    public Emits findAll(CharSequence text) {
        return findAll(text, false);
    }

    public Emits findAllIgnoreCase(CharSequence text) {
        return findAll(text, true);
    }

    public Emit findFirst(CharSequence text, boolean ignoreCase) {
        AcNode state = root;
        for (int i = 0, len = text.length(); i < len; i++) {
            state = nextState(state, text.charAt(i), ignoreCase);
            String keyword = state.getFirstKeyword();
            if (keyword != null) {
                return new Emit(i - keyword.length() + 1, i + 1, keyword);
            }
        }
        return null;
    }

    public Emit findFirst(CharSequence text) {
        return findFirst(text, false);
    }

    public Emit findFirstIgnoreCase(CharSequence text) {
        return findFirst(text, true);
    }

    private AcNode nextState(AcNode state, char c, boolean ignoreCase) {
        AcNode next = state.nextState(c, ignoreCase);
        while (next == null) {
            state = state.getFailure();
            next = state.nextState(c, ignoreCase);
        }
        return next;
    }
}