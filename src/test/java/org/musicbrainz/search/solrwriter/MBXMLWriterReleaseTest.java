package org.musicbrainz.search.solrwriter;

import java.util.ArrayList;
import java.util.Arrays;

public class MBXMLWriterReleaseTest extends MBXMLWriterTest{
	static {
		corename = "release";
		doc = new ArrayList<String>(Arrays.asList(new String[]{
				"mbid", uuid,
				"artist", "Chiptunes = WIN \\m|♥|m/",
				"artistname", "Chiptunes = WIN \\m|♥|m/",
				"release", "Chiptunes = WIN \\m|♥|m/"}));
	}
}
