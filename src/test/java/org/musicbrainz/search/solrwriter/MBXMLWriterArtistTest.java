package org.musicbrainz.search.solrwriter;

import java.util.ArrayList;
import java.util.Arrays;

public class MBXMLWriterArtistTest extends MBXMLWriterTest {
	static {
		corename = "artist";

		doc = new ArrayList<String>(Arrays.asList(new String[]{
				"mbid", uuid,
				"artist", "Howard Shore",
				"sortname", "Shore, Howard"}));
	}
}
