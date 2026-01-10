package org.musicbrainz.search.solrwriter.json;

import org.musicbrainz.search.solrwriter.AbstractMBWriterEventTest;
import org.musicbrainz.search.solrwriter.MBJSONWriterTestInterface;

public class MBJSONWriterEventTest extends AbstractMBWriterEventTest implements MBJSONWriterTestInterface {
	static {
		corename = "event";
	}
}
