package de.basisit.conferences.commands

import de.basisit.conferences.Room
import grails.validation.Validateable

class EditTemporaryRoomCommand implements Validateable {

    Long id
    String jitsiRoomName
    Room.RoomType type = Room.RoomType.TEMPORARY
    Date openingAt
    Date closingAt

    static constraints = {
        importFrom Room
    }
}
