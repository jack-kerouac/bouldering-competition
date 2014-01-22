package bcomp

import bcomp.aaa.Role
import bcomp.aaa.User
import bcomp.aaa.UserRole
import bcomp.gym.TentativeGrade

class BouldererService {

    public final static String BOULDERER_AUTHORITY = "ROLE_BOULDERER"

    def gradeLearningService

    /**
     * Registers a new user with the boulderer role.
     */
    public void registerBoulderer(User user) {
        user.registrationDate = new Date()

        TentativeGrade initial = gradeLearningService.calculateInitialGrade(user)
        user.setGrade(initial)

        user.save()

        Role bouldererRole = Role.findByAuthority(BOULDERER_AUTHORITY);
        UserRole.create(user, bouldererRole)
    }

    public def getSessionStatistics(User boulderer_) {
        def sessions = BoulderingSession.where {
            boulderer == boulderer_
        }.order('date', 'asc')

        def ascents = [] as Set
        def stats = []
        sessions.each { BoulderingSession session ->
            // incrementally add new ascents
            ascents.addAll(session.ascents)
            def s = [:]
            s['session'] = session
            s['grade'] = gradeLearningService.calculateGrade(boulderer_, ascents)
            stats << s
        }

        return stats
    }

}
