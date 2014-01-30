package bcomp

import bcomp.aaa.User
import bcomp.gym.Boulder
import bcomp.gym.Gym
import grails.plugin.springsecurity.annotation.Secured
import grails.rest.RestfulController
import grails.validation.Validateable
import org.springframework.http.HttpStatus

@Secured(['ROLE_BOULDERER'])
class BoulderingSessionController extends RestfulController {

    static responseFormats = ['json', 'html']

    BoulderingSessionController() {
        super(BoulderingSession)
    }

    def create() {
        def boulderer = getPrincipal()
        def cmd = flash.cmd ?: new BoulderingSessionCommand()
        [boulderer: boulderer, cmd: cmd]
    }


    def save(BoulderingSessionCommand cmd) {
        if (!cmd.hasErrors()) {
            BoulderingSession s = new BoulderingSession()
            s.date = cmd.date
            s.boulderer = cmd.boulderer
            s.gym = cmd.gym

            for (AscentCommand aCmd : cmd.ascents) {
                aCmd.validate()
                Ascent a = new Ascent(boulder: aCmd.boulder, style: aCmd.style)
                s.addToAscents(a)
            }

            s.save()

            request.withFormat {
                html {
                    flashHelper.confirm 'default.logged.message': [g.message(code: 'bcomp.boulderingSession.label'), s.date]
                    redirect controller: 'boulderer', action: 'statistics', params: ['username': s.boulderer.username]
                }
                'json' {
                    render(contentType: "application/json", status: HttpStatus.CREATED) { s }
                }
            }
        } else {
            respond cmd.errors, view: 'create'
        }
    }
}


@Validateable
class BoulderingSessionCommand {

    static constraints = {
        gym nullable: false
        boulderer nullable: false
        ascents nullable: false, minSize: 1
    }

    Date date = new Date()
    Gym gym
    User boulderer

    Set<AscentCommand> ascents

}


@Validateable
class AscentCommand {

    static constraints = {
        importFrom Ascent
    }

    Ascent.Style style

    Boulder boulder

}