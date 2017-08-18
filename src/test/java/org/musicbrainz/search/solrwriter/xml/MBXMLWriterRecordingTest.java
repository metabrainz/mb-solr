package org.musicbrainz.search.solrwriter.xml;

import org.musicbrainz.search.solrwriter.AbstractMBWriterRecordingTest;
import org.musicbrainz.search.solrwriter.MBXMLWriterTestInterface;

public class MBXMLWriterRecordingTest extends AbstractMBWriterRecordingTest implements MBXMLWriterTestInterface {
	static {
		corename = "recording";
	}
}
