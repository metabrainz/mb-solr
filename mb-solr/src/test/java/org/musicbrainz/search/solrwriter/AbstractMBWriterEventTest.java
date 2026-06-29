package org.musicbrainz.search.solrwriter;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class AbstractMBWriterEventTest extends AbstractMBWriterTest {
	@Override
	public ArrayList<String> getDoc() {
		return new ArrayList<>(Arrays.asList(new String[]{
				"event", "Wacken Open Air 2024",
				"mbid", uuid}));
	}
}
