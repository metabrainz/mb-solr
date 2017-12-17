package org.musicbrainz.search.solrwriter.json;

import org.musicbrainz.search.solrwriter.AbstractMBWriterPlaceTest;
import org.musicbrainz.search.solrwriter.MBJSONWriterTestInterface;

public class MBJSONWriterPlaceTest extends AbstractMBWriterPlaceTest implements MBJSONWriterTestInterface {
	static {
		corename = "place";
	}
}
