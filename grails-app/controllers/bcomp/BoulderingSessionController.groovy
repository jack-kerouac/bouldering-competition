package bcomp

import bcomp.aaa.User
import bcomp.gym.Boulder
import bcomp.gym.Gym
import grails.plugin.springsecurity.annotation.Secured
import grails.rest.RestfulController
import grails.validation.Validateable

@Secured(['ROLE_BOULDERER'])
class BoulderingSessionController extends RestfulController {

    static responseFormats = ['html', 'json']

    BoulderingSessionController() {
        super(BoulderingSession)
    }

    def create() {
        def boulderer = getPrincipal()
        def cmd = flash.cmd ?: new BoulderingSessionCommand()
        [boulderer: boulderer, cmd: cmd]
    }


    def save(BoulderingSessionCommand cmd) {
        if (cmd.validate()) {
            BoulderingSession s = new BoulderingSession()
            s.date = cmd.date
            s.boulderer = cmd.boulderer
            s.gym = cmd.gym

            for (AscentCommand aCmd : cmd.ascents) {
                aCmd.validate()
                if (aCmd.style != AscentCommand.Style.none) {
                    Ascent a = new Ascent(boulder: aCmd.boulder, style: Ascent.Style.valueOf(aCmd.style.toString()))
                    s.addToAscents(a)
                }
            }

            s.save()

            flashHelper.confirm 'default.logged.message': [g.message(code: 'bcomp.boulderingSession.label'), s.date]
            redirect controller: 'boulderer', action: 'statistics', params: ['username': s.boulderer.username]
        } else {
            // put command object into flash scope before redirecting, making it available to the view action
            flash.cmd = cmd
            redirect action: 'create'
        }
    }
}


@Validateable
class BoulderingSessionCommand {

    static constraints = {
        gym nullable: false
        boulderer nullable: false
    }

    Date date = new Date()
    Gym gym
    User boulderer

    Set<AscentCommand> ascents

}

@Validateable
class AscentCommand {

    enum Style {
        flash, top, none;
    }

    Style style = Style.none

    Boulder boulder

    static constraints = {
        importFrom Ascent
    }
}
