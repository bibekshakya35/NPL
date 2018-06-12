package spec.fuzzystring.trie.optimized;

import fuzzystring.trie.optimized.Trie;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TrieSpec {
    private Trie trie;

    @Before
    public void init() {
        trie = new Trie();
        String keys[] = {"the", "a", "there", "answer", "any",
                "by", "bye", "their"};
        for (String key : keys) {
            trie.insert(key);
        }
    }
    @Test
    public void shouldSearchFromPrefix(){
        Assert.assertEquals(trie.startsWith("ans"),true);
        Assert.assertEquals(trie.startsWith("an"),true);
        Assert.assertEquals(trie.startsWith("by"),true);
        Assert.assertEquals(trie.startsWith("dar"),false);
    }
    @Test
    public void shouldSearchGivenWord() {
        Assert.assertEquals(trie.search("the"), true);
        Assert.assertEquals(trie.search("these"), false);
        Assert.assertEquals(trie.search("their"), true);
        Assert.assertEquals(trie.search("thew"), false);
    }
    @Test

    @After
    public void destroy() {
        trie = null;
    }
}
