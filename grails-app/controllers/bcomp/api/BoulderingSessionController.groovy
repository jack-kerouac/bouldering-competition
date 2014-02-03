package bcomp.api

import bcomp.Ascent
import bcomp.BoulderingSession
import bcomp.aaa.User
import bcomp.gym.Boulder
import bcomp.gym.Gym
import grails.plugin.springsecurity.annotation.Secured
import grails.rest.RestfulController
import grails.validation.Validateable
import org.springframework.http.HttpStatus

@Secured(['ROLE_BOULDERER'])
class BoulderingSessionController extends RestfulController {

    static responseFormats = ['json']

    BoulderingSessionController() {
        super(BoulderingSession)
    }


    def save(BoulderingSessionCommand cmd) {
        if (!cmd.hasErrors()) {
            // DO NOT UPDATE ASSOCIATIONS
            cmd.boulderer.discard()
            cmd.gym.discard()

            BoulderingSession s = new BoulderingSession()
            s.date = cmd.date
            s.boulderer = cmd.boulderer
            s.gym = cmd.gym

            for (AscentCommand aCmd : cmd.ascents) {
                aCmd.boulder.discard()

                aCmd.validate()
                Ascent a = new Ascent(boulder: aCmd.boulder, style: aCmd.style)
                s.addToAscents(a)
            }

            s.save()

            render(contentType: "application/json", status: HttpStatus.CREATED) { s }
        } else {
            respond cmd.errors, view: '/createSession'
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