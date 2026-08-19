package org.musicbrainz.search.solrwriter.xml;

import org.musicbrainz.search.solrwriter.AbstractMBWriterReleaseGroupTest;
import org.musicbrainz.search.solrwriter.MBFlatXMLWriterTestInterface;

public class MBXMLWriterReleaseGroupTest extends AbstractMBWriterReleaseGroupTest implements MBFlatXMLWriterTestInterface {
	static {
		corename = "release-group";
	}
}
