package org.musicbrainz.search.solrwriter.xml;

import org.musicbrainz.search.solrwriter.AbstractMBWriterCDStubTest;
import org.musicbrainz.search.solrwriter.MBFlatXMLWriterTestInterface;

public class MBXMLWriterCDStubTest extends AbstractMBWriterCDStubTest implements MBFlatXMLWriterTestInterface {
	static {
		corename = "cdstub";
	}
}
