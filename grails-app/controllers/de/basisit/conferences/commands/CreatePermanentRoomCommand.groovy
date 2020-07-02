package de.basisit.conferences.commands

import de.basisit.conferences.Room
import grails.validation.Validateable

class CreatePermanentRoomCommand implements Validateable {

    String jitsiRoomName
    // Set it like this to be able to reuse constraints from Domain class
    Room.RoomType type = Room.RoomType.PERMANENT
    Date openingAt = null
    Date closingAt = null

    static constraints = {
        importFrom Room
    }
}