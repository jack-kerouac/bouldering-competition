package bcomp.api

import bcomp.Ascent
import bcomp.gym.*
import grails.rest.RestfulController
import grails.validation.Validateable
import org.springframework.http.HttpStatus

class BoulderController extends RestfulController {

    static responseFormats = ['json']

    def boulderService

    BoulderController() {
        super(Boulder)
    }

    def save(CreateBouldersCommand cmd) {
        if (!cmd.hasErrors()) {
            List<Boulder> boulders = [] as List;

            // DO NOT UPDATE ASSOCIATIONS
            // TODO: how to prevent that gym attributes are changed when creating a boulder. The following does not
            // work: org.hibernate.HibernateException: reassociated object has dirty collection
            //cmd.gym.discard()
            cmd.floorPlan.discard()

            cmd.coordinates.each { coordinates ->
                Boulder b = new Boulder()
                b.gym = cmd.gym
                b.color = cmd.color
                b.onFloorPlan(cmd.floorPlan, coordinates.x, coordinates.y)
                // TODO: validation of entered grades
                switch (cmd.gradeCertainty) {
                    case Boulder.GradeCertainty.ASSIGNED:
                        b.assignedGrade(Grade.fromFontScale(cmd.grade));
                        break;
                    case Boulder.GradeCertainty.RANGE:
                        b.gradeRange(Grade.fromFontScale(cmd.gradeRangeLow),
                                Grade.fromFontScale(cmd.gradeRangeHigh));
                        break;
                    case Boulder.GradeCertainty.UNKNOWN:
                        b.unknownGrade();
                        break;
                }

                boulderService.setBoulder(cmd.gym, b)

                boulders.add(b)
            }

            // TODO: what to return?
            render(contentType: "application/json", status: HttpStatus.CREATED) { ['boulders': boulders] }
        } else {
            respond cmd.errors, view: '/createBoulders'
        }
    }

    def ascents() {
        def boulderId = params.boulderId
        def ascents = Ascent.findAll {
            boulder.id == boulderId
        }
        respond(ascents)
    }


    def showPhoto() {
        cache shared: true, validFor: 3600  // 1hr on content

        Boulder boulder = Boulder.findById(params.boulderId)
        if(!boulder.hasPhoto()) {
            render status: 404, text: 'no photo for this boulder available'
            return
        }

        response.setContentType("image/jpg")
        response.outputStream << boulder.getPhotoAsInputStream()
    }

    def savePhoto() {
        Boulder boulder = Boulder.findById(params.boulderId)

        boulder.setPhotoFromInputStream(request.getInputStream())
        render status: 201
    }

    def deletePhoto() {
        Boulder boulder = Boulder.findById(params.boulderId)
        boulder.removePhoto()
        render status: 200
    }
}


@Validateable
class CreateBouldersCommand {

    Gym gym

    FloorPlan floorPlan
    Set<Coordinates> coordinates

    Boulder.GradeCertainty gradeCertainty
    String grade

    String gradeRangeLow
    String gradeRangeHigh

    BoulderColor color


    static constraints = {
        importFrom Boulder

        floorPlan nullable: false, validator: { floorPlan, cmd ->
            return cmd.gym.floorPlans.contains(floorPlan)
        }

        gradeCertainty nullable: false
        grade nullable: true, validator: { grade, cmd ->
            return cmd.gradeCertainty == Boulder.GradeCertainty.ASSIGNED ? grade != null : true
        }
        gradeRangeLow nullable: true, validator: { gradeRangeLow, cmd ->
            return cmd.gradeCertainty == Boulder.GradeCertainty.RANGE ? gradeRangeLow != null : true
        }
        gradeRangeHigh nullable: true, validator: { gradeRangeHigh, cmd ->
            return cmd.gradeCertainty == Boulder.GradeCertainty.RANGE ? gradeRangeHigh != null : true
        }
    }

}

@Validateable
class Coordinates {
    Double x, y

    static constraints = {
        x min: 0.0d, max: 1.0d
        y min: 0.0d, max: 1.0d
    }
}
