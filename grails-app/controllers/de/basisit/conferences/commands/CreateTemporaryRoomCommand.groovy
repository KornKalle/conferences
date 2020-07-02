package de.basisit.conferences.commands

import de.basisit.conferences.Room
import grails.validation.Validateable

class CreateTemporaryRoomCommand implements Validateable {

    String jitsiRoomName
    // Set it like this to be able to reuse constraints from Domain class
    Room.RoomType type = Room.RoomType.TEMPORARY
    Date openingAt
    Date closingAt

    static constraints = {
        importFrom Room
    }
}