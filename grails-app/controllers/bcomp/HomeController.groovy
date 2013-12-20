package bcomp

class HomeController {

    def home() {
        [users: ['florian', 'christoph'], gyms: ['Heavens Gate', 'Boulderwelt']]
    }
}
