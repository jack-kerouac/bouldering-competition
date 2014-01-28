package bcomp

import bcomp.gym.Boulder
import bcomp.gym.Grade
import grails.plugin.springsecurity.annotation.Secured
import grails.rest.RestfulController
import org.springframework.http.HttpStatus

@Secured(['ROLE_BOULDERER'])
class BoulderController extends RestfulController {

    def boulderService

    static responseFormats = ['json', 'html']

    BoulderController() {
        super(Boulder)
    }

    def create() {
        []
    }

    def save(CreateBouldersCommand cmd) {
        if (!cmd.hasErrors()) {
            cmd.coordinates.each { coordinates ->
                Boulder b = new Boulder()
                b.gym = cmd.gym
                b.color = cmd.color
                b.onFloorPlan(cmd.floorPlan, coordinates.x, coordinates.y)
                // TODO: validation of entered grades, bind errors to fields, display errors in view
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
            }

            request.withFormat {
                html {
                    flashHelper.confirm 'default.set.message': ["bcomp.boulder.color.$cmd.color", 'bcomp.boulder.label'], true
                    redirect controller: 'home', action: 'home'
                }
                'json' {
                    // TODO: what to return?
                    render(contentType: "application/json", status: HttpStatus.CREATED) { null }
                }
            }
        } else {
            respond cmd.errors, view: 'create'
        }
    }

}
