package org.musicbrainz.search.solrwriter.json;

import org.musicbrainz.search.solrwriter.AbstractMBWriterGenreTest;
import org.musicbrainz.search.solrwriter.MBJSONWriterTestInterface;

public class MBJSONWriterGenreTest extends AbstractMBWriterGenreTest implements MBJSONWriterTestInterface {
	static {
		corename = "genre";
	}
}
