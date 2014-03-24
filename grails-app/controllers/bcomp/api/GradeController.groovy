package bcomp.api

import bcomp.gym.BoulderGrade


class GradeController {

    static responseFormats = ['json']

    def show() {
        cache shared: true, neverExpires: true

        respond BoulderGrade.FONT_GRADES.collect { BoulderGrade.fromFontScale(it) }
    }

}
