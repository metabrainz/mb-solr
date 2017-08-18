package org.musicbrainz.search.solrwriter.json;

import org.musicbrainz.search.solrwriter.AbstractMBWriterCDStubTest;
import org.musicbrainz.search.solrwriter.MBJSONWriterTestInterface;

public class MBJSONWriterCDStubTest extends AbstractMBWriterCDStubTest implements MBJSONWriterTestInterface {
	static {
		corename = "cdstub";
	}
}
