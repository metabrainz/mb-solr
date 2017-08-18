package org.musicbrainz.search.solrwriter;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class AbstractMBWriterAreaTest extends AbstractMBWriterTest {
	@Override
	public ArrayList<String> getDoc() {
		return new ArrayList<>(Arrays.asList(new String[]{
				"area", "Thüringen",
				"mbid", uuid}));
	}
}
