package org.musicbrainz.search.solrwriter.xml;

import org.musicbrainz.search.solrwriter.AbstractMBWriterWorkTest;
import org.musicbrainz.search.solrwriter.MBXMLWriterTestInterface;

public class MBXMLWriterWorkTest extends AbstractMBWriterWorkTest implements MBXMLWriterTestInterface {
	static {
		corename = "work";
	}
}
