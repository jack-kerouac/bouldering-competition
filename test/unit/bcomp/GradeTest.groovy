package bcomp

import bcomp.gym.Grade
import grails.test.GrailsUnitTestCase
import org.junit.Test

/**
 * User: florian
 */
class GradeTest extends GrailsUnitTestCase {

    void testFontScaleConversion() {
        int fontGrades = Grade.FONT_GRADES.size()
        double segment = 1.0 / fontGrades

        assert new Grade(0.0).toFontScale() == '1A'
        assert new Grade(0.999).toFontScale() == '8C+'

        assert new Grade(0 + segment/2).toFontScale() == '1A'

        assert Grade.fromFontScale('6A').toFontScale() == '6A'
        assert Grade.fromFontScale('6a').toFontScale() == '6A'

        assert Grade.fromFontScale('3a').toFontScale() == '3A'
        assert Grade.fromFontScale('8c+').toFontScale() == '8C+'

        assert Grade.fromFontScale('1A').value == segment/2
    }

}
