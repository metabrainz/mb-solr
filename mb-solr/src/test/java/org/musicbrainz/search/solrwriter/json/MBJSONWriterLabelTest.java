package org.musicbrainz.search.solrwriter.json;

import org.musicbrainz.search.solrwriter.AbstractMBWriterLabelTest;
import org.musicbrainz.search.solrwriter.MBJSONWriterTestInterface;

public class MBJSONWriterLabelTest extends AbstractMBWriterLabelTest implements MBJSONWriterTestInterface {
	static {
		corename = "label";
	}
}
