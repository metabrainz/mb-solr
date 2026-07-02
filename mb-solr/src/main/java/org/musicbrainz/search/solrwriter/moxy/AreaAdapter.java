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

import org.musicbrainz.mmd2.Relation;
import org.musicbrainz.mmd2.RelationList;
import org.musicbrainz.mmd2.DefAreaElementInner;

import java.util.ArrayList;
import java.util.List;

public class AreaAdapter extends NotUnmarshallableXmlAdapter<AreaAdapter.AdaptedArea, DefAreaElementInner> {

    public static class AdaptedArea extends DefAreaElementInner {
        public List<RelationAdapter.AdaptedRelation> relations;
    }

    /**
     * Call when convert model to json, replaces area in model with adaptedArea
     * which does not contain a list of RelationList, instead all relations in each existing
     * RelationList are merged into a list of relations. We do this because it is not possible to merge
     * a List of RelationLists into the area using oxml.xml mapping
     */
    @Override
    public AdaptedArea marshal(DefAreaElementInner area) throws Exception {
        AdaptedArea adaptedArea = new AdaptedArea();
        adaptedArea.relations = RelationListUtil.marshal(area.getRelationList());

        //Also need to copy any other elements/attributes we may want to output
        adaptedArea.setName(area.getName());
        adaptedArea.setSortName(area.getSortName());
        adaptedArea.setDisambiguation(area.getDisambiguation());
        adaptedArea.setIso31661CodeList(area.getIso31661CodeList());
        adaptedArea.setIso31662CodeList(area.getIso31662CodeList());
        adaptedArea.setIso31663CodeList(area.getIso31663CodeList());
        adaptedArea.setAnnotation(area.getAnnotation());
        adaptedArea.setLifeSpan(area.getLifeSpan());
        adaptedArea.setAliasList(area.getAliasList());
        adaptedArea.setTagList(area.getTagList());
        adaptedArea.setUserTagList(area.getUserTagList());
        adaptedArea.setGenreList(area.getGenreList());
        adaptedArea.setUserGenreList(area.getUserGenreList());
        adaptedArea.setId(area.getId());
        adaptedArea.setType(area.getType());
        adaptedArea.setTypeId(area.getTypeId());
        adaptedArea.setScore(area.getScore());

        return adaptedArea;
    }
}
