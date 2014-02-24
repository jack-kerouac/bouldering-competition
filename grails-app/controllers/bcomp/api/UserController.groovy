package bcomp.api

import bcomp.aaa.User
import grails.rest.RestfulController
import org.springframework.security.core.userdetails.UsernameNotFoundException

class UserController extends RestfulController {

    static responseFormats = ['json']

    def bouldererService

    def daoAuthenticationProvider

    def springSecurityService

    UserController() {
        super(User)
    }

    def index(String email, Integer max) {
        if (email != null) {
            respond User.findByUsername(email)
        } else {
            super.index(max)
        }
    }

    def statistics(Long userId) {
        def boulderer = User.findById(userId)
        if (boulderer == null)
            throw new UsernameNotFoundException("boulderer ${userId} not found")

        def stats = bouldererService.getSessionStatistics(boulderer)

        respond stats
    }

}