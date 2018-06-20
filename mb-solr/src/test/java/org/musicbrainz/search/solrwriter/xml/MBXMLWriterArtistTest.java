package org.musicbrainz.search.solrwriter.xml;

import org.musicbrainz.search.solrwriter.AbstractMBWriterArtistTest;
import org.musicbrainz.search.solrwriter.MBXMLWriterTestInterface;

public class MBXMLWriterArtistTest extends AbstractMBWriterArtistTest implements MBXMLWriterTestInterface {
	static {
		corename = "artist";
	}
}
