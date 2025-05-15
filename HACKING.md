The Debugger's Guide to MusicBrainz Solr
========================================

This file gives tips to debug the Solr-based MusicBrainz indexed search.

Table of contents
-----------------

<!-- toc -->

- [Setup](#setup)
- [Solr logs](#solr-logs)
- [Solr Admin](#solr-admin)
  * [Query screen](#query-screen)
    + [Parameters priority](#parameters-priority)

<!-- tocstop -->

Setup
-----

Set a development setup with [`musicbrainz-docker`](https://github.com/metabrainz/musicbrainz-docker).

This can be used to run any released or development versions of:
* MusicBrainz Server (MBS -
[repository](https://github.com/metabrainz/musicbrainz-server/),
[search-related tickets](https://tickets.metabrainz.org/issues/?jql=project%20%3D%20MBS%20AND%20component%20%3D%20Search%20AND%20status%20!%3D%20Closed)
),
* Search Index Rebuilder (SIR -
[repository](https://github.com/metabrainz/sir/),
[tickets](https://tickets.metabrainz.org/issues/?jql=project%20%3D%20SEARCH%20AND%20component%20%3D%20Indexer%20AND%20status%20!%3D%20Closed)
),
* MusicBrainz Solr search server (MB Solr -
[repository](https://github.com/metabrainz/mb-solr/),
[tickets](https://tickets.metabrainz.org/issues/?jql=project%20%3D%20SEARCH%20AND%20component%20%3D%20%22Response%20Writer%22%20AND%20status%20!%3D%20Closed)
)
which comes with a Solr schema (MBSSSSS -
[repository](https://github.com/metabrainz/mbsssss/),
[tickets](https://tickets.metabrainz.org/issues/?jql=project%20%3D%20SEARCH%20AND%20component%20%3D%20Schema%20AND%20status%20!%3D%20Closed)
).

These versions of MBS, SIR and MB Solr should
(preferably for development, necessarily for production)
conform to the same version of MusicBrainz Relax NG schema (MMD -
[repository](https://github.com/metabrainz/mmd-schema)
) as they may just not work together otherwise.

See also [MusicBrainz search architecture](https://musicbrainz.org/doc/Development/Search_Architecture) for a broader overview.

Solr logs
---------

Solr logs are useful to debug making a search query with MBS and/or indexing documents with SIR.

In a terminal, follow Solr logs using:

```bash
docker compose logs -f --tail 1 search
```

For an example, browse your local MusicBrainz Server instance at
<http://localhost:5000/search?query=%28Downward+Spiral%29+AND+format%3ACD&type=release&limit=25&method=advanced>.
Then the request appears as follows in Solr logs.
```
[   x:release] o.a.s.c.S.Request [release]  webapp=/solr path=/advanced params={q=(Downward+Spiral)+AND+format:CD&start=0&rows=25&wt=mbjson}
```

These values can then be used for debugging in the Solr Admin query screen; See the next section.

Solr Admin
----------

To debug how other components (MBS, SIR) interact with the search server,
the Solr Admin web interface (browsable from <http://localhost:8983>) is your friend.

See “[Getting Started / Solr Admin UI](https://solr.apache.org/guide/solr/9_7/getting-started/solr-admin-ui.html)"
in the Apache Solr Reference Guide 9.7 for more information.

### Query screen

The query screen allows to reproduce and tweak search queries made through MBS.

To follow the same example as in the above section “[Solr logs](#solr-logs)”,
switch the Core Selector to “`release`” and click on “Query” to get to
<http://localhost:8983/solr/#/release/query>.

Then start filling the form as follows:
* Set the first field “Request-Handler (qt)”
  to the value for `path` in the log record, with a leading slash:
  `/advanced`
* Set the second field “q” (query string)
  to the text you typed for the search
  (that is the unescaped value for “q” in the log record):
  `(Downward Spiral) AND format:CD`
* Select `xml` in the field “wt” (writer type) for the most readable presentation.

From here, different combinations can be chosen depending on your needs:

* Check `debugQuery` to have a detailed score computation (using search fields and boosts) for each result.

* Set “fl” (field list) to
  either `mbid,score` for the least detailed list of results
  or `*,score` for the most detailed list of results

* If you expect another item to show up in the search results but it doesn’t,
  you can ask Solr to return the score logging for that item
  and compare it with the scores of the search results,
  by setting “Raw Query Parameters” to:
  `explainOther=mbid:fe78827f-e402-42a0-ab63-a3fd11c12cc4`

Alternatively to get the same output format as in MB Search API,
unselect any value in the field “wt” (writer type) and
set “Raw Query Parameters” to either `wt=mbjson` or `wt=mbxml`.
These formats are incompatible with the “fl” field and the
`debugQuery` and `explainOther` options, so setting any of these
will not change the output.

See “[Query Screen](https://solr.apache.org/guide/solr/9_7/query-guide/query-screen.html)"
in Apache Solr Reference Guide 9.7 for more information.

#### Parameter priority

Some parameters are automatically set on all queries based on the core configuration.

Some parameters are set based on the request handler.
These are identified as "invariants", which means that the parameters will always be set to this value even if you specify a different value in the admin interface.
For example the [`basic` request handler](https://github.com/metabrainz/mbsssss/blob/v-2025-05-13/common/requestHandler-basic.xml) erases the `pf` field for good.

Some parameters for ranking are set with defaults and can be overridden.
These can be changed by copying them into the relevant fields in the admin query panel to see the resulting change in ranking order.
For example the [release’s request parameters](https://github.com/metabrainz/mbsssss/blob/v-2025-05-13/release/conf/request-params.xml)
has a default value for the `fl` field which can be overriden if needed
(while the `pf` value will still be erased if used from the `/basic` endpoint).

Finally, “Raw Query Parameters” has lesser priority than any other form fields.
