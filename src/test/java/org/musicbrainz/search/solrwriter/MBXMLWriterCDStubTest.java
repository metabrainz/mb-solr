package org.musicbrainz.search.solrwriter;

import java.util.ArrayList;
import java.util.Arrays;

public class MBXMLWriterCDStubTest extends MBXMLWriterTest{
	static {
		corename = "cdstub";
		doc = new ArrayList<String>(Arrays.asList(new String[]{
				"id", "1",
				"discid", "zsXyqGWvw0zF024A_saTokxIMzo-",
				"title", "Doo-lang Doo-lang"}));
	}
}
