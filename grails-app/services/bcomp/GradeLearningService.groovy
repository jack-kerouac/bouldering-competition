package bcomp

import bcomp.aaa.User
import bcomp.gym.Boulder
import bcomp.gym.Grade
import grails.transaction.Transactional

@Transactional
class GradeLearningService {

    def resetCurrentUserGrades() {
        // probability should be 90% that personal level is at most two font grades away from personal level
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
                boulder.currentGrade = boulder.initialGradeRangeLow;
                // probability should 90% that real grade is at most one font grade away from initial grade
                // => sigma = 1.645 (see http://de.wikipedia.org/wiki/Normalverteilung)
                boulder.currentGradeVariance = (1 * Grade.oneFontGradeDifference() / 1.645)**2;
            } else if (boulder.hasKnownRange()) {
                boulder.currentGrade = Grade.between(boulder.initialGradeRangeLow, boulder.initialGradeRangeHigh);
                // probability should be 90% that real grade is at most one font grade outside of initial range
                // => sigma = 1.645 (see http://de.wikipedia.org/wiki/Normalverteilung)
                double diff = (boulder.initialGradeRangeHigh - boulder.initialGradeRangeLow) / 2 + Grade.oneFontGradeDifference()
                boulder.currentGradeVariance = (diff / 1.645)**2;
            } else {
                boulder.currentGrade = Grade.between(boulder.initialGradeRangeLow, boulder.initialGradeRangeHigh);
                // TODO: is this a good initial sigma?
                // almost all values should be within the range of grades
                // => sigma = 3 (see http://en.wikipedia.org/wiki/68%E2%80%9395%E2%80%9399.7_rule)
                double diff = (boulder.initialGradeRangeHigh - boulder.initialGradeRangeLow) / 2
                boulder.currentGradeVariance = (diff / 3)**2;
            }
        }
    }

    def updateCurrentUserGrades() {
        User.findAll().each { user ->
            updateCurrentGrade(user)
        }
    }

    private void updateCurrentGrade(User user) {
        // if there is only one ascent, it counts as 1 : numberOfAscentsThatCurrentGradeCountsFor in the grade
        // calculation
        def numberOfAscentsThatCurrentGradeCountsFor = 10

        def ascents = Ascent.where {
            boulderer == user
        }
        double gradeValue = 0
        double weights = 0
        ascents.each { ascent ->
            Boulder boulder = ascent.boulder
            def weight = 1 / boulder.currentGradeVariance
            gradeValue += boulder.currentGrade.value * weight
            weights += weight
        }
        def weight = 1 / user.currentGradeVariance
        gradeValue += numberOfAscentsThatCurrentGradeCountsFor * user.currentGrade.value * weight
        weights += numberOfAscentsThatCurrentGradeCountsFor * weight

        user.currentGrade = new Grade(gradeValue / weights)
    }

    def updateCurrentBoulderGrades() {
        Boulder.findAll().each { boulder ->
            updateCurrentGrade(boulder)
        }
    }

    private void updateCurrentGrade(Boulder boulder_) {
        // if there is only one ascent, it counts as 1 : numberOfAscentsThatCurrentGradeCountsFor in the grade
        // calculation
        def numberOfAscentsThatCurrentGradeCountsFor = 10

        def ascents = Ascent.where {
            boulder == boulder_
        }
        double gradeValue = 0
        double weights = 0
        ascents.each { ascent ->
            User boulderer = ascent.boulderer
            def weight = 1 / boulderer.currentGradeVariance
            gradeValue += boulderer.currentGrade.value * weight
            weights += weight
        }
        def weight = 1 / boulder_.currentGradeVariance
        gradeValue += numberOfAscentsThatCurrentGradeCountsFor * boulder_.currentGrade.value * weight
        weights += numberOfAscentsThatCurrentGradeCountsFor * weight

        boulder_.currentGrade = new Grade(gradeValue / weights)
    }

}
