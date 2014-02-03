package bcomp

import bcomp.aaa.User
import bcomp.gym.Boulder
import bcomp.gym.FloorPlan
import bcomp.gym.Grade
import bcomp.gym.Gym

/**
 * User: florian
 */
class LeaderboardServiceTest extends GroovyTestCase {

    def grailsApplication

    def leaderboardService

    def boulderService

    Gym hg
    Boulder b1, b2, b3
    User flo, chris, fia


    void createFixture() {
        hg = SampleData.createBoulderwelt(grailsApplication)
        FloorPlan fp = hg.floorPlans.first()
        hg.save()

        b1 = SampleData.createBoulder1(fp)
        boulderService.setBoulder(hg, b1)

        b2 = SampleData.createBoulder2(fp)
        boulderService.setBoulder(hg, b2)

        b3 = SampleData.createBoulder3(fp)
        boulderService.setBoulder(hg, b3)

        hg.save(flush: true)

        flo = new User(username: 'flo', password: 'p', initialGrade: Grade.fromFontScale('6a'))
        flo.save()
        chris = new User(username: 'chris', password: 'p', initialGrade: Grade.fromFontScale('6a'))
        chris.save()
        fia = new User(username: 'fia', password: 'p', initialGrade: Grade.fromFontScale('6a'))
        fia.save()
    }

    void testSimpleCalculate() {
        createFixture()
        BoulderingSession s1 = new BoulderingSession(gym: hg, boulderer: flo, date: new Date());
        Ascent a1 = new Ascent(boulder: b1, style: Ascent.Style.flash)
        s1.addToAscents(a1).save()
        BoulderingSession s2 = new BoulderingSession(gym: hg, boulderer: chris, date: new Date());
        Ascent a2 = new Ascent(boulder: b1, style: Ascent.Style.top)
        s2.addToAscents(a2).save()

        def ranking = leaderboardService.calculateRanking(hg)
        assert ranking.size() == 2

        def rankFlo = ranking.find { rank -> rank.boulderer.username == 'flo' }
        assertNotNull rankFlo
        assert rankFlo.position == 1
        assert rankFlo.lastSession == s1.date
        assert rankFlo.countFlashes == 1
        assert rankFlo.countTops == 1
        assert rankFlo.score == LeaderboardService.POINTS_FLASH

        def rankChris = ranking.find { it.boulderer.username == 'chris' }
        assertNotNull rankChris
        assert rankChris.position == 2
        assert rankChris.lastSession == s2.date
        assert rankChris.countFlashes == 0
        assert rankChris.countTops == 1
        assert rankChris.score == LeaderboardService.POINTS_TOP
    }

    void testScore() {
        createFixture()
        BoulderingSession s = new BoulderingSession(gym: hg, boulderer: flo, date: new Date());
        Ascent a1 = new Ascent(boulder: b1, style: Ascent.Style.flash)
        s.addToAscents(a1);
        Ascent a2 = new Ascent(boulder: b2, style: Ascent.Style.flash)
        s.addToAscents(a2);
        Ascent a3 = new Ascent(boulder: b3, style: Ascent.Style.top, tries: 3)
        s.addToAscents(a3);
        s.save()

        def ranking = leaderboardService.calculateRanking(hg)

        assert ranking.find { rank -> rank.boulderer.username == 'flo' }.score == 2 * LeaderboardService.POINTS_FLASH +
                1 * LeaderboardService.POINTS_TOP
    }

    void testEqualScore() {
        createFixture()
        BoulderingSession s1 = new BoulderingSession(gym: hg, boulderer: flo, date: new Date());
        Ascent a1 = new Ascent(boulder: b1, style: Ascent.Style.flash)
        s1.addToAscents(a1).save()
        BoulderingSession s2 = new BoulderingSession(gym: hg, boulderer: chris, date: new Date());
        Ascent a2 = new Ascent(boulder: b1, style: Ascent.Style.flash)
        s2.addToAscents(a2).save()

        def ranking = leaderboardService.calculateRanking(hg)

        assert ranking.find { rank -> rank.boulderer.username == 'flo' }.position ==
                ranking.find { rank -> rank.boulderer.username == 'chris' }.position
    }

}
