package org.musicbrainz.search.solrwriter;

import org.apache.solr.SolrTestCaseJ4;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public abstract class AbstractMBWriterTest extends SolrTestCaseJ4 implements
		MBWriterTestInterface {
	@BeforeClass
	public static void beforeClass() throws Exception {
		System.setProperty("solr.directoryFactory", "solr.NRTCachingDirectoryFactory");
		initCore("solrconfig.xml", "schema.xml", new File("../mbsssss").getAbsolutePath(), getCorename());
	}

	public static String corename;

	public static String getCorename() {
		return corename;
	}

	/**
	 * Get the document to use in the tests
	 */
	abstract ArrayList<String> getDoc();

	/**
	 * Add a document containing doc to the current core.
	 *
	 * @throws IOException
	 */
	void addDocument() throws IOException, InterruptedException {
		ArrayList<String> values = new ArrayList<>(getDoc());

		assertU(adoc((values.toArray(new String[values.size()]))));
		// The configured update processor ignores client commits. Wait for the
		// configured auto soft commit so the flat document is searchable.
		TimeUnit.SECONDS.sleep(15);
	}

	@After
	public void After() {
		clearIndex();
	}

	@Test
	/**
	 * Check that the response is built from the flat fields in the indexed
	 * document, rather than from an entity XML document stored in _store.
	 */
	public void performCoreTest() throws Exception {
		addDocument();
		String expectedFile;
		byte[] content;
		String expected;

		String expectedFileName = String.format("%s-list.%s", getCorename(),
				getExpectedFileExtension());

		expectedFile = AbstractMBWriterTest.class.getResource
				(expectedFileName).getFile();
		content = Files.readAllBytes(Paths.get(expectedFile));
		expected = new String(content);

		String response = h.query(req("qt", "/advanced", "q", "*:*", "wt", getWritername()));
		compare(expected, response);
	}

}
