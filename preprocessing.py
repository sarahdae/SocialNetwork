import nltk
from nltk.stem.wordnet import WordNetLemmatizer
from nltk.corpus import wordnet
import os
import re
import time
import sys


def chapter_seperator(filename):
    '''

    :param filename:
    :return: chapter_list: list of chapters as strings
    '''

    my_file = open(filename, "r")
    big_list = []

    line_list = my_file.readlines()
    my_file.close()
    my_file = open(filename, "r")
    text = my_file.read()

    chapter = ""
    chapter_list = []
    instances_of_chapter_header = re.findall("CHAPTER [A-Za-z1-9]+.*\n", text)
    indices = []
    for i in instances_of_chapter_header:
        indices.append(line_list.index(i))

    def glue_string_list(list_of_string):
        glued = ""
        for i in list_of_string:
            glued += i
        return glued

    cur_list = []
    for i in range(len(indices)):
        if i == len(indices) - 1:
            cur_list = line_list[indices[i] + 1:]
            chapter = glue_string_list(cur_list)
            chapter_list.append(chapter)

        else:
            cur_list = line_list[indices[i] + 1:indices[i + 1]]
            chapter = glue_string_list(cur_list)
            chapter_list.append(chapter)

    return chapter_list


def sentence_tokenizer(chapter):
    '''
    a method which takes a chapter as a string and splits it
    :argument chapter: a string
    :return list_sentences: a list of sentences
    '''
    # sentence tokenizer
    list_sentences = nltk.sent_tokenize(chapter)
    return list_sentences


def word_tokenizer(sentence):
    '''
    A method to tokenize a sentence
    :argument sentence: a string
    :return token_list: a list of all tokens
    '''
    token_list = nltk.word_tokenize(sentence)
    return token_list


def tagger(token_list):
    '''
    A method which takes a list of words and tags them
    :argument token_list: list of strings
    :return list_tagged: a list with all sentences tagged with pos tags
    '''
    tagged_list = nltk.pos_tag(token_list)
    return tagged_list


def translatePOStoWNPOS(pos):
    '''
    translates treebank pos into wordnet word type
    :param pos: POS-tag as string
    :return: a wordnet word type
    '''

    if pos[:1] == ("J"):
        return wordnet.ADJ
    elif pos[:1] == ("V"):
        return wordnet.VERB
    elif pos[:1] == ("N"):
        return wordnet.NOUN
    elif pos[:1] == ("R"):
        return wordnet.ADV
    else:
        return ""


def my_lemmatize(lemmatizer, token, POS):
    '''

    :param lemmatizer:
    :param token:
    :param POS:
    :return: a lemma
    '''
    WNPOS = translatePOStoWNPOS(POS)
    if WNPOS == "":
        return lemmatizer.lemmatize(token)
    else:
        return lemmatizer.lemmatize(token, WNPOS)


def preprocess(input_file_name, output_file_name):
    '''
    writes everything to a .csv
    :param input_file_name:
    :param output_file_name:

    '''

    output_file = open(output_file_name + ".tsv", "a")
    output_file.write("#ChapterID\tSentenceID\tTokenID\tToken\tLemma\tPOS\n")

    chapters = chapter_seperator(input_file_name)

    print("---Creating new output file---")
    for i, chapter in enumerate(chapters):
        sentences = sentence_tokenizer(chapter)
        for j, sentence in enumerate(sentences):
            token_list = word_tokenizer(sentence)
            # lemmatize
            tagged_token_list = tagger(token_list)
            for k in range(len(token_list)):
                lemmatizer = WordNetLemmatizer()  # init Lemmatizer
                current_token = token_list[k]
                current_POS = tagged_token_list[k][1]
                current_lemma = my_lemmatize(lemmatizer, current_token, current_POS)

                output_file.write(str(i + 1) + "\t")  # writing chapter ID to opfile
                output_file.write(str(j + 1) + "\t")  # writing sentence ID to opfile
                output_file.write(str(k + 1) + "\t")  # writing token ID to opfile
                output_file.write(current_token + "\t")  # writing token to opfile
                output_file.write(current_lemma + "\t")
                output_file.write(current_POS + "\t\n")  # writing POS to opfile

    print("---Output file created---")
    print("\nEND OF SCRIPT")


if __name__ == "__main__":

    my_input_file = input("Enter name of input file with suffix: e.g.: text.txt\n")
    while not os.path.isfile(my_input_file):
        print(my_input_file + " doesn't exist")
        my_input_file = input("Enter name of input file with suffix: e.g.: text.txt\n")
    my_output_file = input("Enter output file WITHOUT suffix: e.g.: output\n")
    #my_output_file = "output"

    if os.path.isfile(my_output_file + ".tsv"):
        message = "CAUTION: " + my_output_file + ".tsv already exists\nIf you go on, the old file will be overwritten!!!\nDo you want to go on? (y/n)\n"
        user_in = input(message)
        if user_in == "y" or user_in == "Y":

            os.remove(my_output_file + ".tsv")
            print("---Deleting old output---")
            time.sleep(2)

        else:
            sys.exit("re-run program, please!")

    preprocess(my_input_file, my_output_file)





