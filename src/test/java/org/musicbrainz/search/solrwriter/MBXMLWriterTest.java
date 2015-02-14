package org.musicbrainz.search.solrwriter;

import org.apache.solr.SolrTestCaseJ4;
import org.junit.BeforeClass;
import org.junit.Test;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

public class MBXMLWriterTest extends SolrTestCaseJ4{
	private void performCoreTest(String corename, ArrayList<String> documentValues) throws Exception {
		initCore("solrconfig.xml", "schema.xml", "mbsssss", corename);
		String xmlfilepath = MBXMLWriterTest.class.getResource(corename + ".xml").getFile();
		byte[] content = Files.readAllBytes(Paths.get(xmlfilepath));
		String xml = new String(content);
		documentValues.add(0, xml);
		documentValues.add(0, "_store");
		assertU(adoc((documentValues.toArray(new String[documentValues.size()]))));
		assertU(commit());

		xmlfilepath = MBXMLWriterTest.class.getResource(corename + "-list.xml").getFile();
		content = Files.readAllBytes(Paths.get(xmlfilepath));
		xml = new String(content);
		assertEquals(xml, h.query(req("q", "*:*", "fl", "score", "wt", "mbxml")));
		deleteCore();
	}
	@Test
	public void testArtist() throws Exception {
		ArrayList<String> doc = new ArrayList<String>(Arrays.asList(new String[]{
				"mbid",	"9b58672a-e68e-4972-956e-a8985a165a1f",
				"artist", "Howard Shore",
				"sortname", "Shore, Howard"}));
		performCoreTest("artist", doc);
	}

	@Test
	public void testArea() throws Exception {
		ArrayList<String> doc = new ArrayList<>(Arrays.asList(new String[]{
				"area", "Thüringen",
				"mbid", "ff2ee1ad-febe-4b48-8999-e77870b62744"}));
		performCoreTest("area", doc);
	}
}
