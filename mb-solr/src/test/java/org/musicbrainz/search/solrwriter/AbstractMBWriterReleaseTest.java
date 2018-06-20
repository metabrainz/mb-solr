package org.musicbrainz.search.solrwriter;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class AbstractMBWriterReleaseTest extends AbstractMBWriterTest {
	@Override
	public ArrayList<String> getDoc() {
		return new ArrayList<>(Arrays.asList(new String[]{
				"mbid", uuid,
				"artist", "Chiptunes = WIN \\m|♥|m/",
				"artistname", "Chiptunes = WIN \\m|♥|m/",
				"release", "Chiptunes = WIN \\m|♥|m/"}));
	}
}
