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

        Boulder b1 = new Boulder(grade: 'red')
        b1.onFloorPlan(fp, 534, 298)
        hg.addToBoulders(b1)

        Boulder b2 = new Boulder(grade: 'red')
        b2.onFloorPlan(fp, 743, 343)
        hg.addToBoulders(b2)

        Boulder b3 = new Boulder(grade: 'white')
        b3.onFloorPlan(fp, 566, 292)
        hg.addToBoulders(b3)

        Boulder b4 = new Boulder(grade: 'white')
        b4.onFloorPlan(fp, 612, 481)
        hg.addToBoulders(b4)

        Boulder b5 = new Boulder(grade: 'black')
        b5.onFloorPlan(fp, 751, 659)
        hg.addToBoulders(b5)

        Boulder b6 = new Boulder(grade: 'blue')
        b6.onFloorPlan(fp, 783, 366)
        hg.addToBoulders(b6)

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
