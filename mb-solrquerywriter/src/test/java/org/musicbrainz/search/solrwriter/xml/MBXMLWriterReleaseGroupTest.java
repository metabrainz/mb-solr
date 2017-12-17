package org.musicbrainz.search.solrwriter.xml;

import org.musicbrainz.search.solrwriter.AbstractMBWriterReleaseGroupTest;
import org.musicbrainz.search.solrwriter.MBXMLWriterTestInterface;

public class MBXMLWriterReleaseGroupTest extends AbstractMBWriterReleaseGroupTest implements MBXMLWriterTestInterface {
	static {
		corename = "release-group";
	}
}
