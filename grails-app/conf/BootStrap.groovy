import bcomp.aaa.Role
import bcomp.aaa.User
import bcomp.aaa.UserRole
import bcomp.gym.Boulder
import bcomp.gym.FloorPlan
import bcomp.gym.Gym

import javax.imageio.ImageIO

class BootStrap {

    def grailsApplication

    private void createBoulders() {
        Gym hg = new Gym('Heavens Gate')

        def filePath = 'resources/halle_big.jpg'
        def imageFile = grailsApplication.getParentContext().getResource("classpath:$filePath").getFile()
        FloorPlan fp = new FloorPlan(ImageIO.read(imageFile))
        hg.addToFloorPlans(fp)

        Boulder b1 = new Boulder(grade: 'yellow')
        hg.addToBoulders(b1)

        Boulder b2 = new Boulder(grade: 'blue')
        hg.addToBoulders(b2)

        Boulder b3 = new Boulder(grade: 'black')
        hg.addToBoulders(b3)

        hg.save(flush: true)
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
