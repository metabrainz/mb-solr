package org.musicbrainz.search.solrwriter;

import org.apache.solr.SolrTestCaseJ4;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.update.CommitUpdateCommand;
import org.junit.After;
import org.junit.Test;

import java.nio.file.Paths;

/**
 * The cdstub and tag cores have no MBID: the indexer (sir) identifies their
 * documents by the numeric database row id, sent in the field "id", and
 * deletes them by this id.
 * This id is internal: it must not be searchable through the advanced
 * (edismax) request handler, and it is never returned in responses.
 */
public abstract class AbstractInternalIdTest extends SolrTestCaseJ4 {
	protected static final String ID = "42";

	protected static void initInternalIdCore(String coreName) throws Exception {
		System.setProperty("solr.directoryFactory", "solr.NRTCachingDirectoryFactory");
		initCore("solrconfig.xml", "schema.xml", Paths.get("../mbsssss").toAbsolutePath(), coreName);
	}

	/**
	 * @return the fields and values of a document as sent by sir,
	 * including "id" with the value {@link #ID}
	 */
	protected abstract String[] getIndexedDocument();

	/**
	 * @return a public query that matches the indexed document
	 */
	protected abstract String getPublicQuery();

	@After
	public void clean() {
		clearIndex();
	}

	/**
	 * The default update chain ignores commits from clients.
	 */
	private static void commitBypassingUpdateChain() throws Exception {
		try (SolrQueryRequest commitRequest = req()) {
			h.getCore().getUpdateHandler().commit(new CommitUpdateCommand(commitRequest, false));
		}
	}

	private void assertFound(String query, int count) {
		assertQ(req("qt", "/advanced", "q", query, "fl", "score,_store"),
				"//result[@numFound='" + count + "']");
	}

	@Test
	public void indexesAndDeletesDocumentById() throws Exception {
		assertU(adoc(getIndexedDocument()));
		commitBypassingUpdateChain();
		assertFound(getPublicQuery(), 1);

		assertU(delI(ID));
		commitBypassingUpdateChain();
		assertFound(getPublicQuery(), 0);
	}

	@Test
	public void doesNotExposeIdAsSearchField() throws Exception {
		assertU(adoc(getIndexedDocument()));
		commitBypassingUpdateChain();
		assertFound("id:" + ID, 0);
		// Not searched by default either ("qf")
		assertFound(ID, 0);
	}
}
