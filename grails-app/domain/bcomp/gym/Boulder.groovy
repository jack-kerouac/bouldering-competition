package bcomp.gym

import javax.imageio.ImageIO
import java.awt.*

class Boulder {

    enum GradeCertainty {
        UNKNOWN, RANGE, ASSIGNED;
    }

    static mapping = {
        end column: '"end"'
    }

    static constraints = {
        foreignId nullable: true, unique: 'gym'

        color nullable: false
        description nullable: true
        end nullable: true
        // Limit upload file size to 5MB
        photoAsJpg maxSize: 1024 * 1024 * 5, nullable: true
    }

    static belongsTo = [gym: Gym]

    static hasOne = [location: Location]


    Long foreignId

    /**
     * Either the color of the holds and feet of the boulder or any other marks to distinguish this boulder from
     * others.
     */
    BoulderColor color

    static embedded = ['initialGradeRangeLow', 'initialGradeRangeHigh']

    GradeCertainty initialGradeCertainty;
    Grade initialGradeRangeLow, initialGradeRangeHigh;

    /**
     * cannot make these fields private, otherwise ignored by GORM. Use grade property instead!
     */
    double gradeMean
    /**
     * cannot make these fields private, otherwise ignored by GORM. Use grade property instead!
     */
    double gradeVariance

    Date dateCreated
    Date lastUpdated

    static transients = ['grade', 'photo']

    public TentativeGrade getGrade() {
        return new TentativeGrade(mean: new Grade(this.gradeMean), variance: gradeVariance)
    }

    public void setGrade(TentativeGrade grade) {
        this.gradeMean = grade.mean.value
        this.gradeVariance = grade.variance
    }


    Date end

    String description

    byte[] photoAsJpg;


    public Boulder() {
        this.initialGradeRangeLow = Grade.zero();
        this.initialGradeRangeHigh = Grade.zero();
    }

    public void removePhoto() {
        photoAsJpg = null;
    }

    public void setPhoto(Image image) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(image, "JPG", out);
        photoAsJpg = out.toByteArray();
    }

    public Image setPhotoFromInputStream(InputStream input) {
        Image image = ImageIO.read(input)
        setPhoto(image)
    }

    public boolean hasPhoto() {
        return photoAsJpg != null;
    }

    public Image getPhoto() {
        return ImageIO.read(getPhotoAsInputStream())
    }

    public InputStream getPhotoAsInputStream() {
        return new ByteArrayInputStream(photoAsJpg)
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

    public String getInitialGrade() {
        switch (initialGradeCertainty) {
            case GradeCertainty.ASSIGNED:
                return getAssignedGrade().toFontScale()
            case GradeCertainty.RANGE:
                return getGradeRangeLow().toFontScale() + " – " + getGradeRangeHigh().toFontScale()
            case GradeCertainty.UNKNOWN:
                return "?"
        }
    }

    public boolean hasUnknownGrade() {
        return initialGradeCertainty == GradeCertainty.UNKNOWN;
    }

    boolean equals(o) {
        if (this.is(o)) return true
        if (!(o instanceof Boulder)) return false

        Boulder boulder = (Boulder) o

        if (id != boulder.id) return false

        return true
    }

    int hashCode() {
        return (id != null ? id.hashCode() : 0)
    }
}
