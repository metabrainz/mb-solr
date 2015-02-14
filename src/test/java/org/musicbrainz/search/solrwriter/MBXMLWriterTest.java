package org.musicbrainz.search.solrwriter;

import org.apache.solr.SolrTestCaseJ4;
import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.xmlunit.builder.DiffBuilder;
import org.xmlunit.builder.Input;
import org.xmlunit.diff.Diff;

import javax.xml.transform.Source;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

public class MBXMLWriterTest extends SolrTestCaseJ4{
	private void performCoreTest(String corename, ArrayList<String> documentValues) throws Exception {
		initCore("solrconfig.xml", "schema.xml", "mbsssss", corename);
		addDocument(corename, documentValues);
		String xmlfilepath;
		byte[] content;
		String xml;

		xmlfilepath = MBXMLWriterTest.class.getResource(corename + "-list.xml").getFile();
		content = Files.readAllBytes(Paths.get(xmlfilepath));
		xml = new String(content);
		Source control = Input.fromMemory(xml).build();

		String response = h.query(req("q", "*:*", "fl", "score", "wt", "mbxml"));
		Source test = Input.fromMemory(response).build();

		Diff d = DiffBuilder.compare(Input.fromMemory(xml)).withTest(test).build();
		assertFalse(d.hasDifferences());
	}

	public void addDocument(String corename, ArrayList<String> documentValues) throws IOException {
		String xmlfilepath = MBXMLWriterTest.class.getResource(corename + ".xml").getFile();
		byte[] content = Files.readAllBytes(Paths.get(xmlfilepath));
		String xml = new String(content);
		documentValues.add(0, xml);
		documentValues.add(0, "_store");
		assertU(adoc((documentValues.toArray(new String[documentValues.size()]))));
		assertU(commit());
	}

	@After
	/**
	 * Call deleteCore after each test method.
	 */
	public void deleteCoreAfterTestMethod() {
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

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	/**
	 * Check that a useful error message is shown to the user if 'score' is not in the field list.
	 */
	public void testNoScoreException() throws Exception {
		// artist could be any valid core name
		initCore("solrconfig.xml", "schema.xml", "mbsssss", "artist");
		ArrayList<String> doc = new ArrayList<String>(Arrays.asList(new String[]{
				"mbid",	"9b58672a-e68e-4972-956e-a8985a165a1f",
				"artist", "Howard Shore",
				"sortname", "Shore, Howard"}));
		addDocument("artist", doc);
		thrown.expectMessage(MBXMLWriter.SCORE_NOT_IN_FIELD_LIST);
		h.query(req("q", "*:*", "wt", "mbxml"));
	}

	@Test
	/**
	 * Check that a useful error message is shown to the user if the document doesn't have a '_store' field.
	 */
	public void testNoStore() throws Exception {
		// artist could be any valid core name
		initCore("solrconfig.xml", "schema.xml", "mbsssss", "artist");
		ArrayList<String> doc = new ArrayList<String>(Arrays.asList(new String[]{
				"mbid",	"9b58672a-e68e-4972-956e-a8985a165a1f",
				"artist", "Howard Shore",
				"sortname", "Shore, Howard"}));
		assertU(adoc(doc.toArray(new String[doc.size()])));
		assertU(commit());
		thrown.expectMessage(MBXMLWriter.NO_STORE_VALUE);
		h.query(req("q", "*:*", "fl", "score", "wt", "mbxml"));
	}
}
