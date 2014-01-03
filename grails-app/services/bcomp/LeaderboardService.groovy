package bcomp

import bcomp.gym.Gym
import grails.transaction.Transactional

@Transactional
class LeaderboardService {

    final static int POINTS_FLASH = 35
    final static int POINTS_TOP = 25

    def calculateRanking(Gym gym) {
        def ascents = Ascent.where {
            boulder.section.gym == gym
        }

        def ranking = [:]

        ascents.each { a ->
            Rank r = ranking.get(a.boulderer)
            if (r == null) {
                r = new Rank(boulderer: a.boulderer)
                r.lastSession = new Date(0, 01, 01)
                ranking.put(a.boulderer, r)
            }

            r.lastSession = a.date.after(r.lastSession) ? a.date : r.lastSession

            r.countTops++
            switch (a.style) {
                case Ascent.Style.flash:
                    r.countFlashes++
                    break;
            }
        }

        // TO SET
        ranking = ranking.values()

        ranking.each {
            it.score = POINTS_FLASH * it.countFlashes + POINTS_TOP * (it.countTops - it.countFlashes)
        }

        if(ranking.size() == 0)
            return ranking
        else if(ranking.size() == 1) {
            ranking[0].position = 1;
            return ranking
        }
        else {
            def sorted = ranking.sort { it.score }.reverse()
            sorted[0].position = 1;
            int pos = 1
            int prevScore = sorted[0].score

            sorted.subList(1, sorted.size()).each {
                if (prevScore != it.score)
                    pos++
                it.position = pos
                prevScore = it.score
            }

            return ranking
        }
    }
}
