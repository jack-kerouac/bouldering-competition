package bcomp
/**
 * User: florian
 */
class LeaderboardServiceTest extends GroovyTestCase {

    def leaderboardService

    Gym hg
    Boulder b1, b2, b3
    Boulderer flo, chris, fia


    void setUp() {
        hg = new Gym('Heavens Gate')
        def panicRoom = new Section(name: 'Panic Room')
        hg.addSection(panicRoom)
        hg.save()

        b1 = new Boulder(grade: 'yellow', section: panicRoom)
        b1.save()
        b2 = new Boulder(grade: 'red', section: panicRoom)
        b2.save()
        b3 = new Boulder(grade: 'black', section: panicRoom)
        b3.save()

        flo = new Boulderer(name: 'flo')
        flo.save()
        chris = new Boulderer(name: 'chris')
        chris.save()
        fia = new Boulderer(name: 'fia')
        fia.save()
    }

    void testSimpleCalculate() {
        Ascent a1 = new Ascent(date: new Date(), boulderer: flo, boulder: b1, style: Ascent.Style.flash)
        a1.save()
        Ascent a2 = new Ascent(date: new Date(), boulderer: chris, boulder: b1, style: Ascent.Style.top, tries: 3)
        a2.save()

        def ranking = leaderboardService.calculateRanking(hg)
        assert ranking.size() == 2

        def rankFlo = ranking.find { rank -> rank.boulderer.name == 'flo' }
        assertNotNull rankFlo
        assert rankFlo.position == 1
        assert rankFlo.lastSession == a1.date
        assert rankFlo.countFlashes == 1
        assert rankFlo.countTops == 0
        assert rankFlo.score == LeaderboardService.POINTS_FLASH

        def rankChris = ranking.find { it.boulderer.name == 'chris' }
        assertNotNull rankChris
        assert rankChris.position == 2
        assert rankChris.lastSession == a2.date
        assert rankChris.countFlashes == 0
        assert rankChris.countTops == 1
        assert rankChris.score == LeaderboardService.POINTS_TOP
    }

    void testScore() {
        Ascent a1 = new Ascent(date: new Date(), boulderer: flo, boulder: b1, style: Ascent.Style.flash)
        a1.save()
        Ascent a2 = new Ascent(date: new Date(), boulderer: flo, boulder: b2, style: Ascent.Style.flash)
        a2.save()
        Ascent a3 = new Ascent(date: new Date(), boulderer: flo, boulder: b3, style: Ascent.Style.top, tries: 3)
        a3.save()

        def ranking = leaderboardService.calculateRanking(hg)

        assert ranking.find { rank -> rank.boulderer.name == 'flo' }.score == 2 * LeaderboardService.POINTS_FLASH +
                1 * LeaderboardService.POINTS_TOP
    }

    void testEqualScore() {
        Ascent a1 = new Ascent(date: new Date(), boulderer: flo, boulder: b1, style: Ascent.Style.flash)
        a1.save()
        Ascent a2 = new Ascent(date: new Date(), boulderer: chris, boulder: b1, style: Ascent.Style.flash)
        a2.save()

        def ranking = leaderboardService.calculateRanking(hg)

        assert ranking.find { rank -> rank.boulderer.name == 'flo' }.position ==
                ranking.find { rank -> rank.boulderer.name == 'chris' }.position
    }

}
