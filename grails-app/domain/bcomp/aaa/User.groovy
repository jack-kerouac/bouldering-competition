package bcomp.aaa

import bcomp.gym.Grade
import bcomp.gym.TentativeGrade
import grails.rest.Resource

class User {

	transient springSecurityService

	String username
	String password
	boolean enabled = true
	boolean accountExpired
	boolean accountLocked
	boolean passwordExpired

    static embedded = ['initialGrade']

    Date registrationDate

    Grade initialGrade

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
        return new TentativeGrade(mean: new Grade(this.gradeMean), variance: gradeVariance)
    }

    public void setGrade(TentativeGrade grade) {
        this.gradeMean = grade.mean.value
        this.gradeVariance = grade.variance
    }

	static constraints = {
		username blank: false, unique: true
		password blank: false
        registrationDate nullable: false
	}

	static mapping = {
		password column: '`password`'
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
