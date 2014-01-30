package bcomp

import bcomp.gym.Grade


class GradeController {

    static responseFormats = ['json']


    def show() {
        respond Grade.FONT_GRADES.collect { Grade.fromFontScale(it) }
    }

}
