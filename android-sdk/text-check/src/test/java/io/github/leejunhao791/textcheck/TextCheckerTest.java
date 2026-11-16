package io.github.leejunhao791.textcheck;

final class TextCheckerTest {
    public static void main(String[] args) {
        verify("hello", 5, "");
        verify("", 0, "EMPTY_INPUT");
        verify(null, 0, "EMPTY_INPUT");
        verify(" ", 1, "");
        verify("\uD83D\uDE42", 2, "");
        System.out.println("PASS TextChecker success, empty input and UTF-16 length");
    }

    private static void verify(String input, int expectedLength, String expectedError) {
        TextChecker.Result result = TextChecker.check(input);
        if (result.getLength() != expectedLength || !expectedError.equals(result.getErrorCode())) {
            throw new AssertionError("Unexpected result for input: " + input);
        }
    }
}
