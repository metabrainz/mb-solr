package org.musicbrainz.search.solrwriter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Method;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.lucene.document.Document;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.response.*;
import org.apache.solr.search.DocIterator;
import org.musicbrainz.mmd2.*;

public class MBXMLWriter implements QueryResponseWriter {

	/**
	 * The context used to (un-)serialize XML
	 */
	private JAXBContext context = null;
	private Marshaller marshaller = null;
	private Unmarshaller unmarshaller = null;
	private ObjectFactory objectfactory = null;

	/**
	 * The entity type of this MBXMLWriter
	 */
	private String entityType = null;

	private enum entityTypes {
		artist, area, label, recording, release, release_group, work;

		public static boolean isValidType(String entityType) {
			for (entityTypes et : entityTypes.values()) {
				if (et.toString() == entityType) {
					return true;
				}
			}
			return false;
		}
	}

	public String getContentType(SolrQueryRequest arg0, SolrQueryResponse arg1) {
		return CONTENT_TYPE_XML_UTF8;
	}

	public void init(NamedList initArgs) {
		try {
			context = JAXBContext
					.newInstance(org.musicbrainz.mmd2.ObjectFactory.class);
			marshaller = context.createMarshaller();
			unmarshaller = context.createUnmarshaller();
		} catch (JAXBException e) {
			throw new RuntimeException(e);
		}

		/*
		 * Check for which entity type we're generating responses
		 */
		objectfactory = new ObjectFactory();
		Object entityTypeTemp = initArgs.get("entitytype");

		if (entityTypeTemp == null) {
			throw new RuntimeException("no entitytype given");
		} else {
			entityType = (String) entityType;
		}

		if (entityTypes.isValidType(entityType)) {
			throw new RuntimeException(entityType
					+ "is not a valid entity type");
		}
	}

	private static void adjustScore(float maxScore, Object object,
			float objectScore) throws IOException {
		Method setScoreMethod = null;
		try {
			setScoreMethod = object.getClass().getMethod("setScore",
					String.class);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(
					"Expected an object with a setScore method");
		}

		int adjustedScore = (((int) (((objectScore / maxScore)) * 100)));

		try {
			setScoreMethod.invoke(object, Integer.toString(adjustedScore));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void write(Writer writer, SolrQueryRequest req, SolrQueryResponse res)
			throws IOException {
		NamedList vals = res.getValues();

		org.apache.solr.response.ResultContext con = (ResultContext) vals
				.get("response");

		Metadata metadata = objectfactory.createMetadata();
		WorkList wl = objectfactory.createWorkList();
		List<Work> works = wl.getWork();

		float maxScore = con.docs.maxScore();
		DocIterator iter = con.docs.iterator();

		while (iter.hasNext()) {
			Integer id = iter.nextDoc();
			Document doc = req.getSearcher().doc(id);
			String store = doc.getField("_store").stringValue();
			if (store == null) {
				throw new RuntimeException(
						"_store should be a string value but wasn't");
			}
			Work w = null;
			try {
				w = (Work) unmarshaller.unmarshal(new ByteArrayInputStream(
						store.getBytes()));
			} catch (JAXBException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				continue;
			}
			// TODO: this needs "score" in the field list of Solr, otherwise
			// this causes a NullPointerException
			adjustScore(maxScore, w, iter.score());

			works.add(w);
		}

		metadata.setWorkList(wl);
		StringWriter sw = new StringWriter();
		try {
			marshaller.marshal(metadata, sw);
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		writer.write(sw.toString());
	}
}
