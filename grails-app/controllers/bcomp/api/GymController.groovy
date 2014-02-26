package bcomp.api

import bcomp.BoulderingSession
import bcomp.gym.Boulder
import bcomp.gym.Gym
import grails.plugin.springsecurity.annotation.Secured
import grails.rest.RestfulController

class GymController extends RestfulController {

    static responseFormats = ['json']

    GymController() {
        super(Gym)
    }

    def show() {
        def id = params.id
        withCacheHeaders {
            Gym gym = Gym.findById(id)

            delegate.lastModified {
                gym.lastUpdated ?: gym.dateCreated
            }
            generate {
                super.show()
            }
        }
    }

    def boulders() {
        def id = params.gymId
        withCacheHeaders {
            Gym gym = Gym.findById(id)

            if (!gym.boulders.isEmpty()) {
                delegate.lastModified {
                    gym.lastModifiedBoulderDate()
                }
            }
            generate {
                def boulders = gym.getBouldersAtDate(new Date())
                respond boulders
            }
        }
    }

    def sessions() {
        def gymId = params.gymId
        def sessions = BoulderingSession.where {
            gym.id == gymId
        }.findAll()
        respond sessions
    }

    @Override
    @Secured(['ROLE_BOULDERER'])
    def delete() {
        return super.delete()
    }

    @Secured(['ROLE_BOULDERER'])
    @Override
    def update() {
        return super.update()
    }

    @Secured(['ROLE_BOULDERER'])
    @Override
    def save() {
        return super.save()
    }
}
