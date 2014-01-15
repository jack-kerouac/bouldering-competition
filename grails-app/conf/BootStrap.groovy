import bcomp.aaa.Role
import bcomp.aaa.User
import bcomp.aaa.UserRole
import bcomp.gym.*

import javax.imageio.ImageIO

class BootStrap {

    def grailsApplication

    private void createBoulders() {
        Gym gym = Gym.findByName('Boulderwelt')
        FloorPlan fp = gym.floorPlans.first()

        Boulder b1 = new Boulder(color: BoulderColor.RED)
        b1.onFloorPlan(fp, 534 / 2000, 298 / 1393)
        b1.knownGradeRange(Grade.fromFontScale('1A'), Grade.fromFontScale('8A'))
        gym.addToBoulders(b1)

        Boulder b2 = new Boulder(color: BoulderColor.RED)
        b2.onFloorPlan(fp, 743 / 2000, 343 / 1393)
        b2.knownGradeRange(Grade.fromFontScale('1A'), Grade.fromFontScale('8A'))
        gym.addToBoulders(b2)

        Boulder b3 = new Boulder(color: BoulderColor.WHITE)
        b3.onFloorPlan(fp, 566 / 2000, 292 / 1393)
        b3.unknownGrade()
        gym.addToBoulders(b3)

        Boulder b4 = new Boulder(color: BoulderColor.WHITE)
        b4.onFloorPlan(fp, 612 / 2000, 481 / 1393)
        b4.knownGradeRange(Grade.fromFontScale('5C'), Grade.fromFontScale('6a+'))
        gym.addToBoulders(b4)

        Boulder b5 = new Boulder(color: BoulderColor.BLACK)
        b5.onFloorPlan(fp, 751 / 2000, 659 / 1393)
        b5.knownGradeRange(Grade.fromFontScale('6b'), Grade.fromFontScale('7a'))
        gym.addToBoulders(b5)

        Boulder b6 = new Boulder(color: BoulderColor.YELLOW_BLACK)
        b6.onFloorPlan(fp, 783 / 2000, 366 / 1393)
        b6.knownGradeRange(Grade.fromFontScale('3B'), Grade.fromFontScale('4A'))
        gym.addToBoulders(b6)

        /* known grades */
        Boulder b7 = new Boulder(color: BoulderColor.RED)
        b7.onFloorPlan(fp, 0.4056701030927835, 0.8858173076923077)
        b7.knownGrade(Grade.fromFontScale('6c'))
        gym.addToBoulders(b7)

        Boulder b8 = new Boulder(color: BoulderColor.BROWN)
        b8.onFloorPlan(fp, 0.5128865979381443, 0.8636279585798816)
        b8.knownGrade(Grade.fromFontScale('7a+'))
        gym.addToBoulders(b8)

        Boulder b9 = new Boulder(color: BoulderColor.YELLOW)
        b9.onFloorPlan(fp, 0.5304123711340206, 0.8680658284023669)
        b9.knownGrade(Grade.fromFontScale('7a'))
        gym.addToBoulders(b9)

        gym.save(flush: true)
    }

    private void createGym() {
        Gym gym = new Gym('Boulderwelt')

        def filePath = 'resources/halle_big.jpg'
        def imageFile = grailsApplication.getParentContext().getResource("classpath:$filePath").getFile()
        FloorPlan fp = new FloorPlan(ImageIO.read(imageFile))
        gym.addToFloorPlans(fp)

        gym.save(flash: true)
    }

    def bouldererRole

    private void createBoulderer(String username, Grade initialGrade) {
        def user = new User(username: username, password: 'p', initialGrade: initialGrade)
        user.save(flush: true)
        UserRole.create user, bouldererRole, true
    }

    private void createSecurityData() {
        bouldererRole = new Role(authority: 'ROLE_BOULDERER').save(flush: true)

        createBoulderer 'flo', Grade.fromFontScale('7b')
        createBoulderer 'christoph', Grade.fromFontScale('6c')
        createBoulderer 'fia', Grade.fromFontScale('6b')
        createBoulderer 'thomas', Grade.fromFontScale('6a')
        createBoulderer 'franz', Grade.fromFontScale('6c')
        createBoulderer 'anja', Grade.fromFontScale('6a')
        createBoulderer 'chris', Grade.fromFontScale('6a+')
        createBoulderer 'tony', Grade.fromFontScale('6c')
    }

    def init = { servletContext ->
        environments {
            production {
            }
            development {
                createSecurityData()
                createGym()
                createBoulders()
            }
            test {
            }
        }

    }

    def destroy = {
    }
}
