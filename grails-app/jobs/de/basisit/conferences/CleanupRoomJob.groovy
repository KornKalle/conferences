package de.basisit.conferences

import groovy.time.TimeCategory
import java.text.DateFormat
import java.text.SimpleDateFormat

/**
 * Job which deletes all rooms which's closingAt Date was at least one week ago
 */
class CleanupRoomJob {

    def roomService
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

    static triggers = {
        cron name:   'cronTrigger',   startDelay: 10000, cronExpression: '0 2 * * * ?'
    }

    def execute() {
        this.log("Start Job")
        Date deleteUntil = use(TimeCategory ) { 1.week.ago }
        this.log("Deleted " + roomService.deleteOutdatedRooms(deleteUntil) + " Rooms")
        this.log("End Job")
    }

    /**
     * Log job status in consistent format
     * @param String message
     */
    protected void log(String message) {
        Date timestamp = new Date()
        println ("[" + dateFormat.format(timestamp).toString() + "] CleanUpRoomJob - " + message)
    }
}
