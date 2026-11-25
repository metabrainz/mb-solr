package org.musicbrainz.search.solrwriter;

import org.junit.BeforeClass;

public class CDStubInternalIdTest extends AbstractInternalIdTest {
	@BeforeClass
	public static void beforeClass() throws Exception {
		initInternalIdCore("cdstub");
	}

	@Override
	protected String[] getIndexedDocument() {
		return new String[]{
				"id", ID,
				"discid", "zsXyqGWvw0zF024A_saTokxIMzo-",
				"title", "Doo-lang Doo-lang",
				"_store", "<cdstub xmlns=\"http://musicbrainz.org/ns/mmd-2.0#\""
						+ " id=\"zsXyqGWvw0zF024A_saTokxIMzo-\"><title>Doo-lang Doo-lang</title></cdstub>"};
	}

	@Override
	protected String getPublicQuery() {
		return "title:doo";
	}
}
