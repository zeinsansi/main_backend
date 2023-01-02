package com.backend.main._backend.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class RedirectControllerTest {
    RedirectController redirectController = new RedirectController();

    @Test
    void testRedirectToRoot() {
        String result = redirectController.redirectToRoot();
        Assertions.assertEquals("redirect:/", result);
    }
}