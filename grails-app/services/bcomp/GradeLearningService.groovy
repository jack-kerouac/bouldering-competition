package bcomp

import bcomp.aaa.User
import bcomp.gym.Boulder
import bcomp.gym.Grade
import grails.transaction.Transactional

@Transactional
class GradeLearningService {

    def resetCurrentUserGrades() {
        // TODO: calculate 1.645 from 90%
        // probability that deviance of initial grade is at most two font grade from personal level should be 90%
        // => sigma = 1.645 (see http://de.wikipedia.org/wiki/Normalverteilung)
        double variance = (2 * Grade.oneFontGradeDifference() / 1.645)**2

        User.findAll().each { user ->
            user.currentGrade = user.initialGrade
            user.currentGradeVariance = variance
        }
    }

}
