package org.musicbrainz.search.solrwriter.json;

import org.musicbrainz.search.solrwriter.AbstractMBWriterEditorTest;
import org.musicbrainz.search.solrwriter.MBJSONWriterTestInterface;

public class MBJSONWriterEditorTest extends AbstractMBWriterEditorTest implements MBJSONWriterTestInterface {
	static {
		corename = "editor";
	}
}
