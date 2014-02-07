package bcomp.api

import bcomp.aaa.User
import grails.plugin.springsecurity.annotation.Secured
import grails.rest.RestfulController
import org.springframework.security.core.userdetails.UsernameNotFoundException

class UserController extends RestfulController {

    static responseFormats = ['json']

    def bouldererService

    UserController() {
        super(User)
    }

    def statistics(Long userId) {
        def boulderer = User.findById(userId)
        if (boulderer == null)
            throw new UsernameNotFoundException("boulderer ${userId} not found")

        def stats = bouldererService.getSessionStatistics(boulderer)

        respond stats
    }

}
