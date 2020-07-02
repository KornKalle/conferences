package de.basisit.conferences

import de.basisit.conferences.Room.RoomType
import de.basisit.conferences.commands.CreatePermanentRoomCommand
import de.basisit.conferences.commands.CreateRoomRemoteCommand
import de.basisit.conferences.commands.CreateTemporaryRoomCommand
import de.basisit.conferences.commands.EditTemporaryRoomCommand
import de.basisit.conferences.commands.GetRoomRemoteCommand
import grails.config.Config
import grails.core.support.GrailsConfigurationAware
import grails.gorm.transactions.Transactional

import org.springframework.security.access.AccessDeniedException
import java.security.SecureRandom

@Transactional
class RoomService implements GrailsConfigurationAware {

    def springSecurityService

    String jitsiURL

    /**
     * Creates a new Room with type Temporary
     *
     * @param CreateTemporaryRoomCommand cmd
     * @return Room
     */
    def create(CreateTemporaryRoomCommand cmd) {
        Room room = this.create(cmd.jitsiRoomName, cmd.type, cmd.openingAt, cmd.closingAt)
        room.createdByUid = springSecurityService.principal?.username
        room.save()
    }

    /**
     * Creates a new Room with type Permanent
     * @param CreatePermanentRoomCommand cmd
     * @return Room
     */
    def createPermanent(CreatePermanentRoomCommand cmd) {
        Room room = this.create(cmd.jitsiRoomName, cmd.type, cmd.openingAt, cmd.closingAt)
        room.createdByUid = springSecurityService.principal?.username
        room.save()
    }

    /**
     * Creates a new Room via API Key Authentication
     * @param CreateRoomRemoteCommand cmd
     * @return Room
     */
    def createFromRemote(CreateRoomRemoteCommand cmd) {
        Room room = this.create(cmd.jitsiRoomName, cmd.type, cmd.openingAt, cmd.closingAt)
        room.createdByUid = cmd.createdByUid
        room.save()
    }

    /**
     * Edits an already existing Room
     * @param EditTemporaryRoomCommand cmd
     * @return Room
     */
    def edit(EditTemporaryRoomCommand cmd) {
        Room room = Room.get(cmd.id)
        if (room.createdByUid == springSecurityService.principal?.username) {
            room.jitsiRoomName = cmd.jitsiRoomName.replace(' ', '')
            room.type = cmd.type
            room.openingAt = cmd.openingAt
            room.closingAt = cmd.closingAt
            room.createdByUid = springSecurityService.principal?.username

            if (!room.hasErrors()) {
                room.save(flush: true)
                return room
            }

        } else {
            throw new AccessDeniedException("Violating access")
        }
    }

    /**
     * Checks if user and room matches and returns it
     * @param uid ldap uid
     * @param id Room.id
     * @return Room
     */
    def getRoomForUidById (String uid, Long id) {
        Room room = Room.get(id)
        if (room.createdByUid == uid) {
            return room
        }
    }

    /**
     * Get Rooms for an Uid via API Key
     * @param GetRoomRemoteCommand cmd
     * @return Room[]
     */
    def getRoomsForUidFromRemote(GetRoomRemoteCommand cmd) {
        Room.findAllByCreatedByUid(cmd.createdByUid)
    }

    /**
     * Get Rooms for an Uid
     * @param String uid
     * @return Room[]
     */
    def getRoomsForUid(String uid) {
        Room.findAllByCreatedByUid(uid)
    }

    /**
     * Returns a Room object for a given PIN if closingAt is greater than now
     *
     * @param pin Room pin user entered at callserver
     * @return Room
     */
    def getRoomNameByPin(Integer pin) {
        Room room = Room.findByPin(pin)

        if ((room.closingAt > new Date()) || (room.type == Room.RoomType.PERMANENT)) {
            return room
        } else {
            return null
        }
    }

    /**
     * Deletes Rooms which's closingAt is before a given date
     * @param Date deleteUntil, Date which will be matched to closingAt, lower will be delete
     * @return Integer, count of deleted Rooms
     */
    Integer deleteOutdatedRooms(Date deleteUntil) {
        Integer roomCount
        Room[] rooms = Room.findAllByClosingAtLessThan(deleteUntil)
        roomCount = rooms.size()

        rooms.each {
            it.delete(flush:true)
        }

        return roomCount
    }

    /**
     * Creates a new Room
     * @param String jitsiRoomName
     * @param Room.RoomType type
     * @param Date openingAt
     * @param Date closingAt
     * @return Room
     */
    def protected create(String jitsiRoomName, RoomType type, Date openingAt, Date closingAt) {
        // create Room Object
        Room room = new Room()
        room.jitsiRoomName = jitsiRoomName
        room.pin = generatePin()
        room.type = type
        room.openingAt = openingAt
        room.closingAt = closingAt
        room.link = this.jitsiURL + '/' + room.jitsiRoomName

        return room
    }

    /**
     * Generates a Unique 6 digit pin by using SecureRandom and checking if pin already exists
     * @return Integer, 6 digit
     */
    protected static Integer generatePin() {
        // Generate Unique Pin
        int pin = 0
        Boolean pinUnique = false
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG")

        while (!pinUnique) {
            pin = sr.nextInt(900000) + 100000
            if (!Room.findByPin(pin)) {
                pinUnique = true
            }
        }
        return pin
    }

    @Override
    void setConfiguration(Config config) {
        jitsiURL = config.getProperty('jitsi.url')
    }
}
