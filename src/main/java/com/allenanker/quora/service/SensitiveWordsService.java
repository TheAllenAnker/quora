package com.allenanker.quora.service;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

@Service
public class SensitiveWordsService implements InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(SensitiveWordsService.class);

    private TrieNode rootOfSensitiveTrie = new TrieNode();

    @Override
    public void afterPropertiesSet() throws Exception {
        try (InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("SensitiveWords.txt");
             InputStreamReader isr = new InputStreamReader(is);
             BufferedReader bf = new BufferedReader(isr)) {
            String word;
            while ((word = bf.readLine()) != null) {
                addWordToSensitiveTire(word.trim());
            }
        } catch (Exception e) {
            logger.error("Read file SensitiveWords.txt failed.");
        }
    }

    public String filterBySensitiveWords(String inputText) {
        if (StringUtils.isBlank(inputText)) {
            return inputText;
        }

        final String replacement = "***";
        StringBuilder sb = new StringBuilder();
        TrieNode trieRootCopy = rootOfSensitiveTrie;
        int start = 0;
        int curr = 0;
        while (curr < inputText.length()) {
            char c = inputText.charAt(curr);
            if (trieRootCopy.getLeaf(c) == null) {
                if (start == curr) {
                    sb.append(inputText.charAt(start));
                } else {
                    sb.append(inputText, start, curr);
                }
                curr++;
                start++;
            } else {
                trieRootCopy = trieRootCopy.getLeaf(c);
                if (trieRootCopy.isEnd()
                        && ((curr + 1 < inputText.length() && trieRootCopy.getLeaf(inputText.charAt(curr + 1)) == null)
                            || curr + 1 == inputText.length())) {
                    sb.append(replacement);
                    start = curr + 1;
                    trieRootCopy = rootOfSensitiveTrie;
                }
                curr++;
            }
        }
//        sb.append()
        return sb.toString();
    }

    private void addWordToSensitiveTire(String word) {
        TrieNode rootNodeCopy = rootOfSensitiveTrie;

        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            if (rootNodeCopy.getLeaf(c) == null) {
                rootNodeCopy.addLeaf(c);
            }
            rootNodeCopy = rootNodeCopy.getLeaf(c);
        }
        rootNodeCopy.setEnd();
    }

    private class TrieNode {
        private boolean isEnd = false;
        private HashMap<Character, TrieNode> leafs;

        public TrieNode() {
            this.leafs = new HashMap<>();
        }

        public void addLeaf(char c) {
            leafs.put(c, new TrieNode());
        }

        public TrieNode getLeaf(char c) {
            return leafs.get(c);
        }

        public boolean isEnd() {
            return isEnd;
        }

        public void setEnd() {
            isEnd = true;
        }
    }

    public static void main(String[] args) {
        SensitiveWordsService sensitiveWordsService = new SensitiveWordsService();
        sensitiveWordsService.addWordToSensitiveTire("abcab");
        sensitiveWordsService.addWordToSensitiveTire("ab");
        System.out.println(sensitiveWordsService.filterBySensitiveWords("abcababaabbedf"));
    }
}
