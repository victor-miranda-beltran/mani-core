package com.victormiranda.mani.core.test.ut.security;

import com.victormiranda.mani.core.model.User;
import com.victormiranda.mani.core.security.TokenUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TokenUtilsTest {
    User user;

    @Before
    public void setup() {
        user = new User();

        user.setId(1);
        user.setName("victor");
    }

    @Test
    public void testCreateToken() {
        final String token = TokenUtils.createToken(user);

        Assert.assertNotNull(token);
    }

    @Test
    public void testValidateToken() {
        final String token = TokenUtils.createToken(user);

        final boolean validation = TokenUtils.validateToken(token, user);
        Assert.assertTrue(validation);
    }

    @Test
    public void testGetUserNameFromToken() {
        final String token = TokenUtils.createToken(user);

        Assert.assertEquals(TokenUtils.getUserFromToken(token), user.getUsername());
    }
}
