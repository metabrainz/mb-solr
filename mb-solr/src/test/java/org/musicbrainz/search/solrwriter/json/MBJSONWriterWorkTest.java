package org.musicbrainz.search.solrwriter.json;

import org.musicbrainz.search.solrwriter.AbstractMBWriterWorkTest;
import org.musicbrainz.search.solrwriter.MBJSONWriterTestInterface;

public class MBJSONWriterWorkTest extends AbstractMBWriterWorkTest implements MBJSONWriterTestInterface {
	static {
		corename = "work";
	}
}
