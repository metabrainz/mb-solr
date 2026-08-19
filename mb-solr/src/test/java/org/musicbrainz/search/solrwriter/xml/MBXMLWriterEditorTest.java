package org.musicbrainz.search.solrwriter.xml;

import org.musicbrainz.search.solrwriter.AbstractMBWriterEditorTest;
import org.musicbrainz.search.solrwriter.MBFlatXMLWriterTestInterface;

public class MBXMLWriterEditorTest extends AbstractMBWriterEditorTest implements MBFlatXMLWriterTestInterface {
	static {
		corename = "editor";
	}
}
