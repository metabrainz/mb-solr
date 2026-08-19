package org.musicbrainz.search.solrwriter.xml;

import org.musicbrainz.search.solrwriter.AbstractMBWriterSeriesTest;
import org.musicbrainz.search.solrwriter.MBFlatXMLWriterTestInterface;

public class MBXMLWriterSeriesTest extends AbstractMBWriterSeriesTest implements MBFlatXMLWriterTestInterface {
	static {
		corename = "series";
	}
}
