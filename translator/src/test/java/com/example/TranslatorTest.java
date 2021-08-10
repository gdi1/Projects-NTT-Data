package com.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TranslatorTest {

    @Test
    void testTranslateSentence() {

        assertAll(
                () -> assertEquals(
                        Translator.translateSentence("/Users/georgeile/Desktop/parent-project/translator/src/dictionary.txt",
                                "The majority of you love red tasty and beautiful apples."),
                        "Majoritatea dintre voi iubiti merele rosii gustoase si frumoase."),
                () -> assertEquals(
                        Translator.translateSentence("/Users/georgeile/Desktop/parent-project/translator/src/dictionary.txt", "She is a fast driver!"),
                        "Ea este un sofer rapid!"));
    }

    @Test
    void testTranslateFile() {

        assertAll(() -> assertEquals(
                Translator.translateFile("/Users/georgeile/Desktop/parent-project/translator/src/dictionary.txt", "/Users/georgeile/Desktop/parent-project/translator/src/fileToConvert.txt"),
                "Toti dintre voi vreti merele gustoase si frumoase. Eu iubesc doar merele rosii! Marul este gustos."),
                () -> assertEquals(
                        Translator.translateFile("/Users/georgeile/Desktop/parent-project/translator/src/dictionary.txt",
                                "/Users/georgeile/Desktop/parent-project/translator/src/secondFileToConvert.txt"),
                        "Eu vreau zece mere gustoase. Merele sunt foarte bune pentru sanatatea noastra! Soferul conduce rapid. Ea este rapida."));
    }

}
