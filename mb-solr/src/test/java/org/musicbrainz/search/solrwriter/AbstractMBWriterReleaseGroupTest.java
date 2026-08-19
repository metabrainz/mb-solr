package org.musicbrainz.search.solrwriter;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class AbstractMBWriterReleaseGroupTest extends AbstractMBWriterTest {
	@Override
	public ArrayList<String> getDoc() {
		return new ArrayList<>(Arrays.asList(new String[]{
				"mbid", "268d91a0-1a7f-4600-bfd4-74d738eb7de4",
				"releasegroup", "Chiptunes = WIN \\m|♥|m/", "calc_type", "Compilation",
				"primarytype", "Album", "primarytype_gid", "f529b476-6e62-324f-b0aa-1f3e33d313fc",
				"secondarytype", "Compilation", "secondarytype_gid", "dd2a21e1-0c00-3729-a7a0-de60b84eb5d1",
				"artist_credit_json", "{\"id\":\"8d24c5da-ac9d-3a77-866b-91c7e2177ede\",\"name_credits\":[{\"artist\":{\"id\":\"89ad4ac3-39f7-470e-963a-56509c546377\",\"name\":\"Various Artists\",\"sort_name\":\"Various Artists\",\"disambiguation\":\"add compilations to this artist\"}}]}",
				"release_json", "{\"id\":\"871f12ec-6259-4e93-b440-9108559c38b1\",\"title\":\"Chiptunes = WIN \\\\m|♥|m/\",\"status\":\"Official\",\"status_id\":\"4e304316-386d-3409-af2e-78857eec5cfe\"}",
				"tag", "electronic", "tag_count", "1"
		}));
	}
}
