package org.musicbrainz.search.solrwriter;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class AbstractMBWriterReleaseTest extends AbstractMBWriterTest {
	@Override
	public ArrayList<String> getDoc() {
		return new ArrayList<>(Arrays.asList(new String[]{
				"mbid", "871f12ec-6259-4e93-b440-9108559c38b1", "release", "Chiptunes = WIN \\m|♥|m/",
				"artist", "Chiptunes = WIN \\m|♥|m/", "artistname", "Chiptunes = WIN \\m|♥|m/",
				"status", "Official", "status_gid", "4e304316-386d-3409-af2e-78857eec5cfe",
				"packaging", "None", "packaging_gid", "119eba76-b343-3e02-a292-f0f00644bb9b", "lang", "eng", "script", "Latn",
				"artist_credit_json", "{\"id\":\"8d24c5da-ac9d-3a77-866b-91c7e2177ede\",\"name_credits\":[{\"artist\":{\"id\":\"2b542c4a-694a-49b9-b475-9f55f99436a1\",\"name\":\"Chiptunes = WIN \\\\m|♥|m/\",\"sort_name\":\"Chiptunes = WIN \\\\m|♥|m/\",\"aliases\":[{\"name\":\"Chiptunes = WIN\",\"sort_name\":\"Chiptunes = WIN\"}]}}]}",
				"rgid", "268d91a0-1a7f-4600-bfd4-74d738eb7de4", "calc_type", "Compilation",
				"primarytype", "Album", "primarytype_gid", "f529b476-6e62-324f-b0aa-1f3e33d313fc",
				"secondarytype", "Compilation", "secondarytype_gid", "dd2a21e1-0c00-3729-a7a0-de60b84eb5d1",
				"release_event_json", "{\"date\":\"2012-06-05\",\"area\":{\"id\":\"525d4e18-3d00-31b9-a58b-a146a916de8f\",\"name\":\"[Worldwide]\",\"iso_3166_1_codes\":[\"XW\"]}}",
				"barcode_none", "true",
				"label_info_json", "{\"label\":{\"id\":\"7669cf9a-7328-44cf-96ba-b2dc9d534ed8\",\"name\":\"Chiptunes = WIN \\\\m|♥|m/\"}}",
				"medium_json", "{\"id\":\"47ceaa3e-405c-4f9b-8999-5e4f7fd2134b\",\"format\":\"Digital Media\",\"disc_count\":0,\"track_count\":51}",
				"tag", "bitunes", "tag_count", "1", "tag", "bandcamp", "tag_count", "1", "tag", "chiptunes", "tag_count", "2"}));
	}
}
