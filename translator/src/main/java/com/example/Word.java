package com.example;

import java.util.ArrayList;
import java.util.List;

public class Word {

    private String word;
    private List<String> partOfSpeech = new ArrayList<>();
    private List<String> conversion = new ArrayList<>();
    private String mORf; // m/f
    private String singularOrPlural;

    /**
     * 
     * @param noun
     * @param wordsDictionary
     * @return this method returns a number from 0 to 4 in order to properly return
     *         the translated version of the adjecive in which it should be
     *         conversed (the translation always occurs from English to Romanian)
     */
    public static int returnTypeAdjective(String noun, List<Word> wordsDictionary) {

        for (Word word : wordsDictionary) {
            if (word.word.equals(noun.toLowerCase())) {

                if (word.partOfSpeech.size() == 1 && word.partOfSpeech.get(0).equals("n")) {

                    if (word.mORf.equals("m") && word.singularOrPlural.equals("singular")) {
                        return 1;

                    } else if (word.mORf.equals("m") && word.singularOrPlural.equals("plural")) {
                        return 3;

                    } else if (word.mORf.equals("f") && word.singularOrPlural.equals("singular")) {
                        return 2;

                    } else if (word.mORf.equals("f") && word.singularOrPlural.equals("plural")) {
                        return 4;

                    } else {

                        return 0;
                    }

                } else {

                    return 0;
                }
            }
        }
        return 0;
    }

    /**
     * 
     * properly creating a new Word instance
     */
    public Word(String[] characteristics) {

        if (characteristics.length != 0) {
            this.word = characteristics[0];

            int i = 1;
            /**
             * verifies which part of speech it is
             */
            while (!characteristics[i].equals("n") && !characteristics[i].equals("v")
                    && !characteristics[i].equals("adj") && !characteristics[i].equals("adv")
                    && !characteristics[i].equals("pp") && !characteristics[i].equals("prep")
                    && !characteristics[i].equals("conj") && !characteristics[i].equals("num")) {

                this.conversion.add(characteristics[i]);
                i++;
            }
            this.partOfSpeech.add(characteristics[i]);

            /**
             * checks for multiple parts of speech associated with the same word
             */
            if ((i + 1) < characteristics.length && characteristics[i + 1].equals("adv")) {

                this.partOfSpeech.add(characteristics[i + 1]);
                i = i + 2;

                /**
                 * checks if it is a noun
                 */
            } else if (characteristics[i].equals("n")) {

                /**
                 * adds if it is masculin/feminin
                 */
                this.mORf = characteristics[i + 1];
                /**
                 * adds if it is singular or plural
                 */
                this.singularOrPlural = characteristics[i + 2];
            }
        }
    }

    @Override
    public String toString() {
        return "Word [conversion=" + conversion + ", mORf=" + mORf + ", partOfSpeech=" + partOfSpeech
                + ", singularOrPlural=" + singularOrPlural + ", word=" + word + "]";
    }

    /**
     * 
     * @param wordsDictionary
     * @param wordToCheck     the word whose part of speech needs to be discovered
     * @return a String which represents the part of speech of the desired word
     */
    public static List<String> isPartOfSpeech(List<Word> wordsDictionary, String wordToCheck) {

        for (Word word : wordsDictionary) {

            if (word.word.equals(wordToCheck.toLowerCase())) {
                return word.partOfSpeech;
            }
        }
        return null;
    }

    /**
     * for prepositions, conjunctions and adverbs which are not also adjectives
     */
    public static String convertSimpleWord(List<Word> wordsDictionary, String wordToCheck) {

        for (Word word : wordsDictionary) {

            if (word.word.equals(wordToCheck.toLowerCase())) {

                if (word.conversion.size() != 0) {
                    return word.conversion.get(0);
                } else {

                    return "";
                }
            }
        }
        return wordToCheck;
    }

