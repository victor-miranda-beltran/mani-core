package com.victormiranda.mani.core.test.it.controller;

import com.victormiranda.mani.core.model.User;
import com.victormiranda.mani.core.security.TokenFilter;
import com.victormiranda.mani.core.security.TokenUtils;
import com.victormiranda.mani.core.test.it.ITApp;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.Filter;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@SpringApplicationConfiguration(classes = ITApp.class)
@WebAppConfiguration
@IntegrationTest("server.port:0")
public class HiControllerIT {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private TokenFilter tokenFilter;

    @Autowired
    private Filter springSecurityFilterChain;

    private MockMvc mvc;

    @Before
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .addFilters(tokenFilter, this.springSecurityFilterChain)
                .build();
    }

    @Test
    public void testHi() throws Exception {
        User user = new User();
        user.setName("victor");
        user.setPassword("paco");

        mvc.perform(
            get("/")
                .header(TokenUtils.X_AUTH_TOKEN,TokenUtils.createToken(user))
            )
            .andExpect(status().isOk())
            .andDo(MockMvcResultHandlers.print());

    }

}
