package org.musicbrainz.search.solrwriter.xml;

import org.musicbrainz.search.solrwriter.AbstractMBWriterPlaceTest;
import org.musicbrainz.search.solrwriter.MBFlatXMLWriterTestInterface;

public class MBXMLWriterPlaceTest extends AbstractMBWriterPlaceTest implements MBFlatXMLWriterTestInterface {
	static {
		corename = "place";
	}
}
