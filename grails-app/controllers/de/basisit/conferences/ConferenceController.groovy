package de.basisit.conferences

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured

import java.text.SimpleDateFormat
import java.time.DateTimeException

class ConferenceController {

    def conferenceService

    /**
     * Returns a JSON Object the Jitsi Reservation Management needs
     * Follow: https://github.com/jitsi/jicofo/blob/master/doc/reservation.md
     * @return JSON Object
     */
    @Secured('permitAll')
    def get() {
        def result
        try {
            def jitsiRoomName = request.parameterMap.get('name').join(', ').replaceAll('[^a-zA-Z0-9-_]', "")
            Room room = Room.findByJitsiRoomNameIlike(jitsiRoomName)
            result = conferenceService.get(room)
            render result as JSON
        } catch (NullPointerException ignored) {
            // @TODO: Clarify error message
            render(status: 403, message: "Conference does not exist, for further information check support resources") as JSON
        } catch (DateTimeException dte) {
            render(status: 403, message: dte.getMessage()) as JSON
        } catch (Exception e) {
            e.printStackTrace()
            render(status: 403, message: 'An Unknown Error Occured, please contact support, error occured at: ' + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()).toString()) as JSON
        }
    }
}
