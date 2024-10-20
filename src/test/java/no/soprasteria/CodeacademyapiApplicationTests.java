package no.soprasteria;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.security.GeneralSecurityException;

public class CodeacademyapiApplicationTests {

    @Test
    void encrypt() throws GeneralSecurityException {
        System.out.println(CryptoUtil.encrypt("<insert>", System.getenv("MASTER_KEY_CODE_ACADEMY_RABBITMQ").getBytes()));
        System.out.println(CryptoUtil.decrypt("<insert>", System.getenv("MASTER_KEY_CODE_ACADEMY_RABBITMQ").getBytes()));
    }
}
