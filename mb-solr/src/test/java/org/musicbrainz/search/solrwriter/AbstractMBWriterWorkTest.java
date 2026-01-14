package org.musicbrainz.search.solrwriter;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class AbstractMBWriterWorkTest extends AbstractMBWriterTest {
	@Override
	public ArrayList<String> getDoc() {
		return new ArrayList<>(Arrays.asList(new String[]{
				"work", "Poker Face",
				"mbid", uuid}));
	}
}
