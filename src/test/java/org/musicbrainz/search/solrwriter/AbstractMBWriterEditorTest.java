package org.musicbrainz.search.solrwriter;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class AbstractMBWriterEditorTest extends AbstractMBWriterTest {
	@Override
	public ArrayList<String> getDoc() {
		return new ArrayList<>(Arrays.asList(new String[]{
				"id", "1337",
				"editor", "Mineo",
				"bio", "Nothing to see here!"}));
	}
}
