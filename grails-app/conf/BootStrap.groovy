import bcomp.gym.Boulder
import bcomp.gym.Gym
import bcomp.aaa.Role
import bcomp.gym.Section
import bcomp.aaa.User
import bcomp.aaa.UserRole

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

        hg.save(flush: true)

        Boulder b1 = new Boulder(grade: 'yellow', section: panicRoom)
        b1.save(flush: true)

        Boulder b2 = new Boulder(grade: 'blue', section: grotte)
        b2.save(flush: true)

        Boulder b3 = new Boulder(grade: 'black', section: dach)
        b3.save(flush: true)
    }

    private void createSecurityData() {
        def bouldererRole = new Role(authority: 'ROLE_BOULDERER').save(flush: true)

        def flo = new User(username: 'flo', password: 'p')
        flo.save(flush: true)
        def chris = new User(username: 'chris', password: 'p')
        chris.save(flush: true)

        UserRole.create flo, bouldererRole, true
        UserRole.create chris, bouldererRole, true
    }

    def init = { servletContext ->
        environments {
            production {
                createSecurityData()
                createBoulders()
            }
            development {
                createSecurityData()
                createBoulders()
            }
            test {
            }
        }

    }

    def destroy = {
    }
}
