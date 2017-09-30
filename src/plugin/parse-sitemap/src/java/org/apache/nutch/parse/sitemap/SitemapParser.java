package org.apache.nutch.parse.sitemap;

import crawlercommons.sitemaps.*;
import org.apache.hadoop.conf.Configuration;
import org.apache.nutch.parse.*;
import org.apache.nutch.protocol.Content;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;

public class SitemapParser implements Parser {

    private Configuration conf;

    @Override
    public ParseResult getParse(Content content) {
        SiteMapParser parser = new SiteMapParser(false);

        AbstractSiteMap siteMap;
        String contentType = content.getContentType();
        try {
            siteMap = parser
                    .parseSiteMap(contentType, content.getContent(), new URL(content.getUrl()));
        } catch (UnknownFormatException | IOException e) {
            return new ParseStatus(e).getEmptyParseResult(content.getUrl(), getConf());
        }

        if (siteMap.isIndex()) {
            //todo implement nested sitemaps
//            Collection<AbstractSiteMap> links = ((SiteMapIndex) siteMap).getSitemaps();
//            Map<CharSequence, CharSequence> map = new HashMap<CharSequence, CharSequence>();
//            for (AbstractSiteMap siteMapIndex : links) {
//                page.getSitemaps().put(new Utf8(siteMapIndex.getUrl().toString()), new Utf8("parser"));
//            }
            return new ParseStatus(new UnsupportedOperationException("Cannot parse nested sitemaps")).getEmptyParseResult(content.getUrl(), getConf());
        } else {
            Collection<SiteMapURL> links = ((SiteMap) siteMap).getSiteMapUrls();
            ArrayList<Outlink> outlinks = new ArrayList<Outlink>();

            for (SiteMapURL sitemapUrl : links) {
                try {
                    outlinks.add(new Outlink(sitemapUrl.getUrl().toString(), "deneme"));
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }

            ParseData parseData = new ParseData(ParseStatus.STATUS_SUCCESS, "Sitemap", outlinks.toArray(new Outlink[outlinks.size()]),
                    content.getMetadata());
            return ParseResult.createParseResult(content.getUrl(), new ParseImpl(new String(content.getContent()), parseData));
        }
    }


    public void setConf(Configuration conf) {
        this.conf = conf;
    }

    @Override
    public Configuration getConf() {
        return conf;
    }
}