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

    static embedded = ['initialGradeRangeLow', 'initialGradeRangeHigh', 'currentGrade']

    Grade initialGradeRangeLow, initialGradeRangeHigh;

    Grade currentGrade
    double currentGradeVariance

    Date end

    public Boulder() {
        this.currentGrade = Grade.lowest()
        double currentGradeVariance = 0
        end = new Date(2050, 1, 1)
    }

    public com.google.common.collect.Range<Grade> getGradeRange() {
        return com.google.common.collect.Range.closed(initialGradeRangeLow, initialGradeRangeHigh)
    }


    public void onFloorPlan(FloorPlan floorPlan, double x, double y) {
        location = new OnFloorPlan(floorPlan: floorPlan, x: x, y: y)
        location.boulder = this
    }


    public void knownGrade(Grade grade) {
        initialGradeRangeLow = grade;
        initialGradeRangeHigh = grade;
    }

    public boolean hasKnownGrade() {
        return initialGradeRangeLow == initialGradeRangeHigh;
    }

    public void knownGradeRange(Grade rangeLow, Grade rangeHigh) {
        this.initialGradeRangeLow = rangeLow;
        this.initialGradeRangeHigh = rangeHigh;
    }

    public void unknownGrade() {
        initialGradeRangeLow = Grade.lowest()
        initialGradeRangeHigh = Grade.highest()
    }

    public boolean hasKnownRange() {
        def ret = !(initialGradeRangeLow == Grade.lowest() && initialGradeRangeHigh == Grade.highest())
        return ret && initialGradeRangeLow != initialGradeRangeHigh
    }
}
