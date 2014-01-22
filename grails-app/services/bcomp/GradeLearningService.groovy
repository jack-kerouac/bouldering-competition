package bcomp

import bcomp.Ascent.Style
import bcomp.aaa.User
import bcomp.gym.Boulder
import bcomp.gym.Grade
import bcomp.gym.TentativeGrade
import grails.transaction.Transactional

@Transactional
class GradeLearningService {

    /**
     * used for testing the algorithm
     */
    def resetCurrentUserGrades() {
        User.findAll().each { user ->
            user.setGrade(calculateInitialGrade(user))
        }
    }

    /**
     * used for testing the algorithm
     */
    def resetCurrentBoulderGrades() {
        Boulder.findAll().each { boulder ->
            boulder.setGrade(calculateInitialGrade(boulder))
        }
    }

    /**
     * used for testing the algorithm
     */

    def updateCurrentUserGrades() {
        User.findAll().each { user ->
            def all = Ascent.where {
                session.boulderer == user
            }
            user.setGrade(calculateGrade(user, all))
        }
    }

    /**
     * used for testing the algorithm
     */
    def updateCurrentBoulderGrades() {
        Boulder.findAll().each { boulder_ ->
            def all = Ascent.where {
                boulder == boulder_
            }
            boulder_.setGrade(calculateGrade(boulder_, all))
        }
    }

    // probability should be 90% that personal level is at most two font grades away from personal level
    // => sigma = 1.645 (see http://de.wikipedia.org/wiki/Normalverteilung)
    public final static double VARIANCE_OF_INITIAL_GRADE = (2 * Grade.oneFontGradeDifference() / 1.645)**2

    TentativeGrade calculateInitialGrade(User user) {
        return new TentativeGrade(mean: user.initialGrade, variance: VARIANCE_OF_INITIAL_GRADE)
    }

    /**
     * Calculate the grade for the given user if only the given ascents are considered for grade calculation.
     * The initial grade of the user is taken into account as well.
     */
    // TODO: pass current user grade as parameter?
    TentativeGrade calculateGrade(User user, def ascentsToConsider) {
        // if there is only one ascent, it counts as 1 : numberOfAscentsThatInitialGradeCountsFor in the grade
        // calculation
        def numberOfAscentsThatInitialGradeCountsFor = 10

        double gradeValue = 0
        double weights = 0
        ascentsToConsider.each { Ascent ascent ->
            Boulder boulder = ascent.boulder

            // only consider this ascent if user did not flash easy boulder or user flashed harder boulder
            // TODO: use variance here as well? boulder with high variance might count as well though it is
            // harder/easier
            def didNotFlashEasyBoulder = ascent.boulder.grade.mean < user.grade.mean && ascent.style != Style.flash
            def didFlashHardBoulder = ascent.boulder.grade.mean > user.grade.mean && ascent.style == Style.flash
            if (didNotFlashEasyBoulder || didFlashHardBoulder) {
                def weight = 1 / boulder.grade.variance
                gradeValue += boulder.grade.value * weight
                weights += weight
            }
            // count current user grade otherwise
            else {
                def weight = 1 / user.grade.variance
                gradeValue += user.grade.value * weight
                weights += weight
            }
        }
        def weight = 1 / VARIANCE_OF_INITIAL_GRADE
        gradeValue += numberOfAscentsThatInitialGradeCountsFor * user.initialGrade.value * weight
        weights += numberOfAscentsThatInitialGradeCountsFor * weight

        Grade grade = new Grade(gradeValue / weights)
        // TODO: how to calculate new variance?
        double variance = user.grade.variance

        return new TentativeGrade(mean: grade, variance: variance)
    }


    TentativeGrade calculateInitialGrade(Boulder boulder) {
        Grade grade
        double variance
        if (boulder.hasAssignedGrade()) {
            grade = boulder.getAssignedGrade();
            // probability should 90% that real grade is at most one font grade away from initial grade
            // => sigma = 1.645 (see http://de.wikipedia.org/wiki/Normalverteilung)
            variance = (1 * Grade.oneFontGradeDifference() / 1.645)**2;
        } else if (boulder.hasGradeRange()) {
            grade = Grade.between(boulder.getGradeRangeLow(), boulder.getGradeRangeHigh());
            // probability should be 90% that real grade is at most one font grade outside of initial range
            // => sigma = 1.645 (see http://de.wikipedia.org/wiki/Normalverteilung)
            double diff = (boulder.getGradeRangeHigh() - boulder.getGradeRangeLow()) / 2 + Grade
                    .oneFontGradeDifference()
            variance = (diff / 1.645)**2;
        } else {
            grade = Grade.between(Grade.lowest(), Grade.highest());
            // TODO: is this a good initial sigma?
            // almost all values should be within the range of grades
            // => sigma = 3 (see http://en.wikipedia.org/wiki/68%E2%80%9395%E2%80%9399.7_rule)
            double diff = (Grade.highest() - Grade.lowest()) / 2
            variance = (diff / 3)**2;
        }
        return new TentativeGrade(mean: grade, variance: variance)
    }


    TentativeGrade calculateGrade(Boulder boulder, def ascentsToConsider) {
        // if there is only one ascent, it counts as 1 : numberOfAscentsThatCurrentGradeCountsFor in the grade
        // calculation
        def numberOfAscentsThatCurrentGradeCountsFor = 10

        double gradeValue = 0
        double weights = 0
        ascentsToConsider.each { ascent ->
            User boulderer = ascent.boulderer
            def weight = 1 / boulderer.grade.variance
            gradeValue += boulderer.grade.value * weight
            weights += weight
        }
        def weight = 1 / boulder.grade.variance
        gradeValue += numberOfAscentsThatCurrentGradeCountsFor * boulder.grade.value * weight
        weights += numberOfAscentsThatCurrentGradeCountsFor * weight

        Grade grade = new Grade(gradeValue / weights)
        // TODO: how to calculate new variance?
        double variance = boulder.grade.variance

        return new TentativeGrade(mean: grade, variance: variance)
    }

}