    /**
     * 
     * @param wordsDictionary
     * @param indexWordToCheck
     * @param sentenceSplit
     * @return properly returns the translated version of the desired numeral
     */
    public static String convertNumeral(List<Word> wordsDictionary, int indexWordToCheck, String[] sentenceSplit) {

        for (Word word : wordsDictionary) {

            if (word.word.equals(sentenceSplit[indexWordToCheck])) {

                /**
                 * if the numeral is different from 'one' 'two' 'a' 'an' then the numeral's
                 * conversion has only one form and so it will return the only element inside
                 * the List
                 */
                if (!word.word.equals("one") && !word.word.equals("two") && !word.word.equals("a")
                        && !word.word.equals("an")) {

                    return word.conversion.get(0);
                } else {

                    /**
                     * otherwise, it searches for the noun that the numeral should determine
                     */
                    int i = indexWordToCheck + 1;

                    /**
                     * searches until it finds a noun
                     */
                    while (i < sentenceSplit.length
                            && !isPartOfSpeech(wordsDictionary, sentenceSplit[i]).get(0).equals("n")) {
                        i++;
                    }

                    /**
                     * if it finds a noun then it performs certain checks in order to return the
                     * right translated version of the numeral
                     */
                    if (i < sentenceSplit.length
                            && isPartOfSpeech(wordsDictionary, sentenceSplit[i]).get(0).equals("n")) {

                        for (Word word0 : wordsDictionary) {

                            if (word0.word.equals(sentenceSplit[i])) {

                                if (word0.mORf.equals("m")) {

                                    return word.conversion.get(0);

                                } else if (word0.mORf.equals("f")) {

                                    return word.conversion.get(1);
                                }
                            }
                        }
                        /**
                         * if the numeral is the last word within the sentence returns the right
                         * thanslated version of it
                         */
                    } else if (indexWordToCheck == (sentenceSplit.length - 1)) {

                        if (sentenceSplit[indexWordToCheck].equals("one")) {

                            return word.conversion.get(2);

                        } else {

                            return word.conversion.get(0);
                        }
                    }
                }
            }
        }
        return sentenceSplit[indexWordToCheck];
    }

    /**
     * 
     * This emthod returns the proper translated version of a personal pronoun
     */
    public static String convertPersonalPronoun(List<Word> wordsDictionary, int indexWordToCheck,
            String[] sentenceSplit) {

        for (Word word : wordsDictionary) {

            if (word.word.equals(sentenceSplit[indexWordToCheck].toLowerCase())) {

                /**
                 * performs additional checks in order to distinguish between the singular and
                 * plural version of the word 'you'
                 */
                if (word.word.toLowerCase().equals("you")) {

                    /**
                     * by improving this it should be able to properly deal with even more
                     * situations
                     */
                    if (indexWordToCheck > 1 && (sentenceSplit[indexWordToCheck - 1].equals("of")
                            && (sentenceSplit[indexWordToCheck - 2].toLowerCase().equals("both")
                                    || sentenceSplit[indexWordToCheck - 2].toLowerCase().equals("all")
                                    || sentenceSplit[indexWordToCheck - 2].toLowerCase().equals("most")
                                    || sentenceSplit[indexWordToCheck - 2].toLowerCase().equals("majority")))) {

                        return word.conversion.get(1);
                    } else {

                        return word.conversion.get(0);
                    }
                } else {

                    return word.conversion.get(0);
                }
            }
        }
        return sentenceSplit[indexWordToCheck];
    }

    /**
     * 
     * @param wordsDictionary
     * @param indexWordToCheck
     * @param sentenceSplit
     * @return the properly translated version of the desired noun
     */
    public static String convertNoun(List<Word> wordsDictionary, int indexWordToCheck, String[] sentenceSplit) {

        for (Word word : wordsDictionary) {

            if (word.word.equals(sentenceSplit[indexWordToCheck].toLowerCase())) {

                if (indexWordToCheck > 0) {
                    /**
                     * checks to see if there is a numeral or not that determines this noun
                     */
                    int i = indexWordToCheck - 1;
                    while (i >= 0 && (isPartOfSpeech(wordsDictionary, sentenceSplit[i]) == null
                            || isPartOfSpeech(wordsDictionary, sentenceSplit[i]).get(0).equals("adj")
                            || isPartOfSpeech(wordsDictionary, sentenceSplit[i]).get(0).equals("adv")
                            || sentenceSplit[i].equals("and") || sentenceSplit[i].equals("or"))) {

                        i--;
                    }
                    if (i >= 0 && isPartOfSpeech(wordsDictionary, sentenceSplit[i]).get(0).equals("num")) {

                        return word.conversion.get(0);
                    } else {

                        return word.conversion.get(1);
                    }
                } else {
                    return word.conversion.get(1);
                }
            }
        }
        return sentenceSplit[indexWordToCheck];
    }

