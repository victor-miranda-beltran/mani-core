package com.victormiranda.mani.core.test.it;

import com.victormiranda.mani.bean.AccountInfo;
import org.junit.Before;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

public class BaseIT {

    public static final AccountInfo CURRENT_TEST_ACCOUNT = new AccountInfo.Builder()
            .withId(1)
            .withAccountNumber("1234")
            .build();

    @Before
    public void setup() {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("victor","paco"));
    }
}
