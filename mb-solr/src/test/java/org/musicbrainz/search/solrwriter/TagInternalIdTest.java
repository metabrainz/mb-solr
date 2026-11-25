package org.musicbrainz.search.solrwriter;

import org.junit.BeforeClass;

public class TagInternalIdTest extends AbstractInternalIdTest {
	@BeforeClass
	public static void beforeClass() throws Exception {
		initInternalIdCore("tag");
	}

	@Override
	protected String[] getIndexedDocument() {
		return new String[]{
				"id", ID,
				"tag", "shoegaze",
				"_store", "<tag xmlns=\"http://musicbrainz.org/ns/mmd-2.0#\"><name>shoegaze</name></tag>"};
	}

	@Override
	protected String getPublicQuery() {
		return "tag:shoegaze";
	}
}
