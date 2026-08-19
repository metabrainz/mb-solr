package org.musicbrainz.search.solrwriter.xml;

import org.musicbrainz.search.solrwriter.AbstractMBWriterArtistTest;
import org.musicbrainz.search.solrwriter.MBFlatXMLWriterTestInterface;

public class MBXMLWriterArtistTest extends AbstractMBWriterArtistTest implements MBFlatXMLWriterTestInterface {
	static {
		corename = "artist";
	}
}
