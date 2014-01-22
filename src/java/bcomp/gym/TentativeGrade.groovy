package bcomp.gym

class TentativeGrade {

    Grade mean
    double variance

    TentativeGrade() {
        this.mean = Grade.zero();
        this.variance = 0
    }

    /**
     * convenience method to get value of mean
     */
    public double getValue() {
        return mean.value
    }

    public double getSigma() {
        return Math.sqrt(this.variance)
    }

}
