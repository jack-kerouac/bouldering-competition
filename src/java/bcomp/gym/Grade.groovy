package bcomp.gym

class Grade implements Comparable<Grade> {

    final double value;

    public Grade(double value) {
        assert 0.0 <= value && value < 1.0
        this.value = value
    }

    @Override
    int compareTo(Grade o) {
        return this.value<=>o.value
    }

    final static def FONT_GRADES = ['3', '4-', '4', '4+', '5', '5+', '6A', '6A+', '6B', '6B+', '6C', '6C+',
            '7A', '7A+', '7B', '7B+', '7C', '7C+', '8A', '8A+', '8B', '8B+', '8C', '8C+']

    public String toFontScale() {
        double segment = 1.0 / FONT_GRADES.size()
        return FONT_GRADES[(int)Math.floor(value / segment)]
    }

    public static Grade fromFontScale(String font) {
        int i = FONT_GRADES.indexOf(font.toUpperCase())
        assert i != -1
        double segment = 1.0 / FONT_GRADES.size()

        return new Grade(i * segment + (segment / 2))
    }

}
