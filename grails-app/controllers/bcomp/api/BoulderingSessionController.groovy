package bcomp.api

import bcomp.Ascent
import bcomp.BoulderingSession
import grails.rest.RestfulController
import grails.transaction.Transactional
import org.springframework.http.HttpStatus

class BoulderingSessionController extends RestfulController {

    static responseFormats = ['json']

    BoulderingSessionController() {
        super(BoulderingSession)
    }


    @Transactional
    def save(BoulderingSession session) {
        if (!session.hasErrors()) {
            // DO NOT UPDATE ASSOCIATIONS
            session.boulderer.discard()
            session.gym.discard()

            for (Ascent ascent : session.ascents) {
                ascent.boulder.discard()
                // grails does not assign the session to the belongsTo backlink of an ascent
                ascent.session = session
            }

            session.save(flush: true)

            respond session, [status: HttpStatus.CREATED]
        } else {
            respond session.errors
        }
    }
}