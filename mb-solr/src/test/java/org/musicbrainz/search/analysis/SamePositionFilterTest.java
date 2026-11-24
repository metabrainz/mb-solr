package org.musicbrainz.search.analysis;

import java.io.StringReader;

import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.path.PathHierarchyTokenizer;
import org.apache.lucene.tests.analysis.BaseTokenStreamTestCase;

public class SamePositionFilterTest extends BaseTokenStreamTestCase {

	private static Tokenizer pathTokenizer(String path) {
		Tokenizer tokenizer = new PathHierarchyTokenizer();
		tokenizer.setReader(new StringReader(path));
		return tokenizer;
	}

	public void testPathPrefixesShareTheFirstPosition() throws Exception {
		assertTokenStreamContents(
				new SamePositionFilter(pathTokenizer("/some/stuff/here")),
				new String[]{"/some", "/some/stuff", "/some/stuff/here"},
				new int[]{1, 0, 0});
	}

	public void testSingleTokenIsUnchanged() throws Exception {
		assertTokenStreamContents(
				new SamePositionFilter(pathTokenizer("/some")),
				new String[]{"/some"},
				new int[]{1});
	}

	public void testReusedStreamStartsAtANewPosition() throws Exception {
		Tokenizer tokenizer = pathTokenizer("/a/b");
		SamePositionFilter filter = new SamePositionFilter(tokenizer);
		assertTokenStreamContents(filter, new String[]{"/a", "/a/b"}, new int[]{1, 0});
		tokenizer.setReader(new StringReader("/c/d"));
		assertTokenStreamContents(filter, new String[]{"/c", "/c/d"}, new int[]{1, 0});
	}
}
