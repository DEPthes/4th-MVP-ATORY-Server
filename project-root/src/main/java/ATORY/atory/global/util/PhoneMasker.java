package ATORY.atory.global.util;

import org.springframework.stereotype.Component;

@Component
public class PhoneMasker {
    public String mask(String phone) {
        if (phone == null) return null;
        String p = phone.replaceAll("[^0-9]", "");
        if (p.length() < 7) return phone;
        return p.substring(0,3) + "-" + p.substring(3,5) + "**-" + p.substring(7);
    }
}