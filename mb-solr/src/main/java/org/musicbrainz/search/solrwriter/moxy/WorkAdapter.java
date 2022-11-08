/* Copyright (c) 2012 Paul Taylor
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

import org.musicbrainz.mmd2.LanguageList;
import org.musicbrainz.mmd2.Relation;
import org.musicbrainz.mmd2.RelationList;
import org.musicbrainz.mmd2.Work;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.util.ArrayList;
import java.util.List;

public class WorkAdapter extends NotUnmarshallableXmlAdapter<WorkAdapter.AdaptedWork, Work> {

    public static class AdaptedWork extends Work {
        public List<Relation> relations = new ArrayList<Relation>();
        public List<String> languages = new ArrayList<String>();
    }

    /**
     * Call when convert model to json, replaces work in model with adaptedWork
     * which does not contain a list of RelationList, instead all relations in each existing
     * RelationList are merged into a list of relations. We do this because it is not possible to merge
     * a List of RelationLists into the work using oxml.xml mapping
     */
    @Override
    public AdaptedWork marshal(Work work) throws Exception {

        AdaptedWork adaptedWork = new AdaptedWork();
        for(RelationList relationList : work.getRelationList()) {
            for(Relation relation : relationList.getRelation()) {
                adaptedWork.relations.add(relation);
            }
        }

        LanguageList languageList = work.getLanguageList();
        if (languageList != null)
        {
            for(LanguageList.Language language: languageList.getLanguage())
                adaptedWork.languages.add(language.getValue());
        }


        //Also need to copy any other elements/attributes we may want to output
        adaptedWork.setAliasList(work.getAliasList());
        adaptedWork.setArtistCredit(work.getArtistCredit());
        adaptedWork.setDisambiguation(work.getDisambiguation());
        adaptedWork.setId(work.getId());
        adaptedWork.setIswcList(work.getIswcList());
        adaptedWork.setLanguage(work.getLanguage());
        adaptedWork.setRating(work.getRating());
        adaptedWork.setScore(work.getScore());
        adaptedWork.setTitle(work.getTitle());
        adaptedWork.setTagList(work.getTagList());
        adaptedWork.setType(work.getType());
        adaptedWork.setUserRating(work.getUserRating());
        adaptedWork.setUserTagList(work.getUserTagList());
        return adaptedWork;
    }
}