package org.musicbrainz.search.solrwriter;

import org.apache.solr.SolrTestCaseJ4;
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

		Diff d = DiffBuilder.compare(Input.fromString(expected)).withTest(test).build();
		if (d.hasDifferences()) {
			for (Difference diff : d.getDifferences()) {
				System.err.println(diff.toString());
			}
		}
		SolrTestCaseJ4.assertFalse(d.hasDifferences());
	}
}
