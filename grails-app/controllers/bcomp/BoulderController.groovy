package bcomp

import bcomp.gym.Boulder
import bcomp.gym.BoulderColor
import bcomp.gym.Grade
import bcomp.gym.Gym
import grails.plugin.springsecurity.annotation.Secured

@Secured(['ROLE_BOULDERER'])
class BoulderController {

    def boulderService

    def createForm() {
        def gym = Gym.findByName('Boulderwelt')

        def cmd = flash.cmd ?: new CreateBouldersCommand()
        // retrieve stored values for fields from session
        cmd.color = session.lastColor
        cmd.gradeCertainty = session.lastGradeCertainty
        cmd.grade = session.lastGrade
        cmd.gradeRangeLow = session.lastGradeRangeLow
        cmd.gradeRangeHigh = session.lastGradeRangeHigh

        render view: 'create', model: [gym: gym, colors: BoulderColor.values(), cmd: cmd]
    }

    def create(CreateBouldersCommand cmd) {
        if (cmd.validate()) {
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

            flashHelper.confirm 'default.set.message': ["bcomp.boulder.color.$cmd.color", 'bcomp.boulder.label'], true

            // store some options in session so that they do not need to be entered upon creating the next boulder
            session.lastColor = cmd.color
            session.lastGradeCertainty = cmd.gradeCertainty
            session.lastGrade = cmd.grade
            session.lastGradeRangeLow = cmd.gradeRangeLow
            session.lastGradeRangeHigh = cmd.gradeRangeHigh

            redirect controller: 'home', action: 'home'
        } else {
            // put command object into flash scope before redirecting, making it available to the view action
            flash.cmd = cmd
            redirect action: 'createForm'
        }
    }

}
