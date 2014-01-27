modules = {
    flot {
        dependsOn 'jquery'
        resource url: 'js/vendor/jquery.flot.js'
        resource url: 'js/vendor/jquery.flot.time.js'
    }
    foundation {
        // FOUNDATION AND PLUGINS
        resource url: 'js/vendor/custom.modernizr.js', disposition: 'head'
        resource url: 'js/foundation/foundation.js'
        resource url: 'js/foundation/foundation.topbar.js'

        resource url: 'css/normalize.css'
        resource url: 'css/foundation.min.css'
        resource url: 'css/foundation-icons.css'
    }
    angular {
        dependsOn 'moment'
        resource url: 'js/vendor/angular.js'
        resource url: 'js/vendor/angular-resource.js'
        resource url: 'js/vendor/angular-moment.min.js'
    }
    leaflet {
        // LEAFLET AND PLUGINS
        resource url: 'js/vendor/leaflet-src.js'
        resource url: 'css/leaflet.css'
    }
    moment {
        resource url: 'js/vendor/moment-with-langs.js'
    }
    underscore {
        resource url: 'js/vendor/underscore.js'
    }
    application {
        dependsOn 'jquery', 'foundation', 'angular', 'leaflet', 'flot', 'moment', 'underscore'
        resource url: 'js/application.js'
        resource url: 'js/services.js'
        resource url: 'js/controllers.js'
        resource url: 'js/directives.js'

        resource url: 'css/application.css'
    }
}