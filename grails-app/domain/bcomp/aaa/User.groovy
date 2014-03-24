package bcomp.aaa

import bcomp.gym.BoulderGrade
import bcomp.gym.TentativeGrade

class User {

	transient springSecurityService

	String email
	String password
	boolean enabled = true
	boolean accountExpired
	boolean accountLocked
	boolean passwordExpired

    static embedded = ['initialGrade']

    Date registrationDate

    BoulderGrade initialGrade

    /**
     * cannot make these fields private, otherwise ignored by GORM. Use grade property instead!
     */
    double gradeMean
    /**
     * cannot make these fields private, otherwise ignored by GORM. Use grade property instead!
     */
    double gradeVariance

    static transients = ['grade', 'springSecurityService']

    public TentativeGrade getGrade() {
        return new TentativeGrade(mean: new BoulderGrade(this.gradeMean), variance: gradeVariance)
    }

    public void setGrade(TentativeGrade grade) {
        this.gradeMean = grade.mean.value
        this.gradeVariance = grade.variance
    }

	static constraints = {
		email blank: false, unique: true
		password blank: false
        registrationDate nullable: false
	}

	static mapping = {
        table '"user"'
		password column: '"password"'
	}

    Set<Role> getAuthorities() {
		UserRole.findAllByUser(this).collect { it.role } as Set
	}

	def beforeInsert() {
		encodePassword()
	}

	def beforeUpdate() {
		if (isDirty('password')) {
			encodePassword()
		}
	}

	protected void encodePassword() {
		password = springSecurityService.encodePassword(password)
	}
}
