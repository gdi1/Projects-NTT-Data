package com.example;

import java.io.*;
import java.nio.file.*;
import java.text.BreakIterator;
import java.util.*;
import java.util.stream.Collectors;

public class Translator {

    /**
     * 
     * @param pathToDictionary      path to the txt file containing all the words
     *                              from the dictionary
     * @param pathToFileToTranslate path to the file that needs to be translated
     * @return returns the translated version of the entire file
     * @throws IOException
     */
    public static String translateFile(String pathToDictionary, String pathToFileToTranslate) throws IOException {

        String text = Files.readAllLines(Paths.get(pathToFileToTranslate)).stream().reduce((a, b) -> a + b).get();
        BreakIterator iterator = BreakIterator.getSentenceInstance();
        iterator.setText(text);
        List<String> sentences = new ArrayList<>();

        int start = iterator.first();
        int end = iterator.next();

        /**
         * splitting the file into multiple sentences
         */
        while (end != BreakIterator.DONE) {

            int newEnd = iterator.next();
            if (newEnd == BreakIterator.DONE) {

                sentences.add(text.substring(start, end).trim());
                end = newEnd;

            } else {

                sentences.add(text.substring(start, end - 1).trim());
                start = end;
                end = newEnd;
            }
        }

        /**
         * converting each sentence and joining them into a single string which is then
         * returned
         */
        String textConverted = sentences.parallelStream().map(sentence -> translateSentence(pathToDictionary, sentence))
                .collect(Collectors.joining(" "));

        return textConverted;
    }

    /**
     * 
     * @param pathToDictionary path to the txt file containing all the words from
     *                         the dictionary
     * @param sentence         the sentence that needs to be translated
     * @return returns the translated version of the sentence
     */
    public static String translateSentence(String pathToDictionary, String sentence) {

        String result = "";
        String punctuation = sentence.substring(sentence.length() - 1);

        /**
         * reading all the words from the dictionary
         */
        List<Word> wordsDictionary = readWordsFromDictionary(pathToDictionary);
        String[] sentenceSplit = sentence.split("[,.;!? ]+");
        List<Integer> nounsAlreadyConverted = new ArrayList<>();

        for (int i = 0; i < sentenceSplit.length; i++) {

            /**
             * the following lines of code deal with situations in which in English the
             * adjectives normally sit in front of the noun that they determine, however in
             * Romanian usually they are situated right after the noun
             */
            if (Word.isPartOfSpeech(wordsDictionary, sentenceSplit[i]) != null
                    && Word.isPartOfSpeech(wordsDictionary, sentenceSplit[i]).get(0).equals("adj")
                    && Word.checkAdjectiveDeterminesNoun(wordsDictionary, i, sentenceSplit) != -1) {

                if (!nounsAlreadyConverted
                        .contains(Word.checkAdjectiveDeterminesNoun(wordsDictionary, i, sentenceSplit))) {

                    nounsAlreadyConverted.add(Word.checkAdjectiveDeterminesNoun(wordsDictionary, i, sentenceSplit));

                    result += " " + wordConvert(Word.checkAdjectiveDeterminesNoun(wordsDictionary, i, sentenceSplit),
                            sentenceSplit, wordsDictionary);
                }

            }

            /**
             * here we firstly translate the noun and then we will add the adjectives
             * afterwards
             */
            if (!nounsAlreadyConverted.contains(i)) {

                result += " " + wordConvert(i, sentenceSplit, wordsDictionary);
            }
        }
        result = result.trim();

        return (result.substring(0, 1).toUpperCase() + result.substring(1) + punctuation);
    }

    /**
     * 
     * @param indexWordToConvert the position of the word that needs to be converted
     *                           inside the sentence
     * @param sentenceSplit      the sentence splitted into words
     * @param wordsDictionary    the list of words from the dictionary
     * @return returns the correct conversion of the desired word from the sentence
     */
    public static String wordConvert(int indexWordToConvert, String[] sentenceSplit, List<Word> wordsDictionary) {

        if (Word.isPartOfSpeech(wordsDictionary, sentenceSplit[indexWordToConvert]) != null) {

            if (Word.isPartOfSpeech(wordsDictionary, sentenceSplit[indexWordToConvert]).get(0).equals("n")) {

                return Word.convertNoun(wordsDictionary, indexWordToConvert, sentenceSplit);

            } else if (Word.isPartOfSpeech(wordsDictionary, sentenceSplit[indexWordToConvert]).get(0).equals("v")) {

                return Word.convertVerb(wordsDictionary, indexWordToConvert, sentenceSplit);

            } else if (Word.isPartOfSpeech(wordsDictionary, sentenceSplit[indexWordToConvert]).get(0).equals("adj")) {

                return Word.convertAdjective(wordsDictionary, indexWordToConvert, sentenceSplit);

            } else if (Word.isPartOfSpeech(wordsDictionary, sentenceSplit[indexWordToConvert]).get(0).equals("num")) {

                return Word.convertNumeral(wordsDictionary, indexWordToConvert, sentenceSplit);

            } else if (Word.isPartOfSpeech(wordsDictionary, sentenceSplit[indexWordToConvert]).get(0).equals("pp")) {

                return Word.convertPersonalPronoun(wordsDictionary, indexWordToConvert, sentenceSplit);

            } else if (Word.isPartOfSpeech(wordsDictionary, sentenceSplit[indexWordToConvert]).get(0).equals("adv")
                    || Word.isPartOfSpeech(wordsDictionary, sentenceSplit[indexWordToConvert]).get(0).equals("prep")
                    || Word.isPartOfSpeech(wordsDictionary, sentenceSplit[indexWordToConvert]).get(0).equals("conj")) {

                return Word.convertSimpleWord(wordsDictionary, sentenceSplit[indexWordToConvert]);
            }

        }

        /**
         * if the word is not in the dictionary the method will just return the word in
         * english (I observed that this is how Google Translate behaves in situations
         * like these)
         */
        return sentenceSplit[indexWordToConvert];
    }

    /**
     * Reads all the words from the txt file and constructs corresponding Word
     * objects
     * 
     * @param pathToDictionary path to the dictionary
     * @return returns the list of Word objects newly created
     */
    public static List<Word> readWordsFromDictionary(String pathToDictionary) {
        try {

            List<String> dictionary = Files.readAllLines(Paths.get(pathToDictionary));
            List<Word> words = new ArrayList<>();

            words = dictionary.stream().map(line -> new Word(line.split(","))).collect(Collectors.toList());
            return words;

        } catch (Exception e) {

            e.printStackTrace();
            return null;
        }
    }
}