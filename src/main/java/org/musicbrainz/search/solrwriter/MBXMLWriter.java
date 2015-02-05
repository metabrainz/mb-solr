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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;
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
	private Unmarshaller unmarshaller = null;
	protected Marshaller marshaller = null;
	private ObjectFactory objectfactory = null;

	/**
	 * The entity type of this MBXMLWriter
	 */
	private entityTypes entityType = null;

	private enum entityTypes {
		artist, area, cdstub, label, place, recording, release, release_group, tag, work;

		public static entityTypes getType(String entityType) {
			for (entityTypes et : entityTypes.values()) {
				if (et.name().equalsIgnoreCase(entityType)) {
					return et;
				}
			}
			throw new IllegalArgumentException(entityType
					+ "is not a valid entity type");
		}
	}

	/**
	 * MetadataListWrapper wraps common functionality in dealing with
	 * {@link Metadata}. This includes generating the appropriate list object
	 * for different entity types, as well as calling the appropriate method to
	 * set the list on the {@link Metadata} object.
	 */
	class MetadataListWrapper {
		private List objList = null;
		private Object MMDList = null;
		private Metadata metadata = null;
		private Method setCountMethod = null;
		private Method setOffsetMethod = null;

		public MetadataListWrapper() {
			switch (entityType) {
			case area:
				MMDList = objectfactory.createAreaList();
				objList = ((AreaList) MMDList).getArea();
				break;
			case artist:
				MMDList = objectfactory.createArtistList();
				objList = ((ArtistList) MMDList).getArtist();
				break;
			case cdstub:
				MMDList = objectfactory.createCdstubList();
				objList = ((CdstubList) MMDList).getCdstub();
				break;
			case label:
				MMDList = objectfactory.createLabelList();
				objList = ((LabelList) MMDList).getLabel();
				break;
			case place:
				MMDList = objectfactory.createPlaceList();
				objList = ((PlaceList) MMDList).getPlace();
				break;
			case recording:
				MMDList = objectfactory.createRecordingList();
				objList = ((RecordingList) MMDList).getRecording();
				break;
			case release:
				MMDList = objectfactory.createReleaseList();
				objList = ((ReleaseList) MMDList).getRelease();
				break;
			case release_group:
				MMDList = objectfactory.createReleaseGroupList();
				objList = ((ReleaseGroupList) MMDList).getReleaseGroup();
				break;
			case tag:
				MMDList = objectfactory.createTagList();
				objList = ((TagList) MMDList).getTag();
				break;
			case work:
				MMDList = objectfactory.createWorkList();
				objList = ((WorkList) MMDList).getWork();
				break;
			default:
				// This should never happen because MBXMLWriters init method
				// aborts earlier
				throw new IllegalArgumentException("invalid entity type: "
						+ entityType);
			}

			this.metadata = objectfactory.createMetadata();
			try {
				setCountMethod = MMDList.getClass().getMethod("setCount",
						BigInteger.class);
				setOffsetMethod = MMDList.getClass().getMethod("setOffset",
						BigInteger.class);
			} catch (NoSuchMethodException | SecurityException e) {
				throw new RuntimeException(e);
			}
		}

		public List getLiveList() {
			return objList;
		}

		public Metadata getCompletedMetadata() {
			switch (entityType) {
			case area:
				metadata.setAreaList((AreaList) MMDList);
				break;
			case artist:
				metadata.setArtistList((ArtistList) MMDList);
				break;
			case cdstub:
				metadata.setCdstubList((CdstubList) MMDList);
				break;
			case label:
				metadata.setLabelList((LabelList) MMDList);
				break;
			case recording:
				metadata.setRecordingList((RecordingList) MMDList);
				break;
			case place:
				metadata.setPlaceList((PlaceList) MMDList);
				break;
			case release:
				metadata.setReleaseList((ReleaseList) MMDList);
				break;
			case release_group:
				metadata.setReleaseGroupList((ReleaseGroupList) MMDList);
				break;
			case tag:
				metadata.setTagList((TagList) MMDList);
				break;
			case work:
				metadata.setWorkList((WorkList) MMDList);
				break;
			default:
				// This should never happen because MBXMLWriters init method
				// aborts earlier
				throw new IllegalArgumentException("invalid entity type: "
						+ entityType);
			}
			return metadata;
		}

		public void setCountAndOffset(int count, int offset) {
			try {
				setCountMethod.invoke(MMDList, BigInteger.valueOf(count));
				setOffsetMethod.invoke(MMDList, BigInteger.valueOf(offset));
			} catch (IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				throw new RuntimeException(e);
			}
		}
	}

	public String getContentType(SolrQueryRequest arg0, SolrQueryResponse arg1) {
		return CONTENT_TYPE_XML_UTF8;
	}

	private JAXBContext createJAXBContext() throws JAXBException {
		return JAXBContext
				.newInstance(org.musicbrainz.mmd2.ObjectFactory.class);
	}

	protected Marshaller createMarshaller() throws JAXBException {
		return context.createMarshaller();
	}

	public void init(NamedList initArgs) {
		try {
			context = createJAXBContext();
			marshaller = createMarshaller();
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
			entityType = entityTypes.getType((String) entityTypeTemp);
		}
	}

	private static void adjustScore(float maxScore, Object object,
			float objectScore) {
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
		MetadataListWrapper metadatalistwrapper = new MetadataListWrapper();

		NamedList vals = res.getValues();

		ResultContext con = (ResultContext) vals.get("response");

		metadatalistwrapper.setCountAndOffset(con.docs.matches(),
				con.docs.offset());

		List xmlList = metadatalistwrapper.getLiveList();

		float maxScore = con.docs.maxScore();
		DocIterator iter = con.docs.iterator();

		while (iter.hasNext()) {
			String store;
			Integer id = iter.nextDoc();
			Document doc = req.getSearcher().doc(id);
			try {
				store = doc.getField("_store").stringValue();
			} catch (NullPointerException e) {
				throw new RuntimeException(
						"The document didn't include a _store value");
			}
			if (store == null) {
				throw new RuntimeException(
						"_store should be a string value but wasn't");
			}
			Object unmarshalledObj = null;
			try {
				unmarshalledObj = unmarshaller
						.unmarshal(new ByteArrayInputStream(store.getBytes()));
			} catch (JAXBException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				continue;
			}

			/*
			Areas are stored as {@link org.musicbrainz.mmd2.DefAreaElementInner.DefAreaElementInner}, but that is not
			 defined as an XmlRootElement. To work around this, every area is stored in an AreaList with only one
			 element that we access here.

			 TODO: Figure out if there's a way around this.
			 */
			if (unmarshalledObj instanceof AreaList) {
				List<DefAreaElementInner> arealist = ((AreaList) unmarshalledObj).getArea();
				if (arealist.size() == 0) {
					throw new RuntimeException("Expected an AreaList with at least one element");
				}
				unmarshalledObj = (Object) arealist.get(0);
			}

			// TODO: this needs "score" in the field list of Solr, otherwise
			// this causes a NullPointerException
			adjustScore(maxScore, unmarshalledObj, iter.score());

			xmlList.add(unmarshalledObj);
		}

		StringWriter sw = new StringWriter();
		try {
			marshaller.marshal(metadatalistwrapper.getCompletedMetadata(), sw);
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		writer.write(sw.toString());
	}
}
