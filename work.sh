#!/bin/bash


NOTICIA=$1
LANGUAGE="pt"
POS_COMMAND="linguakit tagger -ner  $LANGUAGE $NOTICIA"
NER_COMMAND="linguakit tagger -ner  $LANGUAGE $NOTICIA"
REL_COMMAND="linguakit rel $LANGUAGE $NOTICIA"


TRIPLE_W_L_PoS= python  $($POS_COMMAND)


