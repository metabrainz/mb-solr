package org.musicbrainz.search.analysis;

import java.io.IOException;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;

/**
 * Puts all tokens of the stream at the position of the first token, so that
 * query parsers handle them as alternatives (synonyms) of each other rather
 * than as a phrase.
 * <p>
 * Since Lucene 10, {@link org.apache.lucene.analysis.path.PathHierarchyTokenizer}
 * emits each path prefix at its own position. Applied after that tokenizer at
 * query time, this filter restores the former behavior of matching a document
 * indexed at the path itself or at any of its ancestor paths.
 */
public final class SamePositionFilter extends TokenFilter {
	private final PositionIncrementAttribute posIncAtt = addAttribute(PositionIncrementAttribute.class);
	private boolean firstToken = true;

	public SamePositionFilter(TokenStream input) {
		super(input);
	}

	@Override
	public boolean incrementToken() throws IOException {
		if (!input.incrementToken()) {
			return false;
		}
		if (firstToken) {
			firstToken = false;
		} else {
			posIncAtt.setPositionIncrement(0);
		}
		return true;
	}

	@Override
	public void reset() throws IOException {
		super.reset();
		firstToken = true;
	}
}
