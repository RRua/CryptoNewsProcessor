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
        date = response.css('time.entry-date::attr(datetime)').extract_first().split('T')[0]
        for entry in response.css('article.post div.td-post-content'):
            s=""
            #print(entry)
            #for paragraph in entry.css('div.td-post-content p'):
            for paragraph in entry.css('p'):# | //div/p/span'):
                for x in (paragraph.css('::text').extract()):
                    if x.isupper():
                        x=x.lower()
                    s=s+ " " + x + "\n"
                
        toFile(s , title,"#"+date)
        # print (s)

def toFile(text,id, data):
    filename="/Users/ruirua/Documents/PhD_Classes/AIE/work/noticias/" + str(hash(id)) + data+ ".txt"
    if os.path.exists(filename):
        return 
    else:
        append_write = 'w' # make a new file if not
    f = open(filename,append_write)
    f.write( text.encode('utf-8')+ "\n")
    f.close()
