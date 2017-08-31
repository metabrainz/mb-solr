package org.musicbrainz.search.solrwriter;

public interface MBWriterTestInterface {
	String uuid = "fff523fa-f2db-40eb-be81-f4544b43f39c";

	/**
	 * @return The wt parameter for Solr.
	 */
	String getWritername();

	/**
	 * @return The file name extension of the expected file
	 */
	String getExpectedFileExtension();

	/**
	 * Compare expected (the content of the expected file) and actual (the
	 * response from Solr) to ensure they're equal.
	 *
	 * @param expected
	 * @param actual
	 */
	void compare(String expected, String actual);
}
