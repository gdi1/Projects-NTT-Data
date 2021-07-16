package com.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;

/**
 * Unit test for simple App.
 */
class AppTest {
    /**
     * Testing to see if the constructors' names are properly returned
     */
    @Test
    void testConstrcutors() throws Exception {

        assertEquals(Arrays.asList("Person", "Person"), App.method(() -> {
            return "public";
        }, () -> {
            return "constructor";
        }));
    }

    /**
     * testing to see if the methods' names are properly returned
     */
    @Test
    void testMethods() {
        assertAll(() -> assertEquals(Arrays.asList("getName", "getEmail"), App.method(() -> {
            return "private String";
        }, () -> {
            return "method";
        })), () -> assertEquals(Arrays.asList("setName", "setAge", "setEmail"), App.method(() -> {
            return "public void";
        }, () -> {
            return "method";
        })), () -> assertEquals(Arrays.asList("newMethod"), App.method(() -> {
            return "public Map<String, String>";
        }, () -> {
            return "method";
        })));
    }

    /**
     * testing to see if the fields' names are properly returned
     */
    @Test
    void testFields(){
        assertAll(() -> assertEquals(Arrays.asList("name", "time", "domain", "city", "town", "email"), App.method(() -> {
            return "private String";
        }, () -> {
            return "field";
        })), () -> assertEquals(Arrays.asList("x", "id", "t", "p"), App.method(() -> {
            return "private final int";
        }, () -> {
            return "field";
        })), () -> assertEquals(Arrays.asList("car", "house"), App.method(() -> {
            return "public String";
        }, () -> {
            return "field";
        })));
    }
}

