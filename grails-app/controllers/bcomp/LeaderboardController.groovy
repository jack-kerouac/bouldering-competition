package bcomp

class LeaderboardController {

    def leaderboardService

    def view() {
        def all = Gym.all

        def hg = Gym.findByName('Heavens Gate')
        [ranking: leaderboardService.calculateRanking(hg)]
    }
}
