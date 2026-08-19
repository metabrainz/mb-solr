package org.musicbrainz.search.solrwriter.xml;

import org.musicbrainz.search.solrwriter.AbstractMBWriterRecordingTest;
import org.musicbrainz.search.solrwriter.MBFlatXMLWriterTestInterface;

public class MBXMLWriterRecordingTest extends AbstractMBWriterRecordingTest implements MBFlatXMLWriterTestInterface {
	static {
		corename = "recording";
	}
}
