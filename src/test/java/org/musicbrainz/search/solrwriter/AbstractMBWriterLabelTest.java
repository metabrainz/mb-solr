package org.musicbrainz.search.solrwriter;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class AbstractMBWriterLabelTest extends AbstractMBWriterTest {
	@Override
	public ArrayList<String> getDoc() {
		return new ArrayList<>(Arrays.asList(new String[]{
				"mbid", uuid,
				"laid", uuid,
				"label", "Deutsche Grammophon"}));
	}
}
