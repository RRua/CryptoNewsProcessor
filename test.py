import re


def returnMostCommon(wordmatch,wordlist)
	test1 = wordmatch
	dic = {}
	test1=test1.lower()
	for wd in wordlist:
		wd=wd.lower()
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

	print(dic)
	print("max ->" + max(dic, key=dic.get))