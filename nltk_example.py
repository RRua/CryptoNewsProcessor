import nltk
import nltk.data
#nltk.download('words')
import nltk
import pickle
import sys


def newsTokenizer(news):
	print("tokenizing " + news)
	try:
		file =open( news.decode('latin1') , 'r')
	except IOError as e:
		file = open( news.decode('latin1') , 'w')
	
	text= file.read().decode('utf-8')
	#print(text)
	sdetect = nltk.data.load('tokenizers/punkt/portuguese.pickle')
	sentences = '\n\n'.join(sdetect.tokenize(text.strip()))
	portuguese_sent_tokenizer = nltk.data.load("tokenizers/punkt/portuguese.pickle")
	sentences = portuguese_sent_tokenizer.tokenize(text)
	words=open("/Users/ruirua/Documents/PhD_Classes/AIE/work/words.txt", "w")
	x=0
	y=0
	for sentence in sentences:
		xx=nltk.word_tokenize(sentence)
		for word in xx:
			if "." in word:
				words.write(str(x) +" " + str(y) + " " +word.replace('.','').encode('utf-8') + "\n")
				y=y+1
				words.write(str(x) +" " + str(y) + " " +".".encode('utf-8') + "\n")
			else:
				words.write(str(x) +" " + str(y) + " " +word.encode('utf-8') + "\n")
				#print(word)
			y=y+1
		x=x+1
		y=0
	words.close()





def main(args):
  newsTokenizer(args[0])


if __name__ == "__main__":
   main(sys.argv[1:])