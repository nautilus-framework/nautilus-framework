package org.nautilus.web.feature.user.controller;

import org.nautilus.web.feature.user.service.SecurityService;
import org.nautilus.web.util.Redirect;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class BasicController {

    @Autowired
    protected Redirect redirect;

    @Autowired
    protected SecurityService securityService;
}
