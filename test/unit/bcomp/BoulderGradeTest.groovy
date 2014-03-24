package bcomp

import bcomp.gym.BoulderGrade
import grails.test.GrailsUnitTestCase
import org.junit.Test

/**
 * User: florian
 */
class BoulderGradeTest extends GrailsUnitTestCase {

    void testFontScaleConversion() {
        int fontGrades = BoulderGrade.FONT_GRADES.size()
        double segment = 1.0 / fontGrades

        assert new BoulderGrade(0.0).toFontScale() == '1A'
        assert new BoulderGrade(0.999).toFontScale() == '8C+'

        assert new BoulderGrade(0 + segment/2).toFontScale() == '1A'

        assert BoulderGrade.fromFontScale('6A').toFontScale() == '6A'
        assert BoulderGrade.fromFontScale('6a').toFontScale() == '6A'

        assert BoulderGrade.fromFontScale('3a').toFontScale() == '3A'
        assert BoulderGrade.fromFontScale('8c+').toFontScale() == '8C+'

        assert BoulderGrade.fromFontScale('1A').value == segment/2
    }

}
