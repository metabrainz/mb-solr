package org.musicbrainz.search.solrwriter.moxy;

import org.junit.Test;

import static org.junit.Assert.*;

public class BooleanAliasTypeAdapterTest {

	@Test
	public void testMarshal() throws Exception {
		BooleanAliasTypeAdapter adapter = new BooleanAliasTypeAdapter();
		assertEquals(true, adapter.marshal("primary"));
		assertEquals(null, adapter.marshal("secondary"));
		assertEquals(null, adapter.marshal(null));
	}
}