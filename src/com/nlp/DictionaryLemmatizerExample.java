package com.nlp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import opennlp.tools.lemmatizer.DictionaryLemmatizer;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;

/**
 * Dictionary Lemmatizer Example in Apache OpenNLP
 */
public class DictionaryLemmatizerExample {

	public static void main(String[] args) {
		try {
			// test sentence
			String[] tokens = new String[] { "Most", "large", "cities", "in", "the", "US", "had", "morning", "and",
					"afternoon", "newspapers", "." };

			// Parts-Of-Speech Tagging
			// reading parts-of-speech model to a stream
			InputStream posModelIn = new FileInputStream("models" + File.separator + "en-pos-maxent.bin");
			// loading the parts-of-speech model from stream
			POSModel posModel = new POSModel(posModelIn);
			// initializing the parts-of-speech tagger with model
			POSTaggerME posTagger = new POSTaggerME(posModel);
			// Tagger tagging the tokens
			String tags[] = posTagger.tag(tokens);

			// loading the dictionary to input stream
			InputStream dictLemmatizer = new FileInputStream("dictionary" + File.separator + "en-lemmatizer.txt");
			// loading the lemmatizer with dictionary
			DictionaryLemmatizer lemmatizer = new DictionaryLemmatizer(dictLemmatizer);

			// finding the lemmas
			String[] lemmas = lemmatizer.lemmatize(tokens, tags);

			// printing the results
			System.out.println("\nPrinting lemmas for the given sentence...");
			System.out.println("WORD -POSTAG : LEMMA");
			for (int i = 0; i < tokens.length; i++) {
				System.out.println(tokens[i] + " -" + tags[i] + " : " + lemmas[i]);
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}