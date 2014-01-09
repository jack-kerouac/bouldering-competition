package bcomp

import bcomp.gym.Boulder
import bcomp.gym.BoulderColor
import bcomp.gym.Gym
import grails.plugin.springsecurity.annotation.Secured

@Secured(['ROLE_BOULDERER'])
class BoulderController {

    def createForm() {
        def gym = Gym.findByName('Boulderwelt')
        def cmd = flash.cmd ?: new CreateBoulderCommand()
        render view: 'create', model: [gym: gym, colors: BoulderColor.values(), cmd: cmd]
    }

    def create(CreateBoulderCommand cmd) {
        if(cmd.validate()) {
            Boulder b = new Boulder()
            b.properties = cmd.properties
            b.onFloorPlan(cmd.floorPlan, cmd.x, cmd.y)
            b.save()

            flashHelper.confirm 'default.created.message': [b.color, 'bcomp.boulder.label']
            redirect controller: 'home', action: 'home'
        }
        else {
            // put command object into flash scope before redirecting, making it available to the view action
            flash.cmd = cmd
            redirect action: 'createForm'
        }
    }

}
