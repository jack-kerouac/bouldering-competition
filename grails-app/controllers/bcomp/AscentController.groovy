package bcomp

import bcomp.gym.Boulder
import bcomp.gym.Gym
import grails.plugin.springsecurity.annotation.Secured

@Secured(['ROLE_BOULDERER'])
class AscentController {

    def createForm() {
        def gym = Gym.findByName('Boulderwelt')
        def boulders = gym.boulders
        def boulderer = getPrincipal()
        def cmd = flash.cmd ?: new CreateAscentCommand()
        render view: 'create', model: [gym: gym, boulders: boulders, user: boulderer, cmd: cmd]
    }

    def create(CreateAscentCommand cmd) {
        if(cmd.validate()) {
            Ascent a = new Ascent()
            a.properties = cmd.properties
            a.save()

            flashHelper.confirm 'default.created.message': ['bcomp.ascent.label'], true
            redirect controller: 'home', action: 'home'
        }
        else {
            // put command object into flash scope before redirecting, making it available to the view action
            flash.cmd = cmd
            redirect action: 'createForm'
        }
    }
}
