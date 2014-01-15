package bcomp.aaa

import bcomp.gym.Grade

class User {

	transient springSecurityService

	String username
	String password
	boolean enabled = true
	boolean accountExpired
	boolean accountLocked
	boolean passwordExpired

    static embedded = ['initialGrade', 'currentGrade']

    Grade initialGrade

    Grade currentGrade
    double currentGradeVariance


	static transients = ['springSecurityService']

	static constraints = {
		username blank: false, unique: true
		password blank: false
	}

	static mapping = {
		password column: '`password`'
	}

    public User() {
        this.currentGrade = Grade.lowest()
        this.currentGradeVariance = 0
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
