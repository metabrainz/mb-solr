package org.musicbrainz.search.solrwriter;

import name.falgout.jeffrey.testing.LambdaMatcher;
import org.hamcrest.Matcher;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;

import static org.hamcrest.core.IsEqual.equalTo;

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

	/**
	 *
	 * @param value
	 * @return True, if value can be converted to an XMLGregorianCalendar,
	 * false otherwise.
	 */
	static boolean isValidXMLGregorianCalendarRepresentation(String value) {
		try {
			DatatypeFactory.
					newInstance().
					newXMLGregorianCalendar(value);
		} catch (DatatypeConfigurationException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}


	/**
	 * @return A Matcher that verifies if a String can be converted to a valid
	 * XMLGregorianCalendar object.
	 */
	static Matcher isValidXMLGregorianCalendarMatcher() {
		return LambdaMatcher.createMatcher
				(MBWriterTestInterface
								::isValidXMLGregorianCalendarRepresentation,
						equalTo(true));
	}
}
