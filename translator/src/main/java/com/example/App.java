package com.example;

public final class App {

    public static void main(String[] args) throws Exception {

      /*String result = Translator.translateFile("translator/src/dictionary.txt", "translator/src/secondFileToConvert.txt");
      System.out.println(result);*/

      String result0 = Translator.translateSentence("translator/src/dictionary.txt", "All of you, the apples, are beautiful tasty and red.");
      System.out.println(result0);

      String result1 = Translator.translateSentence("translator/src/dictionary.txt", "She is a fast red or beautiful driver!");
      System.out.println(result1);
    }
}

