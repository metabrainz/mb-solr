package org.musicbrainz.search.solrwriter.json;

import org.musicbrainz.search.solrwriter.AbstractMBWriterReleaseGroupTest;
import org.musicbrainz.search.solrwriter.MBJSONWriterTestInterface;

public class MBJSONWriterReleaseGroupTest extends AbstractMBWriterReleaseGroupTest implements MBJSONWriterTestInterface {
	static {
		corename = "release-group";
	}
}
