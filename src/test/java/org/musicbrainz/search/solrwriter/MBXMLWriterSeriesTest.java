package org.musicbrainz.search.solrwriter;

import java.util.ArrayList;
import java.util.Arrays;

public class MBXMLWriterSeriesTest extends  MBXMLWriterTest{
	static {
		corename = "series";
		doc = new ArrayList<String>(Arrays.asList(new String[]{
				"mbid", uuid,
				"series", "The World Roots Music Library"}));
	}
}
