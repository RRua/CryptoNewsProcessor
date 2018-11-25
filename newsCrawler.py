import  scrapy
import sys

class MySpider(scrapy.Spider):
    name = 'cripto'
    #allowed_domains = ['portaldobitcoin.com']
    
    def start_requests(self):
        url = ''
        tag = getattr(self, 'url', None)
        if tag is not None:
            url = tag
        yield scrapy.Request(url, self.parse)

    def parse(self, response):
        for entry in response.css('div.td_module_wrap'):
            yield {
                'news_url': entry.css('div.td-module-thumb a::attr(href)').extract_first(),
                'news_date': entry.css('span.td-post-date time.entry-date::attr(datetime)').extract_first(),
                #'news_title': entry.css('div.item-details h3.entry-title a::attr(title)').extract_first(),
            }