import bcomp.Boulder
import bcomp.Boulderer
import bcomp.Gym
import bcomp.Section

class BootStrap {

    private void createBoulders() {
        Gym hg = new Gym('Heavens Gate')

        def panicRoom = new Section(name: 'Panic Room')
        hg.addSection(panicRoom)

        def dach = new Section(name: 'Großes Dach')
        hg.addSection(dach)

        def grotte = new Section(name: 'Grotte')
        hg.addSection(grotte)

        hg.addSection(new Section(name: 'Walze'))
        hg.addSection(new Section(name: 'Diamant'))

        hg.save()

        Boulder b1 = new Boulder(grade: 'yellow', section: panicRoom)
        b1.save()

        Boulder b2 = new Boulder(grade: 'blue', section: grotte)
        b2.save()

        Boulder b3 = new Boulder(grade: 'black', section: dach)
        b3.save()

        Boulderer flo = new Boulderer(name: 'flo')
        flo.save()
        Boulderer chris = new Boulderer(name: 'chris')
        chris.save()
    }

    def init = { servletContext ->
        environments {
            production {
                createBoulders()
            }
            development {
                createBoulders()
            }
            test {
            }
        }

    }

    def destroy = {
    }
}
