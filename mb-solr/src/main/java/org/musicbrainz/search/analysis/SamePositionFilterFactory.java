package org.musicbrainz.search.analysis;

import java.util.Map;

import org.apache.lucene.analysis.TokenFilterFactory;
import org.apache.lucene.analysis.TokenStream;

/**
 * Factory for {@link SamePositionFilter}.
 * <pre class="prettyprint">
 * &lt;fieldType name="ancestor_path" class="solr.TextField"&gt;
 *   &lt;analyzer type="query"&gt;
 *     &lt;tokenizer class="solr.PathHierarchyTokenizerFactory" delimiter="/"/&gt;
 *     &lt;filter class="org.musicbrainz.search.analysis.SamePositionFilterFactory"/&gt;
 *   &lt;/analyzer&gt;
 * &lt;/fieldType&gt;</pre>
 */
public class SamePositionFilterFactory extends TokenFilterFactory {

	/** Creates a new SamePositionFilterFactory */
	public SamePositionFilterFactory(Map<String, String> args) {
		super(args);
		if (!args.isEmpty()) {
			throw new IllegalArgumentException("Unknown parameters: " + args);
		}
	}

	@Override
	public TokenStream create(TokenStream input) {
		return new SamePositionFilter(input);
	}
}
