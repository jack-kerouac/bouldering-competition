package bcomp

import grails.plugin.springsecurity.annotation.Secured

@Secured(['ROLE_BOULDERER'])
class HomeController {

    def home() {
        []
    }
}
