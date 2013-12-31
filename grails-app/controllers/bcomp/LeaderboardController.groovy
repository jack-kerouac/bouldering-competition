package bcomp

import bcomp.gym.Gym
import grails.plugin.springsecurity.annotation.Secured

@Secured(['ROLE_BOULDERER'])
class LeaderboardController {

    def leaderboardService

    def view() {
        def all = Gym.all

        def hg = Gym.findByName('Heavens Gate')
        [ranking: leaderboardService.calculateRanking(hg)]
    }
}
