package bcomp

import bcomp.gym.Gym
import grails.plugin.springsecurity.annotation.Secured

@Secured(['ROLE_BOULDERER'])
class LeaderboardController {

    def leaderboardService

    def view() {
        def gym = Gym.findByName('Boulderwelt')
        [gym: gym, ranking: leaderboardService.calculateRanking(gym)]
    }
}
