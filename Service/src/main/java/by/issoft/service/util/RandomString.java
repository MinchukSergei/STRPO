package by.issoft.service.util;

import org.apache.commons.lang.RandomStringUtils;
import org.springframework.stereotype.Component;

@Component
public class RandomString {
    public String getRandomString(int length) {
        return RandomStringUtils.randomAlphanumeric(length);
    }
}