    /**
     * 
     * @param wordsDictionary
     * @param indexWordToCheck
     * @param sentenceSplit
     * @return returns the properly translated version of the adjective
     */
    public static String convertAdjective(List<Word> wordsDictionary, int indexWordToCheck, String[] sentenceSplit) {

        for (Word word : wordsDictionary) {

            if (word.word.equals(sentenceSplit[indexWordToCheck])) {

                int j = indexWordToCheck + 1;
                /**
                 * checks to find a nun that this adjective might determine further down in the
                 * sentence
                 */
                while (j < sentenceSplit.length
                        && (isPartOfSpeech(wordsDictionary, sentenceSplit[j]).get(0).equals("adj")
                                || sentenceSplit[j].equals("and"))) {

                    j++;
                }
                /**
                 * if it finds a noun then it will make use of the returnTypeAdjective() method
                 * in order to return the right translated version
                 */
                if (j < sentenceSplit.length && isPartOfSpeech(wordsDictionary, sentenceSplit[j]).get(0).equals("n")) {

                    return word.conversion.get(returnTypeAdjective(sentenceSplit[j], wordsDictionary) - 1);
                } else {

                    /**
                     * Otherwise, it tries to find a personal pronoun that it might determine. The
                     * pronoun has to be located before the adjective inside the sentence
                     */
                    int i = indexWordToCheck - 1;
                    while (i >= 0 && (isPartOfSpeech(wordsDictionary, sentenceSplit[i]) == null
                            || !isPartOfSpeech(wordsDictionary, sentenceSplit[i]).get(0).equals("pp"))) {

                        i--;
                    }
                    /**
                     * if it finds a personal pronoun and the word sitting next to this one in the
                     * sentence is 'is' 'are' 'were' 'was' then depending on the personal pronoun it
                     * will return the right version of the adjective
                     */
                    if (i >= 0 && isPartOfSpeech(wordsDictionary, sentenceSplit[i]).get(0).equals("pp")
                            && (sentenceSplit[i + 1].equals("is") || sentenceSplit[i + 1].equals("are")
                                    || sentenceSplit[i + 1].equals("were") || sentenceSplit[i + 1].equals("was"))) {

                        if (sentenceSplit[i].toLowerCase().equals("he")) {

                            return word.conversion.get(0);

                        } else if (sentenceSplit[i].toLowerCase().equals("she")) {

                            return word.conversion.get(1);

                        } else if (sentenceSplit[i].toLowerCase().equals("you")
                                && convertPersonalPronoun(wordsDictionary, i, sentenceSplit).equals("tu")) {

                            return word.conversion.get(0);

                        } else if (sentenceSplit[i].toLowerCase().equals("it")) {

                            return word.conversion.get(0);

                        } else if (sentenceSplit[i].toLowerCase().equals("i")) {

                            return word.conversion.get(0);
                        } else {

                            return word.conversion.get(2);
                        }
                    } else {

                        /**
                         * otherwise, it searches for a noun situated before the adjective in the
                         * sentence that it might determine
                         */
                        int k = indexWordToCheck - 1;
                        while (k >= 0 && (isPartOfSpeech(wordsDictionary, sentenceSplit[k]) == null
                                || !isPartOfSpeech(wordsDictionary, sentenceSplit[k]).get(0).equals("n"))) {

                            k--;
                        }
                        /**
                         * if it finds a noun and the word following the it is 'is' 'are' 'was' 'were',
                         * then it will make use of the method returnTypeAdjective() in order to return
                         * the right translated version of the adjective
                         */
                        if (k >= 0 && isPartOfSpeech(wordsDictionary, sentenceSplit[k]).get(0).equals("n")
                                && (sentenceSplit[k + 1].equals("is") || sentenceSplit[k + 1].equals("are")
                                        || sentenceSplit[k + 1].equals("were") || sentenceSplit[k + 1].equals("was"))) {

                            return word.conversion.get(returnTypeAdjective(sentenceSplit[k], wordsDictionary) - 1);

                        } else {

                            /**
                             * this else statement is entered only if the desired adjective is also an
                             * adverb in which case it might not determine neither a personal pronoun nor a
                             * noun
                             */
                            return word.conversion.get(0);
                        }
                    }
                }
            }
        }
        return sentenceSplit[indexWordToCheck];
    }

