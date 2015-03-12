package org.musicbrainz.search.solrwriter;

import java.util.ArrayList;
import java.util.Arrays;

public class MBXMLWriterEditorTest extends MBXMLWriterTest {
	static {
		corename = "editor";
		doc = new ArrayList<>(Arrays.asList(new String[]{
				"id", "1337",
				"editor", "Mineo",
				"bio", "Nothing to see here!"}));
	}
}
