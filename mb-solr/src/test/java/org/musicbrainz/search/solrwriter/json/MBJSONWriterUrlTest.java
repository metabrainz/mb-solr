package org.musicbrainz.search.solrwriter.json;

import org.musicbrainz.search.solrwriter.AbstractMBWriterUrlTest;
import org.musicbrainz.search.solrwriter.MBJSONWriterTestInterface;

public class MBJSONWriterUrlTest extends AbstractMBWriterUrlTest implements MBJSONWriterTestInterface {
	static {
		corename = "url";
	}
}
