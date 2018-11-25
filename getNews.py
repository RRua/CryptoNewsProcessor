import sys
import json
import os
from pprint import pprint
from subprocess import call, check_output, Popen, PIPE


def extractNews(newsFile):
    with open(newsFile) as f:
        jos = json.load(f)
    for jo in jos:
        print("url " + jo['news_url'])
        #call(["scrapy", "runspider", "portal.py", "-a", jo['news_url'] , "-s", "LOG_ENABLED=False"])
        cmd ='scrapy runspider portal.py -a url=' +  jo['news_url']  + ' -s LOG_ENABLED=False'
        pipes = Popen(cmd, shell=True, stdout=PIPE, stderr=PIPE)
        std_out, std_err = pipes.communicate()
        if pipes.returncode != 0:
            # an error happened!
            err_msg = "{}. Code: {}".format(std_err.decode("UTF-8"), pipes.returncode)
            print(err_msg)
        else:
            print("ok")
            


def getNews(url):
    cmd ='scrapy runspider newsCrawler.py -a url=' +url  + ' -s LOG_ENABLED=False -o news.json'
    pipes = Popen(cmd, shell=True, stdout=PIPE, stderr=PIPE)
    std_out, std_err = pipes.communicate()
    if pipes.returncode != 0:
        # an error happened!
        err_msg = "{}. Code: {}".format(std_err.decode("UTF-8"), pipes.returncode)
        print(err_msg)
    else:
        print("o k")



def main(args):
    filename='news.json'
    if os.path.exists(filename):
        os.remove(filename)
    getNews("https://portaldobitcoin.com/noticias/")
    extractNews(filename)
    os.remove(filename)
    getNews("https://www.criptomoedasfacil.com/noticias/")
    extractNews(filename)

if __name__ == "__main__":
   main(sys.argv[1:])