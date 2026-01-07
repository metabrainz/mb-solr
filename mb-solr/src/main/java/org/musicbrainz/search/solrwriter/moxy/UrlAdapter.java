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
import org.musicbrainz.mmd2.Url;

import java.util.ArrayList;
import java.util.List;

public class UrlAdapter extends NotUnmarshallableXmlAdapter<UrlAdapter.AdaptedUrl, Url> {

    public static class AdaptedUrl extends Url {
        public List<RelationAdapter.AdaptedRelation> relations;
    }

    /**
     * Call when convert model to json, replaces url in model with adaptedUrl
     * which does not contain a list of RelationList, instead all relations in each existing
     * RelationList are merged into a list of relations. We do this because it is not possible to merge
     * a List of RelationLists into the url using oxml.xml mapping
     */
    @Override
    public AdaptedUrl marshal(Url url) throws Exception {
        AdaptedUrl adaptedUrl = new AdaptedUrl();
        adaptedUrl.relations = RelationListUtil.marshal(url.getRelationList());

        //Also need to copy any other elements/attributes we may want to output
        adaptedUrl.setResource(url.getResource());
        adaptedUrl.setId(url.getId());
        adaptedUrl.setScore(url.getScore());

        return adaptedUrl;
    }
}
