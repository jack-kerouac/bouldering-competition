package bcomp.api

import bcomp.Ascent

import bcomp.api.Location
import bcomp.gym.*
import grails.rest.RestfulController
import grails.validation.Validateable
import org.grails.databinding.BindUsing
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
                        b.assignedGrade(BoulderGrade.fromFontScale(cmd.grade));
                        break;
                    case Boulder.GradeCertainty.RANGE:
                        b.gradeRange(BoulderGrade.fromFontScale(cmd.gradeRangeLow),
                                BoulderGrade.fromFontScale(cmd.gradeRangeHigh));
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

    def saveOneBoulder(CreateOneBoulderCommand cmd) {
        if (!cmd.hasErrors()) {
            Boulder b = new Boulder()

            cmd.gym.discard()
            b.gym = cmd.gym

            b.foreignId = cmd.foreignId

            b.color = cmd.color

            b.onFloorPlan(cmd.location.floorPlan, cmd.location.x, cmd.location.y)

            // TODO: validation of entered grades
            switch (cmd.initialGrade.certainty) {
                case Boulder.GradeCertainty.ASSIGNED:
                    b.assignedGrade(BoulderGrade.fromFontScale(cmd.initialGrade.grade));
                    break;
                case Boulder.GradeCertainty.RANGE:
                    b.gradeRange(BoulderGrade.fromFontScale(cmd.initialGrade.gradeLow),
                            BoulderGrade.fromFontScale(cmd.initialGrade.gradeHigh));
                    break;
                case Boulder.GradeCertainty.UNKNOWN:
                    b.unknownGrade();
                    break;
            }

            boulderService.setBoulder(b.gym, b)

            // TODO: what to return?
            render(contentType: "application/json", status: HttpStatus.CREATED) { b }
        } else {
            respond cmd.errors
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
        def id = params.boulderId
        withCacheHeaders {
            Boulder boulder = Boulder.findById(id)

            delegate.lastModified {
                boulder.lastUpdated
            }
            generate {
                if (!boulder.hasPhoto()) {
                    render status: HttpStatus.NOT_FOUND, text: 'no photo for this boulder available'
                } else {
                    if (request.method == "HEAD")
                        render status: HttpStatus.OK
                    else {
                        response.setContentType("image/jpg")
                        response.outputStream << boulder.getPhotoAsInputStream()
                    }
                }
            }
        }
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
class CreateOneBoulderCommand {
    static constraints = {
        foreignId nullable: true
    }

    Long foreignId

    Gym gym

    Location location

    @BindUsing({
        obj, source -> RouteColor.valueOf(source['color']['name'])
    })
    RouteColor color

    InitialGrade initialGrade
}

@Validateable
class Location {
    FloorPlan floorPlan
    double x, y

    static constraints = {
        x min: 0.0d, max: 1.0d
        y min: 0.0d, max: 1.0d
    }
}

@Validateable
class InitialGrade {
    Boulder.GradeCertainty certainty

    @BindUsing({
        obj, source -> source['grade']['font']
    })
    String grade

    @BindUsing({
        obj, source -> source['gradeLow']['font']
    })
    String gradeLow

    @BindUsing({
        obj, source -> source['gradeHigh']['font']
    })
    String gradeHigh

    static constraints = {
        grade nullable: true, validator: { grade, cmd ->
            return cmd.certainty == Boulder.GradeCertainty.ASSIGNED ? grade != null : true
        }
        gradeLow nullable: true, validator: { gradeLow, cmd ->
            return cmd.certainty == Boulder.GradeCertainty.RANGE ? gradeLow != null : true
        }
        gradeHigh nullable: true, validator: { gradeHigh, cmd ->
            return cmd.certainty == Boulder.GradeCertainty.RANGE ? gradeHigh != null : true
        }
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

    RouteColor color


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
