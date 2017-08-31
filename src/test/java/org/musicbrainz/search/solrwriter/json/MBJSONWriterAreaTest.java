package org.musicbrainz.search.solrwriter.json;

import org.musicbrainz.search.solrwriter.AbstractMBWriterAreaTest;
import org.musicbrainz.search.solrwriter.MBJSONWriterTestInterface;

public class MBJSONWriterAreaTest extends AbstractMBWriterAreaTest implements MBJSONWriterTestInterface {
	static {
		corename = "area";
	}
}
