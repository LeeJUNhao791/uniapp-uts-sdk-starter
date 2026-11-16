package io.github.leejunhao791.textcheck;

public final class TextChecker {
    private TextChecker() {}

    public static Result check(String text) {
        if (text == null || text.isEmpty()) {
            return new Result(0, "EMPTY_INPUT");
        }
        return new Result(text.length(), "");
    }

    public static final class Result {
        private final int length;
        private final String errorCode;

        private Result(int length, String errorCode) {
            this.length = length;
            this.errorCode = errorCode;
        }

        public int getLength() {
            return length;
        }

        public String getErrorCode() {
            return errorCode;
        }
    }
}
