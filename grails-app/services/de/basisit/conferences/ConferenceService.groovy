package de.basisit.conferences

import grails.gorm.transactions.Transactional
import groovy.time.TimeCategory

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.DateTimeException
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime

@Transactional
class ConferenceService {

    /**
     * Get Info for return values here: https://github.com/jitsi/jicofo/blob/master/doc/reservation.md
     * @param Room
     * @return Map, following jicofo documentation
     */
    def get(Room room) {

        def duration
        def conferenceMap
        def start_time

        // Use DateFormat wanted by Jitsi
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX")
        // TODO: Parameterize Jitsi Timezone in application.yml
        TimeZone tz = TimeZone.getTimeZone('Europe/Berlin')
        // Application runs in UTC Timezone due to JSON conventions, Jitsi needs date with Timezone offset
        Date now = use(TimeCategory) { tz.getOffset(Instant.now().toEpochMilli()).milliseconds.from.now }

        // only check if conference already started as Jitsi returns an own error if it already ended
        if (room.type == Room.RoomType.TEMPORARY && room.openingAt.before(now)) {
            // Build duration in seconds if Room is Temporary for Jitsi
            duration = TimeCategory.minus(room.closingAt, room.openingAt).toMilliseconds() / 1000
            start_time = dateFormat.format(room.openingAt)
        } else if (room.type == Room.RoomType.PERMANENT) {
            start_time = dateFormat.format(now)
            duration = Integer.MAX_VALUE
        } else {
            throw new DateTimeException("Conference is not open yet, please come back to the time the host of the conference communicated to you.")
        }

        conferenceMap = [
                id        : room.id,
                start_time: start_time,
                duration  : duration
        ]

        return conferenceMap
    }
}
