package bcomp

import grails.plugin.springsecurity.annotation.Secured

@Secured(['ROLE_BOULDERER'])
class HomeController {

    def home() {
        [users: ['florian', 'christoph'], gyms: ['Heavens Gate', 'Boulderwelt']]
    }
}
