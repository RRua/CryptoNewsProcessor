import wikipedia
import sys
import difflib
from collections import Counter
import re
import random

def returnMostCommon(wordmatch,wordlist):
	test1 = wordmatch.encode('utf-8')
	dic = {}
	test1=test1.lower()
	for wd in wordlist:
		wd=wd.lower().encode('utf-8')
		common = {}
		tt_count =0
		if len(test1) < len(wd):
		     for letter in test1:
		         if letter in wd:
		             common[letter] = len(re.findall(letter, wd))
		else:
		     for letter in wd:
		         if letter in test1:
		             common[letter] = len(re.findall(letter, test1))
		for word, count in common.items():
		     tt_count=tt_count+1
		dic[wd]=tt_count
	return max(dic, key=dic.get)


def createWikiID(search_object):
	if " " in search_object:
		return search_object.replace(" ","_")
	return search_object


def search_Wikipedia(original_object,search_object):
	wikiID=createWikiID(search_object)
	wikipedia.set_lang('pt')
	try:
		page = wikipedia.page(wikiID)
	except Exception as e:
		#it is not like that
		l = wikipedia.search(original_object)
		x=difflib.get_close_matches(original_object,l)
		if not x:
			#didnt retrieved nothing, unlikely
			return ""
		else:
			if len(x)>1:
				try:
					page=wikipedia.page(returnMostCommon(original_object,x))
				except wikipedia.DisambiguationError as e:
					s = random.choice(e.options)
					search_Wikipedia(original_object,s)
			else:
				try:
					page = wikipedia.page(x[0])
				except wikipedia.DisambiguationError as e:
					s = random.choice(e.options)
					search_Wikipedia(original_object,s)
	return page.content.encode('utf-8')
		
	
	

def firstParagraphToFile(text,id):
	lines = text.splitlines()
	wikis=open(("/Users/ruirua/Documents/PhD_Classes/AIE/work/wikipediaExtracted/" + id + ".txt").encode('utf-8'), "w")
	if len(lines)>0:
		wikis.write(lines[0] + "\n")
	wikis.close()



def main(args):
	args[0]=args[0].replace("\"","")
  	if "_" in args[0]:
	  	ret = search_Wikipedia(args[0],args[0])
	  	firstParagraphToFile(ret, args[0])
  	else:
	  	idd = createWikiID(args[0])
	  	ret = search_Wikipedia(args[0],idd)
	  	firstParagraphToFile(ret, args[0])
 
if __name__ == "__main__":
   main(sys.argv[1:])
   #lines = open(sys.argv[1]).read().splitlines()
   #print(lines[0].decode('utf-8'))