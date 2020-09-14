package org.musicbrainz.search.solrwriter;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class AbstractMBWriterRecordingTest extends AbstractMBWriterTest {
	@Override
	public ArrayList<String> getDoc() {
		return new ArrayList<>(Arrays.asList(new String[]{
				"mbid", uuid,
				"recording", "Roots and Beginnings",
				"arid", "9b58672a-e68e-4972-956e-a8985a165a1f",
				"artist", "Howard Shore",
				"artistname", "Howard Shore",
				"creditname", "Howard Shore",
				"video", "false"
		}));
	}
}
