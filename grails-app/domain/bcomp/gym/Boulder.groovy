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

    static embedded = ['rangeLow', 'rangeHigh']

    Grade rangeLow, rangeHigh;

    public com.google.common.collect.Range<Grade> getGradeRange() {
        return com.google.common.collect.Range.closed(rangeLow, rangeHigh)
    }


    public void onFloorPlan(FloorPlan floorPlan, double x, double y) {
        location = new OnFloorPlan(floorPlan: floorPlan, x: x, y: y)
        location.boulder = this
    }

    public void knownGrade(Grade grade) {
        rangeLow = grade;
        rangeHigh = grade;
    }

    public void knownGradeRange(Grade rangeLow, Grade rangeHigh) {
        this.rangeLow = rangeLow;
        this.rangeHigh = rangeHigh;
    }

    public void unknownGrade() {
        rangeLow = Grade.lowest()
        rangeHigh = Grade.highest()
    }

}
