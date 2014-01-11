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

    def resetCurrentBoulderGrades() {
        Boulder.findAll().each { boulder ->
            if (boulder.hasKnownGrade()) {
                // probability that deviance of initial grade is at most one font grade real difficulty should be
                // 90%
                // => sigma = 1.645 (see http://de.wikipedia.org/wiki/Normalverteilung)
                double variance = (1 * Grade.oneFontGradeDifference() / 1.645)**2

                boulder.currentGrade = boulder.initialGradeRangeLow;
                boulder.currentGradeVariance = variance;
            } else if (boulder.hasKnownRange()) {
                // probability that deviance of initial grade is at most one font grade real difficulty should be
                // 90%
                // => sigma = 1.645 (see http://de.wikipedia.org/wiki/Normalverteilung)
                double gradeDifference = boulder.initialGradeRangeHigh.value - boulder.initialGradeRangeLow.value
                double variance = (gradeDifference / 1.645)**2

                boulder.currentGrade = new Grade((boulder.initialGradeRangeHigh.value - boulder.initialGradeRangeLow
                        .value) / 2 + boulder.initialGradeRangeLow.value);
                boulder.currentGradeVariance = variance;
            } else {
                // => sigma = 3 (see http://en.wikipedia.org/wiki/68%E2%80%9395%E2%80%9399.7_rule)
                double gradeDifference = boulder.initialGradeRangeHigh.value - boulder.initialGradeRangeLow.value
                double variance = (gradeDifference / 3)**2

                boulder.currentGrade = new Grade((boulder.initialGradeRangeHigh.value - boulder.initialGradeRangeLow
                        .value) / 2 + boulder.initialGradeRangeLow.value);
                boulder.currentGradeVariance = variance;
            }
        }
    }

}
