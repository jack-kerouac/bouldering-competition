package bcomp

import bcomp.gym.Boulder
import bcomp.gym.Gym
import grails.plugin.springsecurity.annotation.Secured

@Secured(['ROLE_BOULDERER'])
class BoulderController {

    def createForm() {
        // TODO: without this invocation, hg below only has one section ?!?
        def gyms = Gym.all

        def gym = Gym.findByName('Heavens Gate')
        def grades = ['green', 'yellow', 'red', 'blue', 'black', 'pink']
        def cmd = flash.cmd ?: new CreateBoulderCommand()
        render view: 'create', model: [gym: gym, grades: grades, cmd: cmd]
    }

    def create(CreateBoulderCommand cmd) {
        if(cmd.validate()) {
            Boulder b = new Boulder()
            b.properties = cmd.properties
            b.save()

            flashHelper.confirm 'default.created.message': [b.grade, 'bcomp.boulder.label'], true
            redirect controller: 'home', action: 'home'
        }
        else {
            // put command object into flash scope before redirecting, making it available to the view action
            flash.cmd = cmd
            redirect action: 'createForm'
        }
    }

}
