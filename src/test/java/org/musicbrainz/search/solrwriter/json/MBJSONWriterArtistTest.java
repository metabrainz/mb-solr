package org.musicbrainz.search.solrwriter.json;

import org.musicbrainz.search.solrwriter.AbstractMBWriterArtistTest;
import org.musicbrainz.search.solrwriter.MBJSONWriterTestInterface;

public class MBJSONWriterArtistTest extends AbstractMBWriterArtistTest implements MBJSONWriterTestInterface {
	static {
		corename = "artist";
	}
}
