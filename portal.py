# -*- coding: utf-8 -*-
import os
import scrapy

class PortalSpider(scrapy.Spider):
    name = "portalbitcoin"

    def start_requests(self):
        url = ''
        tag = getattr(self, 'url', None)
        if tag is not None:
            url = tag
        yield scrapy.Request(url, self.parse)

    def parse(self, response):
        title = response.css('article.post header.td-post-title h1.entry-title ::text').extract_first()
        s=""
        for entry in response.css('article.post div.td-post-content'):
            s=""
            #print(entry)
            #for paragraph in entry.css('div.td-post-content p'):
            for paragraph in entry.css('p'):# | //div/p/span'):
                for x in (paragraph.css('::text').extract()):
                    s=s+ " " + x
                
        toFile(s , title)
        # print (s)

def toFile(text,id):
    filename="/Users/ruirua/Documents/PhD_Classes/AIE/work/noticias/portalbitcoin/" + id + ".txt"
    if os.path.exists(filename):
        return 
    else:
        append_write = 'w' # make a new file if not
    f = open(filename,append_write)
    f.write( text.encode('utf-8')+ "\n")
    f.close()