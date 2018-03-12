package com.nlp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

import opennlp.tools.namefind.BioCodec;
import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.NameSampleDataStream;
import opennlp.tools.namefind.TokenNameFinder;
import opennlp.tools.namefind.TokenNameFinderFactory;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.util.InputStreamFactory;
import opennlp.tools.util.MarkableFileInputStreamFactory;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.Span;
import opennlp.tools.util.TrainingParameters;

/**
 * NER Training in OpenNLP with Name Finder Training Java Example
 * 
 */
public class NERTrainingExample {

	public static void main(String[] args) {

		// reading training data
		InputStreamFactory in = null;
		try {
			// in = new MarkableFileInputStreamFactory(new File("AnnotatedSentences.txt"));
			in = new MarkableFileInputStreamFactory(new File("Ocean Bill of Lading.train"));

		} catch (FileNotFoundException e2) {
			e2.printStackTrace();
		}

		ObjectStream sampleStream = null;
		try {
			sampleStream = new NameSampleDataStream(new PlainTextByLineStream(in, StandardCharsets.UTF_8));
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		// setting the parameters for training
		TrainingParameters params = new TrainingParameters();
		params.put(TrainingParameters.ITERATIONS_PARAM, 70);
		params.put(TrainingParameters.CUTOFF_PARAM, 1);

		// training the model using TokenNameFinderModel class
		TokenNameFinderModel nameFinderModel = null;
		try {
			nameFinderModel = NameFinderME.train("en", null, sampleStream, params,
					TokenNameFinderFactory.create(null, null, Collections.emptyMap(), new BioCodec()));
		} catch (IOException e) {
			e.printStackTrace();
		}

		// saving the model to "ner-custom-model.bin" file
		try {
			File output = new File("ner-custom-model.bin");
			FileOutputStream outputStream = new FileOutputStream(output);
			nameFinderModel.serialize(outputStream);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// testing the model and printing the types it found in the input sentence
		TokenNameFinder nameFinder = new NameFinderME(nameFinderModel);

		String[] testSentence = { "Alisa", "Fernandes", "is", "a", "tourist", "from", "Spain" };

		// Sunil
		String document = "Ocean Bill of Lading  Exporter Booking Number Document Number  WING LLC LN-589654 WEL-5689  Export References  NY  Ultimate Consignee Forwarding Agent  Royal Foodie WING LLC  «m—  Pre-Carriage By Place of Receipt Domestic Routing  ACE LLC New York, New York, USA NA  Exporting Carrier Port of Loading Loading Pier/Tenninal  Red Hook Terminal Red Hook Terminal  Port of Discharge Place of Receipt on Carrier Type of Move  Old Port Of Montreal Ocean  Marks and Numbers No. of ex:   Biscuits - 85 500000 A Local made biscuits which are made out of wheat, milk and sugar with special ingredients 250000 10*4*7 cms  consumable by people of any ages above 3 years safely.  attnttttt. tnene  pages, tnnutng attatnnents to tnn Ocean at ot tattng.  These commodities, technology or software were exported from the United States in accordance with the Export Administration Regulations. Diversion contrary to US. law prohibited.  Carrier has a policy against payment solicitation. or receipt of any rebate, directly or indirectly. which would be unlawful under the United States Shipping Act, 1984 as amended.  .1,    FREIGHT RATES, CHARGES, WEIGHTS ANDIOR MEASUREMENTS Received by Carrier for shipment by ocean vessel between port of loading and port of discharge,  and for arrangement or procurement of pre-carriage from place of receipt and on-carriage to  place 0f delivery. where stated above, the goods as specified above in apparent good order and    Subject to correction condition unless otherwise stated. The goods to be delivered at the above mentioned port of  — discharge or place of delivery. whichever is applicable.  Grand T otalr  IN WITNESS WHEREOF original Bills of Lading have been signed, not otherwise  stated above, one of which being accomplished the others  anal! be void.  DATED AT 1010520] 0 RICHARD AL  BY —  Agent  B/L NO: JY123897    Sunil Soni  ";
		testSentence = document.split(" ");

		System.out.println("Finding types in the test sentence..");
		Span[] names = nameFinder.find(testSentence);
		System.out.println("names length: " + names.length);

		for (Span name : names) {
			String personName = "";
			for (int i = name.getStart(); i < name.getEnd(); i++) {
				personName += testSentence[i] + " ";
			}
			System.out.println(name.getType() + " : " + personName + "\t [probability=" + name.getProb() + "]");
		}
	}

}
