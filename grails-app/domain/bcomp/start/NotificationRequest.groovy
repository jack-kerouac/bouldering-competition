package bcomp.start

class NotificationRequest {

    static constraints = {
        email email: true
    }

    String email;
    Date date;

}
