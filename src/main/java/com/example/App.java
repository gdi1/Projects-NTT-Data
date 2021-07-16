package com.example;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

public final class App {

    public static List<String> method(Supplier<String> prefix, Supplier<String> whatToReturn) throws IOException {

        /**
         * storing the results
         */
        List<String> results = new ArrayList<>();

        /**
         * split the prefix into multiple words
         */
        List<String> splitPrefix = Arrays.asList(prefix.get().split("[ ,]+"));

        /**
         * reading all the lines from the file
         */
        List<String> lines = Files.readAllLines(Paths.get("src/main/java/com/example/Person.java"));
        List<String> cleanedLines = new ArrayList<>();
        /**
         * removing the one line comments
         */
        for (int i = 0; i < lines.size(); i++) {
            if (!lines.get(i).startsWith("//")) {

                cleanedLines.add(lines.get(i));
            }
        }
        /**
         * getting the tokens
         */
        List<String> tokens = cleanedLines.stream().flatMap(line -> Arrays.stream(line.split("[ ,]+")))
                .filter(x -> !x.equals("")).collect(Collectors.toList());

        /**
         * removing all the blocks of comments
         */
        tokens = cleanData(tokens);
        String className = "";

        /**
         * getting the class' name
         */
        className = getClassName(tokens);

        /**
         * getting the desired results
         */
        results = find(splitPrefix, tokens, whatToReturn.get(), className);

        return results;
    }

    public static List<String> find(List<String> splitPrefix, List<String> tokens, String whatToReturn,
            String className) {

        List<String> results = new ArrayList<>();
        /**
         * getting the number of tokens inside the prefix
         */
        int lengthPrefix = splitPrefix.size();

        for (int i = 0; i < tokens.size(); i++) {

            /**
             * trying to find all the occurrences of the prefix within the java file
             */
            boolean check = true;
            for (int j = 0; j < lengthPrefix; j++) {

                if ((i + j) < tokens.size() && !tokens.get(i + j).equals(splitPrefix.get(j))) {
                    check = false;
                    break;

                } else if ((i + j) >= tokens.size()) {
                    check = false;
                    break;
                }
            }
            if (check == true) {

                if ((i + lengthPrefix) < tokens.size()) {

                    switch (whatToReturn) {

                        case "field":
                            /**
                             * The following block of code deals with properly extracting the desired fields
                             * from the java file. It tries to make use of different characters such as "(",
                             * ";" and "=" in order to be able to differentiate between fields, methods and
                             * constructors.
                             * 
                             * Moreover, in situations where there are fields that have been both declared
                             * and initialised, it will only retain the field's name, making sure that its
                             * assigned value is omitted. The program is also capable of properly extracting
                             * fields that have declared within the same line of code, separated by a comma.
                             * 
                             */

                            if ((i + lengthPrefix + 1) < tokens.size()) {

                                if (!tokens.get(i + lengthPrefix).contains("(")
                                        && !tokens.get(i + lengthPrefix + 1).startsWith("(")) {

                                    for (int j = i + lengthPrefix; j < tokens.size(); j++) {

                                        if (tokens.get(j).contains("=") && !tokens.get(j).startsWith("=")
                                                && !tokens.get(j).endsWith("=")) {

                                            results.add(tokens.get(j).substring(0, tokens.get(j).indexOf("=")));

                                            if (tokens.get(j).endsWith(";")) {
                                                break;
                                            }

                                        } else if (tokens.get(j).endsWith("=") && !tokens.get(j).equals("=")) {

                                            results.add(tokens.get(j).substring(0, tokens.get(j).length() - 1));
                                            if (tokens.get(j + 1).endsWith(";")) {
                                                break;
                                            } else {
                                                j++;
                                            }

                                        } else if (tokens.get(j).startsWith("=") && !tokens.get(j).equals("=")) {

                                            if (tokens.get(j).endsWith(";")) {
                                                break;
                                            }

                                        } else if (tokens.get(j).equals("=")) {

                                            if (tokens.get(j + 1).endsWith(";")) {
                                                break;
                                            } else {
                                                j++;
                                            }

                                        } else if (tokens.get(j).endsWith(";") && !tokens.get(j).equals(";")) {

                                            results.add(tokens.get(j).substring(0, tokens.get(j).length() - 1));
                                            break;

                                        } else if (tokens.get(j).equals(";")) {
                                            break;

                                        } else {

                                            results.add(tokens.get(j));
                                        }
                                    }
                                }

                            }
                            break;

                        /**
                         * properly extracting the methods' name that have a certain prefix
                         */
                        case "method":

                            if (tokens.get(i + lengthPrefix).contains("(")) {

                                results.add(tokens.get(i + lengthPrefix).substring(0,
                                        tokens.get(i + lengthPrefix).indexOf("(")));

                            } else if ((i + lengthPrefix + 1) < tokens.size()
                                    && tokens.get(i + lengthPrefix + 1).startsWith("(")) {

                                results.add(tokens.get(i + lengthPrefix));
                            }
                            break;

                        /**
                         * properly extracting the constructors' name that have a certain prefix
                         */
                        case "constructor":

                            if (!className.equals("") && tokens.get(i + lengthPrefix).equals(className)
                                    && (i + lengthPrefix + 1) < tokens.size()
                                    && tokens.get(i + lengthPrefix + 1).startsWith("(")) {

                                results.add(tokens.get(i + lengthPrefix));

                            } else if (!className.equals("") && tokens.get(i + lengthPrefix).contains("(")
                                    && tokens.get(i + lengthPrefix)
                                            .substring(0, tokens.get(i + lengthPrefix).indexOf("("))
                                            .equals(className)) {

                                results.add(tokens.get(i + lengthPrefix).substring(0,
                                        tokens.get(i + lengthPrefix).indexOf("(")));

                            } else if (!className.equals(""))
                                break;

                        default:
                            break;
                    }
                }
            }
        }

        return results;
    }

    /**
     * 
     * @param tokens the list of tokens created from the java file
     * @return returns the list of tokens after filtering those that have been
     *         located inside a block of comments
     */
    public static List<String> cleanData(List<String> tokens) {

        List<String> cleanedData = new ArrayList<>();

        boolean check = true;
        for (int i = 0; i < tokens.size(); i++) {

            if (tokens.get(i).startsWith("/*") || tokens.get(i).startsWith("//")) {

                check = false;
            }

            if (check == true) {

                cleanedData.add(tokens.get(i));
            }

            if (tokens.get(i).endsWith("*/")) {
                check = true;
            }
        }
        return cleanedData;
    }

    /**
     * 
     * @param tokens list of filtered tokens from the java file
     * @return returns the name of the Java Class
     */
    public static String getClassName(List<String> tokens) {

        String className = "";

        /**
         * getting the class' name
         */
        for (int i = 0; i < tokens.size(); i++) {
            if (tokens.get(i).equals("class")) {

                if ((i + 1) < tokens.size()) {

                    if (!tokens.get(i + 1).endsWith("{")) {
                        className = tokens.get(i + 1);
                    } else {
                        className = tokens.get(i + 1).substring(0, tokens.get(i + 1).indexOf("{"));
                    }
                    break;
                }
            }
        }

        return className;
    }

    public static void main(String[] args) throws Exception {

        List<String> results = method(() -> {
            return "public";
        }, () -> {
            return "constructor";
        });
        results.stream().forEach(System.out::println);
    }
}

