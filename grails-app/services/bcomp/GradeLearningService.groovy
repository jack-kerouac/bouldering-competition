package bcomp

import bcomp.aaa.User
import bcomp.gym.Boulder
import bcomp.gym.Grade
import grails.transaction.Transactional

@Transactional
class GradeLearningService {

    def userService

    def boulderService

    def resetCurrentUserGrades() {
        User.findAll().each { user ->
            userService.resetCurrentBouldererGrades(user)
        }
    }

    def resetCurrentBoulderGrades() {
        Boulder.findAll().each { boulder ->
            boulderService.resetCurrentBoulderGrade(boulder)
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
            session.boulderer == user
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
