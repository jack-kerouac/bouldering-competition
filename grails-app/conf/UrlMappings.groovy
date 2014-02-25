class UrlMappings {

    static mappings = {
        "/$controller/$action?/$id?(.$format)?" {
            constraints {
                // apply constraints here
            }
        }


        // OLD

        "/leaderboard"(controller: 'leaderboard', action: 'view')


        // TEMPLATES

        "/"(view: '/index')
        "/home"(view: '/home')
        "/createBoulders"(view: '/createBoulders')
        "/createSession"(view: '/createSession')
        "/gymOverview"(view: '/gymOverview')

        "/status"(view: '/grailsStatus')
        "/statistics"(view: '/statistics')


        "/logout"(plugin: 'spring-security-core', controller: 'logout', action: 'index')

        "500"(view: '/error')

        "/templates/boulderMeta.html"(view: '/templates/boulderMeta')

        "/notifications"(controller: 'notifications', action: 'requestNotification')

        "/loginOrRegister"(controller: 'user', action: 'loginOrRegister')
        "/loginStatus"(controller: 'user', action: 'loginStatus')


        // API

        "/rest/v1/grades"(resource: "grade")

        "/rest/v1/gyms"(resources: "gym") {
            "/boulders"(controller: 'gym', action: 'boulders')
            "/sessions"(controller: 'gym', action: 'sessions')
        }
        "/rest/v1/gyms/$gymId/floorPlans/$floorPlanId"(controller: 'gym', action: 'floorPlanImage')

        "/rest/v1/boulders"(resources: "boulder") {
            "/ascents"(controller: 'boulder', action: 'ascents')
            "/photo"(controller: 'boulder') {
                action = [GET: "showPhoto", PUT: "savePhoto", DELETE: "deletePhoto"]
            }
        }

        "/rest/v1/users"(resources: "user") {
            "/statistics"(controller: 'user', action: 'statistics')
        }
        "/rest/v1/sessions"(resources: 'boulderingSession')
    }
}
