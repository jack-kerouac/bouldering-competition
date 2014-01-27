import bcomp.Ascent
import bcomp.BouldererService
import bcomp.BoulderingSession
import bcomp.SampleData
import bcomp.aaa.Role
import bcomp.aaa.User
import bcomp.gym.*
import grails.converters.JSON

class BootStrap {

    def grailsApplication

    def messageSource

    def bouldererService

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

        bouldererService.registerBoulderer(user)
    }

    private void createSecurityData() {
        new Role(authority: BouldererService.BOULDERER_AUTHORITY).save(flush: true)

        createBoulderer 'flo', Grade.fromFontScale('7b')
        createBoulderer 'christoph', Grade.fromFontScale('6c')
        createBoulderer 'fia', Grade.fromFontScale('6b')
        createBoulderer 'thomas', Grade.fromFontScale('6a')
        createBoulderer 'franz', Grade.fromFontScale('6c')
        createBoulderer 'anja', Grade.fromFontScale('6a')
        createBoulderer 'chris', Grade.fromFontScale('6a+')
        createBoulderer 'tony', Grade.fromFontScale('6c')
    }


    def registerObjectMarshallers() {
        JSON.registerObjectMarshaller(BoulderColor) { BoulderColor color ->
            def map = [:]
            // TODO: how to not specify locale here?
            map['name'] = messageSource.getMessage("bcomp.boulder.color.$color", null, Locale.GERMAN)
            map['primary'] = "rgb($color.primaryColor.red, $color.primaryColor.green, $color.primaryColor.blue)"
            if (color.secondaryColor)
                map['secondary'] = "rgb($color.secondaryColor.red, $color.secondaryColor.green, $color.secondaryColor.blue)"
            return map
        }

        JSON.registerObjectMarshaller(Boulder.GradeCertainty) {
            it.toString()
        }

        JSON.registerObjectMarshaller(Grade) { grade ->
            def map = [:]
            map['value'] = grade.value
            map['font'] = grade.toFontScale()
            return map
        }

        JSON.registerObjectMarshaller(TentativeGrade) { tentGrade ->
            def map = [:]
            map['mean'] = tentGrade.mean
            map['variance'] = tentGrade.variance
            map['sigma'] = tentGrade.sigma
            return map
        }

        JSON.registerObjectMarshaller(FloorPlan) { FloorPlan floorPlan ->
            def map = [:]
            map['id'] = floorPlan.id
            map['img'] = [:]
            map['img']['widthInPx'] = floorPlan.widthInPx
            map['img']['heightInPx'] = floorPlan.heightInPx
            // TODO: how to externalize this?
            map['img']['url'] = "/gyms/$floorPlan.gym.id/floorPlans/$floorPlan.id"
            return map
        }

        JSON.registerObjectMarshaller(Gym) { Gym gym ->
            def map = [:]
            map['id'] = gym.id
            map['name'] = gym.name
            map['floorPlans'] = gym.floorPlans
            return map
        }


        JSON.registerObjectMarshaller(Boulder) { Boulder boulder ->
            def map = [:]
            map['id'] = boulder.id
            map['color'] = boulder.color
            map['grade'] = boulder.grade
            map['description'] = boulder.description
            map['end'] = boulder.end

            def initialGrade = [:]
            initialGrade['certainty'] = boulder.initialGradeCertainty
            switch (boulder.initialGradeCertainty) {
                case Boulder.GradeCertainty.ASSIGNED:
                    initialGrade['grade'] = boulder.assignedGrade
                    break;
                case Boulder.GradeCertainty.RANGE:
                    initialGrade['gradeLow'] = boulder.gradeRangeLow
                    initialGrade['gradeHigh'] = boulder.gradeRangeHigh
                    break;
            }
            map['initialGrade'] = initialGrade

            def location = [:]
            if (boulder.location instanceof OnFloorPlan) {
                OnFloorPlan ofp = boulder.location
                location['floorPlan'] = ofp.floorPlan
                location['x'] = ofp.x
                location['y'] = ofp.y
            } else
                throw new RuntimeException('cannot convert location that is not on floorplan')

            map['location'] = location

            // links
            map['gym'] = boulder.gym.id
            return map
        }

        JSON.registerObjectMarshaller(User) { User user ->
            def map = [:]
            map['id'] = user.id
            map['username'] = user.username
            map['registrationDate'] = user.registrationDate
            map['grade'] = user.grade
            map['initialGrade'] = user.initialGrade

            return map
        }

        JSON.registerObjectMarshaller(BoulderingSession) { BoulderingSession session ->
            def map = [:]
            map['id'] = session.id
            map['date'] = session.date
            map['ascents'] = session.ascents
            map['boulderer'] = session.boulderer.id
            return map
        }

        JSON.registerObjectMarshaller(Ascent) { Ascent ascent ->
            def map = [:]
            map['id'] = ascent.id
            map['style'] = ascent.style.toString()
            map['boulder'] = ascent.boulder.id
            return map
        }

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

        registerObjectMarshallers()
    }

    def destroy = {
    }
}
