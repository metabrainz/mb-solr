package org.musicbrainz.search.solrwriter.json;

import org.musicbrainz.search.solrwriter.AbstractMBWriterRecordingTest;
import org.musicbrainz.search.solrwriter.MBJSONWriterTestInterface;

public class MBJSONWriterRecordingTest extends AbstractMBWriterRecordingTest implements MBJSONWriterTestInterface {
	static {
		corename = "recording";
	}
}
