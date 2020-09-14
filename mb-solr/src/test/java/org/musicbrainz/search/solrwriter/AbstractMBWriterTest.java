package org.musicbrainz.search.solrwriter;

import org.apache.solr.SolrTestCaseJ4;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public abstract class AbstractMBWriterTest extends SolrTestCaseJ4 implements
		MBWriterTestInterface {
	@BeforeClass
	public static void beforeClass() throws Exception {
		initCore("solrconfig.xml", "schema.xml", "../mbsssss", getCorename());
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
	 * @param withStore  whether the _store field should be populated with data
	 * @param storeValue allows specifying the value for the _store field by
	 *                   setting it to a value different from null
	 * @throws IOException
	 */
	void addDocument(boolean withStore, String storeValue) throws IOException, InterruptedException {
		ArrayList<String> values = new ArrayList<>(getDoc());
		if (withStore) {
			String xml;
			if (storeValue != null) {
				xml = storeValue;
			} else {
				String xmlfilepath = MBWriterTestInterface.class.getResource
						(getCorename() + ".xml").getFile();
				byte[] content = Files.readAllBytes(Paths.get(xmlfilepath));
				xml = new String(content);
			}

			values.add(0, xml);
			values.add(0, "_store");
		}

		assertU(adoc((values.toArray(new String[values.size()]))));
		TimeUnit.SECONDS.sleep(15);
	}

	void addDocument(boolean withStore) throws Exception {
		addDocument(withStore, null);
	}

	@After
	public void After() {
		clearIndex();
	}

	@Test
	/**
	 * Check that the XML document returned is the same as the one we stored
	 * in the first place.
	 */
	public void performCoreTest() throws Exception {
		addDocument(true);
		// Sleep to allow auto soft commit
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

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	/**
	 * Check that the expected error message is shown for documents with a
	 * '_store' field with a value that can't be
	 * unmarshalled.
	 */
	public void testInvalidStoreException() throws Exception {
		addDocument(true, "invalid");
		// Sleep to allow auto soft commit
		thrown.expectMessage(MBXMLWriter.UNMARSHALLING_STORE_FAILED +
				"invalid");
		h.query(req("qt", "/advanced", "q", "*:*", "wt", getWritername()));
	}
}
