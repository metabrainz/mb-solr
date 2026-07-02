/* Copyright (c) 2026 MetaBrainz Foundation
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. Neither the name of the MusicBrainz project nor the names of the
 *    contributors may be used to endorse or promote products derived from
 *    this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED
 * TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.musicbrainz.search.solrwriter.moxy;

import jakarta.xml.bind.annotation.XmlElement;
import org.musicbrainz.mmd2.Relation;

/**
 * This adapter class only exists to extend `Relation` with a `target-type`
 * attribute. Since the target type is only accessible on the parent
 * `RelationList`, it's not actually assigned to here, but via
 * `RelationListUtil.marshal`. All other relation properties are copied onto
 * the adapted relation instance as-is.
 */

public class RelationAdapter extends NotUnmarshallableXmlAdapter<RelationAdapter.AdaptedRelation, Relation> {

    public static class AdaptedRelation extends Relation {
        @XmlElement(name = "target-type")
        protected String targetType;
    }

    @Override
    public AdaptedRelation marshal(Relation relation) throws Exception {
        AdaptedRelation adaptedRelation = new AdaptedRelation();

        adaptedRelation.setOrderingKey(relation.getOrderingKey());
        adaptedRelation.setDirection(relation.getDirection());
        adaptedRelation.setAttributeList(relation.getAttributeList());
        adaptedRelation.setBegin(relation.getBegin());
        adaptedRelation.setEnd(relation.getEnd());
        adaptedRelation.setEnded(relation.getEnded());
        adaptedRelation.setSourceCredit(relation.getSourceCredit());
        adaptedRelation.setTargetCredit(relation.getTargetCredit());
        adaptedRelation.setType(relation.getType());
        adaptedRelation.setTypeId(relation.getTypeId());

        adaptedRelation.setArea(relation.getArea());
        adaptedRelation.setArtist(relation.getArtist());
        adaptedRelation.setEvent(relation.getEvent());
        adaptedRelation.setInstrument(relation.getInstrument());
        adaptedRelation.setLabel(relation.getLabel());
        adaptedRelation.setPlace(relation.getPlace());
        adaptedRelation.setRecording(relation.getRecording());
        adaptedRelation.setRelease(relation.getRelease());
        adaptedRelation.setReleaseGroup(relation.getReleaseGroup());
        adaptedRelation.setSeries(relation.getSeries());
        adaptedRelation.setTarget(relation.getTarget());
        adaptedRelation.setWork(relation.getWork());

        return adaptedRelation;
    }
}
