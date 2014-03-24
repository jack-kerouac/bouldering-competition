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

    def grailsLinkGenerator

    def messageSource

    def bouldererService

    def boulderService

    private void createBoulders() {
        Gym gym = Gym.findByName('Boulderwelt München')
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

    private void createGyms() {
        SampleData.createBoulderwelt(grailsApplication).save(flush: true);
        SampleData.createHeavensGate(grailsApplication).save(flush: true);
    }

    private void createBoulderer(String username, BoulderGrade initialGrade) {
        def user = new User(email: username, password: 'p', initialGrade: initialGrade)

        bouldererService.registerBoulderer(user)
    }

    private void createSecurityData() {
        new Role(authority: BouldererService.BOULDERER_AUTHORITY).save(flush: true)

        createBoulderer 'florian.rampp@gmail.com', BoulderGrade.fromFontScale('7b')
        createBoulderer 'christoph.rampp@gmail.com', BoulderGrade.fromFontScale('6c')
        createBoulderer 'fia@chalkup.de', BoulderGrade.fromFontScale('6b')
        createBoulderer 'thomas@duddits.de', BoulderGrade.fromFontScale('6a')
        createBoulderer 'franz@chalkup.de', BoulderGrade.fromFontScale('6c')
        createBoulderer 'anja@chalkup.de', BoulderGrade.fromFontScale('6a')
        createBoulderer 'chriswecklich@gmail.com', BoulderGrade.fromFontScale('6a+')
        createBoulderer 'anthony.graglia@gmail.com', BoulderGrade.fromFontScale('6c')
    }


    def registerObjectMarshallers() {
        JSON.registerObjectMarshaller(RouteColor) { RouteColor color ->
            def map = [:]
            map['name'] = color.toString();
            map['germanName'] = messageSource.getMessage("bcomp.boulder.color.$color", null, Locale.GERMAN);
            map['englishName'] = messageSource.getMessage("bcomp.boulder.color.$color", null, Locale.ENGLISH);
            map['primary'] = RouteColor.asRgb(color.primaryColor)
            if (color.secondaryColor)
                map['secondary'] = RouteColor.asRgb(color.secondaryColor)
            if (color.ternaryColor)
                map['ternary'] = RouteColor.asRgb(color.ternaryColor)
            return map
        }

        JSON.registerObjectMarshaller(Boulder.GradeCertainty) {
            it.toString()
        }

        JSON.registerObjectMarshaller(BoulderGrade) { grade ->
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
            map['img']['url'] = floorPlan.imageUrl
            return map
        }

        JSON.registerObjectMarshaller(Gym) { Gym gym ->
            def map = [:]
            map['id'] = gym.id
            map['name'] = gym.name
            map['created'] = gym.dateCreated
            map['floorPlans'] = gym.floorPlans
            map['colors'] = RouteColor.values()
            return map
        }


        JSON.registerObjectMarshaller(Boulder) { Boulder boulder ->
            def map = [:]
            map['id'] = boulder.id
            map['color'] = boulder.color
            map['grade'] = boulder.grade
            if(boulder.name)
                map['name'] = boulder.name
            map['description'] = boulder.description
            map['created'] = boulder.dateCreated
            if(boulder.end)
                map['end'] = boulder.end.format("yyyy-MM-dd");

            if(boulder.setter) {
                def setter = [:]
                setter['id'] = boulder.setter.id
                map['setter'] = setter
            }


            def initialGrade = [:]
            initialGrade['certainty'] = boulder.initialGradeCertainty
            initialGrade['readable'] = boulder.initialGrade
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

            if (boulder.hasPhoto()) {
                def photo = [:]
                // creation of link using controller, action, and ID does not work since the mapping involves HTTP
                // methods to actions
                photo['url'] = grailsLinkGenerator.link(['uri': "/rest/v1/boulders/$boulder.id/photo",
                        'absolute': true])
                map['photo'] = photo
            }

            map['gym'] = boulder.gym.id
            return map
        }

        JSON.registerObjectMarshaller(User) { User user ->
            def map = [:]
            map['id'] = user.id
            map['email'] = user.email
            // TODO: remove (it is for backwards compatibility)
            map['username'] = user.email

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

            def gym = [:]
            gym['id'] = session.gym.id
            gym['name'] = session.gym.name
            map['gym'] = gym
            return map
        }

        JSON.registerObjectMarshaller(Ascent) { Ascent ascent ->
            def map = [:]
            map['id'] = ascent.id
            map['style'] = ascent.style.toString()
            map['boulder'] = ascent.boulder.id
            map['boulderer'] = ascent.session.boulderer.id
            map['date'] = ascent.session.date
            return map
        }

    }

    def init = { servletContext ->
        environments {
            production {
            }
            development {
                createSecurityData()
                createGyms()
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