    /**
     * 
     * @param wordsDictionary
     * @param indexWordToCheck
     * @param sentenceSplit
     * @return returns the properly translated version of the desired verb (the verb
     *         has to be at present tense for now)
     */
    public static String convertVerb(List<Word> wordsDictionary, int indexWordToCheck, String[] sentenceSplit) {

        for (Word word : wordsDictionary) {

            if (word.word.equals(sentenceSplit[indexWordToCheck].toLowerCase())) {

                int i = indexWordToCheck - 1;
                /**
                 * searching for a personal pronoun before the verb in order to be able to
                 * return the right translated version of the verb
                 */
                while (i >= 0 && (isPartOfSpeech(wordsDictionary, sentenceSplit[i]) == null
                        || !isPartOfSpeech(wordsDictionary, sentenceSplit[i]).get(0).equals("pp"))) {

                    i--;
                }
                /**
                 * depending on which personal pronoun it might be the program will return a
                 * different version of the translated verb
                 */
                if (i >= 0 && isPartOfSpeech(wordsDictionary, sentenceSplit[i]).get(0).equals("pp")) {

                    if (sentenceSplit[i].toLowerCase().equals("i")) {

                        return word.conversion.get(0);

                    } else if (sentenceSplit[i].toLowerCase().equals("you")
                            && convertPersonalPronoun(wordsDictionary, i, sentenceSplit).equals("tu")) {

                        return word.conversion.get(1);

                    } else if (sentenceSplit[i].toLowerCase().equals("you")
                            && convertPersonalPronoun(wordsDictionary, i, sentenceSplit).equals("voi")) {

                        return word.conversion.get(2);

                    } else if (sentenceSplit[i].toLowerCase().equals("they")) {

                        return word.conversion.get(3);

                    } else if (sentenceSplit[i].toLowerCase().equals("she")
                            || sentenceSplit[i].toLowerCase().equals("he")
                            || sentenceSplit[i].toLowerCase().equals("it")) {

                        return word.conversion.get(0);
                    }
                } else {

                    /**
                     * if there isn't a personal pronoun situated before the verb inside the
                     * sentence, then the program searches for a noun
                     */
                    int j = indexWordToCheck - 1;
                    while (j >= 0 && (isPartOfSpeech(wordsDictionary, sentenceSplit[j]) == null
                            || !isPartOfSpeech(wordsDictionary, sentenceSplit[j]).get(0).equals("n"))) {

                        j--;
                    }
                    /**
                     * if it finds a noun then the program will return the right translated version
                     * of the verb
                     */
                    if (j >= 0 && isPartOfSpeech(wordsDictionary, sentenceSplit[j]).get(0).equals("n")) {

                        for (Word word0 : wordsDictionary) {

                            if (word0.word.equals(sentenceSplit[j].toLowerCase())) {

                                if (word0.singularOrPlural.equals("singular")) {

                                    return word.conversion.get(0);
                                } else {

                                    if (sentenceSplit[indexWordToCheck].equals("are")) {

                                        return word.conversion.get(2);
                                    } else {

                                        return word.conversion.get(3);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return sentenceSplit[indexWordToCheck];
    }

    /**
     * 
     * @param wordsDictionary
     * @param indexWordToCheck
     * @param sentenceSplit
     * @return returns the position in the sentence of the noun that a certain
     *         adjective located at the position 'indexWordToCheck' within the
     *         sentence determines
     */
    public static int checkAdjectiveDeterminesNoun(List<Word> wordsDictionary, int indexWordToCheck,
            String[] sentenceSplit) {

        int j = indexWordToCheck + 1;
        while (j < sentenceSplit.length && (isPartOfSpeech(wordsDictionary, sentenceSplit[j]).get(0).equals("adj")
                || sentenceSplit[j].equals("and") || sentenceSplit[j].equals("or"))) {

            j++;
        }
        /**
         * searches for the first noun situated after the adjective inside the sentence
         * and returns its position within the sentence
         */
        if (j < sentenceSplit.length && isPartOfSpeech(wordsDictionary, sentenceSplit[j]).get(0).equals("n")) {

            return j;
        }

        return -1;
    }
}
