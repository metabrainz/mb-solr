package org.musicbrainz.search.solrwriter;

import java.util.ArrayList;
import java.util.Arrays;

public class MBXMLWriterLabelTest extends MBXMLWriterTest {
	static {
		corename = "label";
		doc = new ArrayList<>(Arrays.asList(new String[]{
				"mbid", uuid,
				"laid", uuid,
				"label", "Deutsche Grammophon"}));
	}
}
