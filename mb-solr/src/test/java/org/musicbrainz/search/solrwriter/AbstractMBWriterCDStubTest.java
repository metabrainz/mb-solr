package org.musicbrainz.search.solrwriter;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class AbstractMBWriterCDStubTest extends AbstractMBWriterTest {
	@Override
	public ArrayList<String> getDoc() {
		return new ArrayList<>(Arrays.asList(new String[]{
				"id", "1",
				"discid", "zsXyqGWvw0zF024A_saTokxIMzo-",
				"title", "Doo-lang Doo-lang", "artist", "The Emergency",
				"barcode", "700261200910", "comment", "CD Baby id:emergency2",
				"tracks", "16"}));
	}
}
