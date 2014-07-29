package org.musicbrainz.search.solrwriter;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.solr.common.util.NamedList;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.response.SolrQueryResponse;
import org.eclipse.persistence.jaxb.JAXBContextFactory;
import org.eclipse.persistence.jaxb.JAXBContextProperties;
import org.musicbrainz.mmd2.Metadata;

public class MBJSONWriter extends MBXMLWriter {
	/**
	 * A separate context for json. The context for XML is still necessary to
	 * *un*marshal the _store field
	 */
	private JAXBContext jsonContext = null;

	private JAXBContext createJAXBJSONContext() throws JAXBException {
		try {
			Map<String, Object> properties = new HashMap<String, Object>(3);
			properties.put(JAXBContextProperties.OXM_METADATA_SOURCE,
					"oxml.xml");
			properties
					.put(JAXBContextProperties.MEDIA_TYPE, "application/json");
			properties.put(JAXBContextProperties.JSON_INCLUDE_ROOT, false);
			properties.put(JAXBContextProperties.JSON_VALUE_WRAPPER, "name");
			return JAXBContextFactory.createContext(
					new Class[] { Metadata.class }, properties);
		} catch (JAXBException ex) {
			// Unable to initilize
			// jaxb/Users/paul/code/MusicBrainz/SearchServer/servlet/src/main/resources/oxml.xml
			// context, should never happen
			throw new RuntimeException(ex);
		}
	}

	@Override
	protected Marshaller createMarshaller() throws JAXBException {
		if (jsonContext == null) {
			return null;
		} else {
			return jsonContext.createMarshaller();
		}
	}

	@Override
	public void init(NamedList initArgs) {
		super.init(initArgs);
		try {
			jsonContext = createJAXBJSONContext();
			marshaller = createMarshaller();
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
	}

	@Override
	public String getContentType(SolrQueryRequest arg0, SolrQueryResponse arg1) {
		return "application/json";
	}

	@Override
	protected void doWrite(Writer writer) throws IOException {
		StringWriter sw = new StringWriter();
		try {
			marshaller.marshal(this.metadatalistwrapper.getCompletedMetadata(),
					sw);
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		writer.write(sw.toString());
	}
}
