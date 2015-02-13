package org.musicbrainz.search.solrwriter;

import org.apache.solr.SolrTestCaseJ4;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MBXMLWriterTest extends SolrTestCaseJ4{
	@BeforeClass
	public static void beforeClass() throws Exception {
		initCore("solrconfig.xml", "schema.xml", "mbsssss", "artist");
		String xmlfilepath = MBXMLWriterTest.class.getResource("artist.xml").getFile();
		byte[] content = Files.readAllBytes(Paths.get(xmlfilepath));
		String xml = new String(content);
		assertU(adoc("mbid", "9b58672a-e68e-4972-956e-a8985a165a1f", "artist", "Howard Shore", "sortname", "Shore, " +
				"Howard", "_store", xml));
		assertU(commit());
	}

	@Test
	public void testMeep() throws Exception {
		String xmlfilepath = MBXMLWriterTest.class.getResource("artist-list.xml").getFile();
		byte[] content = Files.readAllBytes(Paths.get(xmlfilepath));
		String xml = new String(content);
		assertEquals(xml, h.query(req("q", "Howard Shore", "fl", "score", "wt", "mbxml")));
	}
}
