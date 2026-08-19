package org.musicbrainz.search.solrwriter.xml;

import org.musicbrainz.search.solrwriter.AbstractMBWriterLabelTest;
import org.musicbrainz.search.solrwriter.MBFlatXMLWriterTestInterface;

public class MBXMLWriterLabelTest extends AbstractMBWriterLabelTest implements MBFlatXMLWriterTestInterface {
	static {
		corename = "label";
	}
}
