package org.musicbrainz.search.solrwriter;

import java.util.ArrayList;
import java.util.Arrays;

public class MBXMLWriterPlaceTest extends MBXMLWriterTest {
	static {
		corename = "place";
		doc = new ArrayList<>(Arrays.asList(new String[]{
				"mbid", uuid,
				"place", "Ilmbühne"}));
	}
}
