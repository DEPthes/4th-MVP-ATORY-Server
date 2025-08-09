package ATORY.atory.global.util;

public final class PhoneMasker {
    private PhoneMasker() {}

    public static String mask(String raw) {
        if (raw == null || raw.isBlank()) return null;

        String digits = raw.replaceAll("\\D", "");
        if (digits.length() < 7) return raw;

        String head = digits.substring(0, 3);
        String tail = digits.substring(digits.length() - 4);
        return head + "-****-" + tail;
    }
}