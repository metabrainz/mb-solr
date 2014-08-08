package org.musicbrainz.search.solrwriter;

import com.sun.xml.bind.marshaller.NamespacePrefixMapper;

public class MBNamespacePrefixMapper extends NamespacePrefixMapper {

	@Override
	public String getPreferredPrefix(String namespaceUri, String suggestion, boolean requirePrefix) {
            if(namespaceUri.equals("http://musicbrainz.org/ns/ext#-2.0"))
            {
                return "ext";
            }
            return suggestion;
	}

}
