package bcomp.gym

import javax.imageio.ImageIO
import java.awt.*

class Boulder extends Route {

    static mapping = {
        discriminator "boulder"
    }

    static constraints = {
        // Limit upload file size to 5MB
        photoAsJpg maxSize: 1024 * 1024 * 5, nullable: true
    }

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

    byte[] photoAsJpg;


    static transients = ['grade', 'photo']

    public Boulder() {
        this.initialGradeRangeLow = Grade.zero();
        this.initialGradeRangeHigh = Grade.zero();
    }

    public TentativeGrade getGrade() {
        return new TentativeGrade(mean: new Grade(this.gradeMean), variance: gradeVariance)
    }

    public void setGrade(TentativeGrade grade) {
        this.gradeMean = grade.mean.value
        this.gradeVariance = grade.variance
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
                return "unbekannt"
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
