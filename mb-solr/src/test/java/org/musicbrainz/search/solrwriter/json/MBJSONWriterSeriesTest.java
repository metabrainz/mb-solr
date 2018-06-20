package org.musicbrainz.search.solrwriter.json;

import org.musicbrainz.search.solrwriter.AbstractMBWriterSeriesTest;
import org.musicbrainz.search.solrwriter.MBJSONWriterTestInterface;

public class MBJSONWriterSeriesTest extends AbstractMBWriterSeriesTest implements MBJSONWriterTestInterface {
	static {
		corename = "series";
	}
}
