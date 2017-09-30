package org.apache.nutch.parse.html;

import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.Text;
import org.apache.nutch.crawl.CrawlDatum;
import org.apache.nutch.crawl.Inlinks;
import org.apache.nutch.indexer.IndexingException;
import org.apache.nutch.indexer.IndexingFilter;
import org.apache.nutch.indexer.NutchDocument;
import org.apache.nutch.metadata.Metadata;
import org.apache.nutch.parse.Parse;


public class GetLinksWithOffsetToData implements IndexingFilter { //todo move to another module

    private Configuration conf;

    @Override
    public NutchDocument filter(NutchDocument doc, Parse parse, Text url, CrawlDatum datum, Inlinks inlinks) throws IndexingException {
        String links = parse.getData().getParseMeta().get(Metadata.LINKS_WITH_OFFSET_ON_PAGE);

        if (StringUtils.isNotEmpty(links)) {
            doc.add("linksWithOffset", links);
        }

        return doc;
    }


    public Configuration getConf() {
        return conf;
    }

    public void setConf(Configuration conf) {
        this.conf = conf;
    }
}