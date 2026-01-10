package org.musicbrainz.search.solrwriter.xml;

import org.musicbrainz.search.solrwriter.AbstractMBWriterEventTest;
import org.musicbrainz.search.solrwriter.MBXMLWriterTestInterface;

public class MBXMLWriterEventTest extends AbstractMBWriterEventTest implements MBXMLWriterTestInterface {
	static {
		corename = "event";
	}
}
