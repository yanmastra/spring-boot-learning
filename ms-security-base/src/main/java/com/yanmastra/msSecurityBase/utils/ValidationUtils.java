package com.yanmastra.msSecurityBase.utils;

import java.util.regex.Pattern;

public class ValidationUtils {
    private ValidationUtils(){}

    private static final Pattern emailPattern = Pattern.compile("^[a-zA-Z0-9_! #$%&'*+/=?`{|}~^. -]+@[a-zA-Z0-9. -]+$");
    private static final Pattern htmlPattern = Pattern.compile("<(\"[^\"]*\"|'[^']*'|[^'\">])*>(.*<(\"[^\"]*\"|'[^']*'|[^'\">])*>)?");

    public static boolean isEmail(String email) {
        return emailPattern.matcher(email).matches();
    }

    public static boolean isHtml(String html) {
        return htmlPattern.matcher(html).matches();
    }

    public static boolean isJson(String s) {
        return JsonUtils.isJson(s);
    }
}
