import bcomp.aaa.Role
import bcomp.aaa.User
import bcomp.aaa.UserRole
import bcomp.gym.Boulder
import bcomp.gym.FloorPlan
import bcomp.gym.Gym
import bcomp.gym.Section

import javax.imageio.ImageIO

class BootStrap {

    def grailsApplication

    private void createBoulders() {
        Gym hg = new Gym('Heavens Gate')

        def filePath = 'resources/halle_big.jpg'
        def imageFile = grailsApplication.getParentContext().getResource("classpath:$filePath").getFile()
        FloorPlan fp = new FloorPlan(ImageIO.read(imageFile))
        hg.addToFloorPlans(fp)

        def panicRoom = new Section(name: 'Panic Room')
        hg.addToSections(panicRoom)

        def dach = new Section(name: 'Großes Dach')
        hg.addToSections(dach)

        def grotte = new Section(name: 'Grotte')
        hg.addToSections(grotte)

        hg.addToSections(new Section(name: 'Walze'))
        hg.addToSections(new Section(name: 'Diamant'))

        hg.save(flush: true)

        Boulder b1 = new Boulder(grade: 'yellow', section: panicRoom)
        b1.save(flush: true)

        Boulder b2 = new Boulder(grade: 'blue', section: grotte)
        b2.save(flush: true)

        Boulder b3 = new Boulder(grade: 'black', section: dach)
        b3.save(flush: true)
    }

    private void createUser(String username, def role) {
        def user = new User(username: username, password: 'p').save(true)
        UserRole.create user, role, true
    }

    private void createSecurityData() {
        def bouldererRole = new Role(authority: 'ROLE_BOULDERER').save(flush: true)

        createUser 'flo', bouldererRole
        createUser 'chris', bouldererRole
        createUser 'thomas', bouldererRole
        createUser 'tony', bouldererRole
        createUser 'fia', bouldererRole
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
