package org.musicbrainz.search.solrwriter.xml;

import org.musicbrainz.search.solrwriter.AbstractMBWriterReleaseTest;
import org.musicbrainz.search.solrwriter.MBXMLWriterTestInterface;

public class MBXMLWriterReleaseTest extends AbstractMBWriterReleaseTest implements MBXMLWriterTestInterface {
	static {
		corename = "release";
	}
}
