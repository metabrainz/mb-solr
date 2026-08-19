package org.musicbrainz.search.solrwriter.xml;

import org.musicbrainz.search.solrwriter.AbstractMBWriterAreaTest;
import org.musicbrainz.search.solrwriter.MBFlatXMLWriterTestInterface;

public class MBXMLWriterAreaTest extends AbstractMBWriterAreaTest implements MBFlatXMLWriterTestInterface {
	static {
		corename = "area";
	}
}
