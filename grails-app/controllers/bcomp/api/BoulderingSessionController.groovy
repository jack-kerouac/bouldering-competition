package bcomp.api

import bcomp.Ascent
import bcomp.BoulderingSession
import grails.rest.RestfulController
import grails.transaction.Transactional
import org.codehaus.groovy.grails.web.servlet.HttpHeaders
import org.springframework.http.HttpStatus

class BoulderingSessionController extends RestfulController {

    static responseFormats = ['json']

    BoulderingSessionController() {
        super(BoulderingSession)
    }

    private void store(BoulderingSession session) {
        // DO NOT UPDATE ASSOCIATIONS
        session.boulderer.discard()
        session.gym.discard()

        for (Ascent ascent : session.ascents) {
            // DO NOT UPDATE ASSOCIATIONS
            ascent.boulder.discard()
            // grails does not assign the session to the belongsTo backlink of an ascent
            ascent.session = session
        }

        session.save(flush: true)

        // taken from RestController
        response.addHeader(HttpHeaders.LOCATION,
                g.createLink(
                        resource: this.controllerName, action: 'show', id: session.id, absolute: true,
                        namespace: hasProperty('namespace') ? this.namespace : null))
    }

    @Transactional
    def save(BoulderingSession session) {
        if (!session.hasErrors()) {
            store(session);

            respond session, [status: HttpStatus.CREATED]
        } else {
            respond session.errors
        }
    }

    @Transactional
    def update(BoulderingSession session) {
        if (session == null) {
            notFound()
            return
        }

        session.properties = getParametersToBind()

        if (!session.hasErrors()) {
            store(session);

            respond session, [status: HttpStatus.OK]
        } else {
            respond session.errors
        }
    }

}