import bcomp.SampleData
import bcomp.UserService
import bcomp.aaa.Role
import bcomp.aaa.User
import bcomp.aaa.UserRole
import bcomp.gym.*

import javax.imageio.ImageIO

class BootStrap {

    def grailsApplication

    def userService

    def boulderService

    private void createBoulders() {
        Gym gym = Gym.findByName('Boulderwelt')
        FloorPlan fp = gym.floorPlans.first()

        boulderService.setBoulder(gym, SampleData.createBoulder1(fp))

        boulderService.setBoulder(gym, SampleData.createBoulder2(fp))

        boulderService.setBoulder(gym, SampleData.createBoulder3(fp))

        boulderService.setBoulder(gym, SampleData.createBoulder4(fp))

        boulderService.setBoulder(gym, SampleData.createBoulder5(fp))

        boulderService.setBoulder(gym, SampleData.createBoulder6(fp))

        /* known grades */
        boulderService.setBoulder(gym, SampleData.createBoulder7(fp))

        boulderService.setBoulder(gym, SampleData.createBoulder8(fp))

        boulderService.setBoulder(gym, SampleData.createBoulder9(fp))

        gym.save(flush: true)
    }

    private void createGym() {
        Gym gym = SampleData.createGym('Boulderwelt', grailsApplication);

        gym.save(flush: true)
    }

    private void createBoulderer(String username, Grade initialGrade) {
        def user = new User(username: username, password: 'p', initialGrade: initialGrade)

        userService.registerBoulderer(user)
    }

    private void createSecurityData() {
        new Role(authority: UserService.BOULDERER_AUTHORITY).save(flush: true)

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
