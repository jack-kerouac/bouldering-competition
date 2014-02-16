package bcomp

import bcomp.start.NotificationRequest
import org.springframework.security.access.annotation.Secured

@Secured('permitAll')
class NotificationsController {
    def requestNotification(NotificationRequest request) {
        request.date = new Date()
        request.save()
        render status: 200
    }
}
