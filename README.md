# COSC431 - Assignment 1

In this assignment we were tasked to create a search engine to process a 500mb XML file's documents, with the restraint that we should be able to search many terms and retrieve all relevant documents in at/under a second. I am hitting ~500ms, for the most part.

## Building

As the search suite is written in Java, building each program in the application is simple.

1. In the directory you wish to run the applications in, place the DocSearch folder in this directory.
2. Run: ```javac DocSearch/*.java```
3. Profit!

## Running

### Parser

The parser application (DocSearch/DocParser) prints to standard output the content of documents separated by a blank line.
On it's own, this application serves little purpose, and is designed to be used with the indexer. It does make for a pretty waterfall of text, though.

### Indexer

The indexer (DocSearch/DocIndex) is built to take document content from the parser application. To create a fresh index:
1. Ensure the directory "index" already exists in the folder above the DocSearch package. If it does not, create it with ```mkdir index```

2. Execute ```java DocSearch/DocParser | java DocSearch/DocIndex```
3. This process takes approximately 40 minutes to complete. Text is printed during this process, such as "Completed index: XXXXms" when the application has finished.
 

### Search

To search upon the index, the DocSearch/DocSearch application will accept queries from stdin. As an example, with a text document containing a list of terms:
1. Ensure the index exists, or create a new one where necessary
2. Execute ```java DocSearch/DocSearch < text.txt```
3. Terms within text.txt will be searched upon the document, with wsj.xml style document numbers returned, as well as a relevance score.

## Index 

A pre-built index folder for the wsj.xml example file is located in my user directory, at `heija959/index/`

This does not apply to the general public, only staff marking this assignment. This index exists as of Thursday, 14th April and may not exist past 2022.

## Decisions/Justifications

Ranking is weak and the numbers are represented uncomfortably as floats, but the information they represent is still a relevance score. I might change this. 
 
I have decided to hash words into respective sub-indexes, and reading index portions by hashing the terms. I understand this is "hacking" and constitutes a circumvention of ideas expressed in lectures... 

...but is it a circumvention? The mechanism of using a dictionary to store the location of in an index is the same as knowing the location of such a term via hash, but in a blurrier ways. 

I did try this seeking technique, but keeping a dictionary and loading a dictionary became an issue in of itself, breaching the 1 second rule through the loading of the dictionary.

It's true I could hard-code this dictionary into the application itself, but that then becomes poor programming if the index were to change. 

The hashing method has drawbacks: If a term doesn't exist in any document, the subindex will still be loaded-- but I don't need to load a dictionary.