import bcomp.Boulder
import bcomp.Gym
import bcomp.Section

class BootStrap {

    def init = { servletContext ->
        Gym hg = new Gym(name: 'Heavens Gate')
        hg.sections = [] as Set

        def panicRoom = new Section(name: 'Panic Room')
        hg.sections.add(panicRoom)

        def dach = new Section(name: 'Großes Dach')
        hg.sections.add(dach)

        def grotte = new Section(name: 'Grotte')
        hg.sections.add(grotte)

        hg.save()

        Boulder b1 = new Boulder(grade: 'yellow', section: panicRoom)
        b1.save()

        Boulder b2 = new Boulder(grade: 'blue', section: grotte)
        b2.save()

        Boulder b3 = new Boulder(grade: 'black', section: dach)
        b3.save()
    }
    def destroy = {
    }
}
