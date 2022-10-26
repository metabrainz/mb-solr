/*
 * Copyright (c) 2014, Wieland Hoffmann
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 * 
 * 3. Neither the name of the copyright holder nor the names of its contributors
 * may be used to endorse or promote products derived from this software without
 * specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.musicbrainz.search.solrwriter;

import java.util.HashMap;
import java.util.Map;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;

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

	private JAXBContext createJAXBJSONContext() {
		try {
			Map<String, Object> properties = new HashMap<>(3);
			properties.put(JAXBContextProperties.OXM_METADATA_SOURCE,
					"oxml.xml");
			properties
					.put(JAXBContextProperties.MEDIA_TYPE, "application/json");
			properties.put(JAXBContextProperties.JSON_INCLUDE_ROOT, false);
			properties.put(JAXBContextProperties.JSON_VALUE_WRAPPER, "name");
			return JAXBContextFactory.createContext(
					new Class[] { Metadata.class }, properties);
		} catch (JAXBException ex) {
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
	protected JAXBContext createJAXBErrorContext() throws JAXBException {
		Map<String, Object> properties = new HashMap<>();
		properties
				.put(JAXBContextProperties.MEDIA_TYPE, "application/json");
		properties.put(JAXBContextProperties.JSON_INCLUDE_ROOT, true);
		return JAXBContextFactory.createContext(
				new Class[] { MBError.class }, properties);
	}

	@Override
	protected Marshaller createErrorMarshaller() throws JAXBException {
		if (errorContext == null) {
			return null;
		} else {
			return errorContext.createMarshaller();
		}
	}

	@Override
	public void init(NamedList initArgs) {
		super.init(initArgs);
		try {
			jsonContext = createJAXBJSONContext();
			errorContext = createJAXBErrorContext();
			errorMarshaller = createErrorMarshaller();
		} catch (JAXBException ex) {
			throw new RuntimeException(ex);
		}
	}

	@Override
	public String getContentType(SolrQueryRequest arg0, SolrQueryResponse arg1) {
		return "application/json";
	}
}
