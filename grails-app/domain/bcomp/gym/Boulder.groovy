package bcomp.gym

class Boulder {

    static constraints = {
        color nullable: false
    }

    static belongsTo = [gym: Gym]

    static hasOne = [location: Location]

    /**
     * Either the color of the holds and feet of the boulder or any other marks to distinguish this boulder from
     * others.
     */
    BoulderColor color

    static embedded = ['gradeRangeLow', 'gradeRangeHigh']

    Grade gradeRangeLow, gradeRangeHigh;

    public com.google.common.collect.Range<Grade> getGradeRange() {
        return com.google.common.collect.Range.closed(gradeRangeLow, gradeRangeHigh)
    }


    public void onFloorPlan(FloorPlan floorPlan, double x, double y) {
        location = new OnFloorPlan(floorPlan: floorPlan, x: x, y: y)
        location.boulder = this
    }

    public void knownGrade(Grade grade) {
        gradeRangeLow = grade;
        gradeRangeHigh = grade;
    }

    public void knownGradeRange(Grade rangeLow, Grade rangeHigh) {
        this.gradeRangeLow = rangeLow;
        this.gradeRangeHigh = rangeHigh;
    }

    public void unknownGrade() {
        gradeRangeLow = Grade.lowest()
        gradeRangeHigh = Grade.highest()
    }

}
