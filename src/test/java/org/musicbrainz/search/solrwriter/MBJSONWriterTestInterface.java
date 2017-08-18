package org.musicbrainz.search.solrwriter;

import static net.javacrumbs.jsonunit.JsonMatchers.jsonEquals;
import static org.junit.Assert.assertThat;

public interface MBJSONWriterTestInterface extends MBWriterTestInterface {
	@Override
	default String getWritername() {
		return "mbjson";
	}

	@Override
	default String getExpectedFileExtension() {
		return "json";
	}

	@Override
	default void compare(String expected, String actual) {
		assertThat(actual, jsonEquals(expected));
	}
}
