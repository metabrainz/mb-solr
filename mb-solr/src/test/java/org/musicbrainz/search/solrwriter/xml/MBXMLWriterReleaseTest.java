package org.musicbrainz.search.solrwriter.xml;

import org.musicbrainz.search.solrwriter.AbstractMBWriterReleaseTest;
import org.musicbrainz.search.solrwriter.MBFlatXMLWriterTestInterface;

public class MBXMLWriterReleaseTest extends AbstractMBWriterReleaseTest implements MBFlatXMLWriterTestInterface {
	static {
		corename = "release";
	}
}
