package bcomp.api

import bcomp.gym.Grade


class GradeController {

    static responseFormats = ['json']

    def show() {
        cache shared: true, neverExpires: true

        respond Grade.FONT_GRADES.collect { Grade.fromFontScale(it) }
    }

}
