package org.musicbrainz.search.solrwriter;

import java.util.ArrayList;
import java.util.Arrays;

public class MBXMLWriterReleaseGroupTest extends MBXMLWriterTest {
	static {
		corename = "release-group";
		doc = new ArrayList<String>(Arrays.asList(new String[]{
				"mbid", uuid,
				"releasegroup", "Chiptunes = WIN \\m|♥|m/"
		}));
	}
}
