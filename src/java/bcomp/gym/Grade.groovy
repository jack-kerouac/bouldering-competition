package bcomp.gym

class Grade implements Comparable<Grade> {

    double value;

    /** required by Hibernate */
    Grade() {}

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

    public static Grade lowest() {
        return fromFontScale(FONT_GRADES[0])
    }

    public static Grade highest() {
        return fromFontScale(FONT_GRADES[FONT_GRADES.size() - 1])
    }

    public static double oneFontGradeDifference() {
        return Grade.fromFontScale('7a+').value - Grade.fromFontScale('7a').value;
    }


    @Override
    public String toString() {
        return "${String.format('%.3f', value)} (${toFontScale()})"
    }
}
