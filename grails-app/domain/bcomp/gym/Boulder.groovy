package bcomp.gym

class Boulder {

    enum GradeCertainty {
        UNKNOWN, RANGE, ASSIGNED;
    }

    static constraints = {
        color nullable: false
        description nullable: true
    }

    static belongsTo = [gym: Gym]

    static hasOne = [location: Location]

    /**
     * Either the color of the holds and feet of the boulder or any other marks to distinguish this boulder from
     * others.
     */
    BoulderColor color

    static embedded = ['initialGradeRangeLow', 'initialGradeRangeHigh', 'currentGrade']

    GradeCertainty initialGradeCertainty;
    Grade initialGradeRangeLow, initialGradeRangeHigh;

    Grade currentGrade
    double currentGradeVariance

    Date end

    String description

    public Boulder() {
        this.initialGradeRangeLow = Grade.zero();
        this.initialGradeRangeHigh = Grade.zero();

        this.currentGrade = Grade.zero()
        this.currentGradeVariance = 0
        end = new Date(2050, 1, 1)
    }

    public void onFloorPlan(FloorPlan floorPlan, double x, double y) {
        location = new OnFloorPlan(floorPlan: floorPlan, x: x, y: y)
        location.boulder = this
    }


    public void assignedGrade(Grade grade) {
        initialGradeCertainty = GradeCertainty.ASSIGNED;
        initialGradeRangeLow = grade;
        initialGradeRangeHigh = Grade.zero();
    }

    public boolean hasAssignedGrade() {
        return initialGradeCertainty == GradeCertainty.ASSIGNED;

    }

    public Grade getAssignedGrade() {
        assert hasAssignedGrade();
        return initialGradeRangeLow;
    }

    public void gradeRange(Grade rangeLow, Grade rangeHigh) {
        initialGradeCertainty = GradeCertainty.RANGE;
        initialGradeRangeLow = rangeLow;
        initialGradeRangeHigh = rangeHigh;
    }

    public boolean hasGradeRange() {
        return initialGradeCertainty == GradeCertainty.RANGE;
    }

    public Grade getGradeRangeLow() {
        assert hasGradeRange()
        return initialGradeRangeLow;
    }

    public Grade getGradeRangeHigh() {
        assert hasGradeRange()
        return initialGradeRangeHigh;
    }

    public void unknownGrade() {
        initialGradeCertainty = GradeCertainty.UNKNOWN;
        initialGradeRangeLow = Grade.lowest()
        initialGradeRangeHigh = Grade.highest()
    }

    public boolean hasUnknownGrade() {
        return initialGradeCertainty == GradeCertainty.UNKNOWN;
    }
}
