package bcomp

import bcomp.aaa.Role
import bcomp.aaa.User
import bcomp.aaa.UserRole
import bcomp.gym.Boulder
import bcomp.gym.Grade

class BouldererService {

    public final static String BOULDERER_AUTHORITY = "ROLE_BOULDERER"

    /**
     * Registers a new user with the boulderer role.
     */
    public void registerBoulderer(User user) {
        user.registrationDate = new Date()

        resetCurrentBouldererGrades(user);

        user.save()

        Role bouldererRole = Role.findByAuthority(BOULDERER_AUTHORITY);
        UserRole.create(user, bouldererRole)
    }

    def resetCurrentBouldererGrades(User user) {
        // probability should be 90% that personal level is at most two font grades away from personal level
        // => sigma = 1.645 (see http://de.wikipedia.org/wiki/Normalverteilung)
        double variance = (2 * Grade.oneFontGradeDifference() / 1.645)**2

        user.currentGrade = user.initialGrade
        user.currentGradeVariance = variance
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
            s['currentGrade'] = calculateGrade(boulderer_, ascents)
            stats << s
        }

        return stats
    }

    /**
     * Calculate the grade for the given user if only the given ascents are considered for grade calculation.
     * The initial grade of the user is taken into account as well.
     */
    Grade calculateGrade(User user, def ascentsToConsider) {
        // if there is only one ascent, it counts as 1 : numberOfAscentsThatInitialGradeCountsFor in the grade
        // calculation
        def numberOfAscentsThatInitialGradeCountsFor = 10

        double gradeValue = 0
        double weights = 0
        ascentsToConsider.each { Ascent ascent ->
            Boulder boulder = ascent.boulder
            def weight = 1 / boulder.currentGradeVariance
            gradeValue += boulder.currentGrade.value * weight
            weights += weight
        }
        // TODO: what is the weight for the initial grade?
        def weight = 1 / user.currentGradeVariance
        gradeValue += numberOfAscentsThatInitialGradeCountsFor * user.initialGrade.value * weight
        weights += numberOfAscentsThatInitialGradeCountsFor * weight

        return new Grade(gradeValue / weights)
    }


}
