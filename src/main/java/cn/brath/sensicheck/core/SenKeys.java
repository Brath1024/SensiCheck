package cn.brath.sensicheck.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.BiPredicate;

public class SenKeys extends ArrayList<SenKey> {
    private static final long serialVersionUID = -9117361135147927914L;
    private final CharSequence source;

    public SenKeys(CharSequence source) {
        this.source = source;
    }

    private SenKeys(SenKeys senKeys) {
        super(senKeys);
        this.source = senKeys.source;
    }

    public CharSequence getSource() {
        return source;
    }

    public List<SenWord> tokenize() {
        SenKeys senKeys = this.copy();
        senKeys.removeContains();
        String source = senKeys.getSource().toString();
        List<SenWord> senWords = new ArrayList<>(senKeys.size() * 2 + 1);
        if (senKeys.isEmpty()) {
            senWords.add(new SenWord(source, null));
            return senWords;
        }
        int index = 0;
        for (SenKey senKey : senKeys) {
            if (index < senKey.getBegin()) {
                senWords.add(new SenWord(source.substring(index, senKey.getBegin()), null));
            }
            senWords.add(new SenWord(source.substring(senKey.getBegin(), senKey.getEnd()), senKey));
            index = senKey.getEnd();
        }
        SenKey last = senKeys.get(senKeys.size() - 1);
        if (last.getEnd() < source.length()) {
            senWords.add(new SenWord(source.substring(last.getEnd()), null));
        }
        return senWords;
    }

    public String replaceWith(String replacement) {
        SenKeys senKeys = this.copy();
        senKeys.removeContains();
        String source = senKeys.getSource().toString();
        if (senKeys.isEmpty()) {
            return source;
        }
        int index = 0;
        StringBuilder sb = new StringBuilder();
        for (SenKey senKey : this) {
            if (index < senKey.getBegin()) {
                sb.append(source, index, senKey.getBegin());
                index = senKey.getBegin();
            }
            sb.append(mask(replacement, index, senKey.getEnd()));
            index = senKey.getEnd();
        }
        SenKey last = senKeys.get(senKeys.size() - 1);
        if (last.getEnd() < source.length()) {
            sb.append(source, last.getEnd(), source.length());
        }
        return sb.toString();
    }

    public void removeOverlaps() {
        removeIf(SenKey::overlaps);
    }

    public void removeContains() {
        removeIf(SenKey::contains);
    }

    private void removeIf(BiPredicate<SenKey, SenKey> predicate) {
        if (this.size() <= 1) {
            return;
        }
        this.sort();
        Iterator<SenKey> iterator = this.iterator();
        SenKey senKey = iterator.next();
        while (iterator.hasNext()) {
            SenKey next = iterator.next();
            if (predicate.test(senKey, next)) {
                iterator.remove();
            } else {
                senKey = next;
            }
        }
    }

    private void sort() {
        this.sort((a, b) -> {
            if (a.getBegin() != b.getBegin()) {
                return Integer.compare(a.getBegin(), b.getBegin());
            } else {
                return Integer.compare(b.getEnd(), a.getEnd());
            }
        });
    }

    private String mask(String replacement, int begin, int end) {
        int count = end - begin;
        int len = replacement != null ? replacement.length() : 0;
        if (len == 0) {
            return repeat("*", count);
        } else if (len == 1) {
            return repeat(replacement, count);
        } else {
            char[] chars = new char[count];
            for (int i = 0; i < count; i++) {
                chars[i] = replacement.charAt((i + begin) % len);
            }
            return new String(chars);
        }
    }

    private String repeat(String s, int count) {
        if (count < 0) {
            throw new IllegalArgumentException("count is negative: " + count);
        }
        if (count == 1) {
            return s;
        }
        final int len = s.length();
        if (len == 0 || count == 0) {
            return "";
        }
        if (Integer.MAX_VALUE / count < len) {
            throw new OutOfMemoryError("Required length exceeds implementation limit");
        }
        if (len == 1) {
            final char[] single = new char[count];
            Arrays.fill(single, s.charAt(0));
            return new String(single);
        }
        final int limit = len * count;
        final char[] multiple = new char[limit];
        System.arraycopy(s.toCharArray(), 0, multiple, 0, len);
        int copied = len;
        for (; copied < limit - copied; copied <<= 1) {
            System.arraycopy(multiple, 0, multiple, copied, copied);
        }
        System.arraycopy(multiple, 0, multiple, copied, limit - copied);
        return new String(multiple);
    }

    private SenKeys copy() {
        return new SenKeys(this);
    }
}