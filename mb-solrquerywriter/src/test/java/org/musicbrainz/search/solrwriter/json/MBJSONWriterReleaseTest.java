package org.musicbrainz.search.solrwriter.json;

import org.musicbrainz.search.solrwriter.AbstractMBWriterReleaseTest;
import org.musicbrainz.search.solrwriter.MBJSONWriterTestInterface;

public class MBJSONWriterReleaseTest extends AbstractMBWriterReleaseTest implements MBJSONWriterTestInterface {
	static {
		corename = "release";
	}
}
