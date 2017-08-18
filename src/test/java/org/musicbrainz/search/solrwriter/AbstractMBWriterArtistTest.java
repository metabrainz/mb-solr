package org.musicbrainz.search.solrwriter;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class AbstractMBWriterArtistTest extends AbstractMBWriterTest {
	@Override
	public ArrayList<String> getDoc() {
		return new ArrayList<String>(Arrays.asList(new String[]{
				"mbid", uuid,
				"artist", "Howard Shore",
				"sortname", "Shore, Howard"}));
	}
}
