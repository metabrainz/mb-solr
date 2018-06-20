package org.musicbrainz.search.solrwriter;

import org.apache.solr.SolrTestCaseJ4;
import org.w3c.dom.Attr;
import org.xmlunit.builder.DiffBuilder;
import org.xmlunit.builder.Input;
import org.xmlunit.diff.Diff;
import org.xmlunit.diff.Difference;

import javax.xml.transform.Source;

public interface MBXMLWriterTestInterface extends MBWriterTestInterface {
	@Override
	default public String getWritername() {
		return "mbxml";
	}

	@Override
	default String getExpectedFileExtension() {
		return "xml";
	}

	@Override
	default public void compare(String expected, String actual) {
		Source test = Input.fromString(actual).build();

		Diff d = DiffBuilder.compare(Input.fromString(expected))
				.withTest(test)
				.withAttributeFilter
						(MBXMLWriterTestInterface::cannotSkipAttribute)
				.build();
		if (d.hasDifferences()) {
			for (Difference diff : d.getDifferences()) {
				System.err.println(diff.toString());
			}
		}
		SolrTestCaseJ4.assertFalse(d.hasDifferences());
	}

	/**
	 * Decides whether attr can not be skipped while comparing two XML
	 * documents.
	 *
	 * @param attr
	 * @return True if the attribute can not be skipped, false otherwise.
	 */
	static boolean cannotSkipAttribute(Attr attr) {
		// Only metadata's created attribute is special at the moment.
		if (attr.getName() != "created") {
			return true;
		}

		boolean isValidXMLGregorianCalendarRepresentation =
				MBWriterTestInterface
						.isValidXMLGregorianCalendarRepresentation(attr
								.getValue());
		return !isValidXMLGregorianCalendarRepresentation;
	}
}
