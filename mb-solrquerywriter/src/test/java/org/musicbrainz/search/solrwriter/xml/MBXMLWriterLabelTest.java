package org.musicbrainz.search.solrwriter.xml;

import org.musicbrainz.search.solrwriter.AbstractMBWriterLabelTest;
import org.musicbrainz.search.solrwriter.MBXMLWriterTestInterface;

public class MBXMLWriterLabelTest extends AbstractMBWriterLabelTest implements MBXMLWriterTestInterface {
	static {
		corename = "label";
	}
}
