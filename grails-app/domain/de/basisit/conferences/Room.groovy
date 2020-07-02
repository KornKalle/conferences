package de.basisit.conferences

class Room {

    enum RoomType {
        PERMANENT,
        TEMPORARY
    }

    // Don't use just "name" for easier parsing at the callserver
    String jitsiRoomName
    Integer pin
    RoomType type
    Date openingAt
    Date closingAt
    Date createdAt = new Date()
    String createdByUid
    String link


    static constraints = {
        // Validate if name is unique
        jitsiRoomName nullable: false, blank: false, validator: { val, obj, errors ->
            def room = this.findByJitsiRoomNameIlike(val.replaceAll('[^a-zA-Z0-9-_]', ""))
            if (room && !obj.hasProperty("id")) {
                errors.rejectValue('jitsiRoomName', 'not unique')
                return false
            } else if (room && (room.id != obj.id)) {
                errors.rejectValue('jitsiRoomName', 'not unique')
                return false
            }
            return true
        }

        // Validate if pin is unique
        pin nullable: false, blank: false, minSize: 6, validator: { val, obj, errors ->
            def room = this.findByPin(val)
            if (room && !obj.hasProperty("id")) {
                errors.rejectValue('pin', 'not unique')
                return false
            } else if (room && (room.id != obj.id)) {
                errors.rejectValue('pin', 'not unique')
                return false
            }
            return true
        }

        // only allow openingAt to be null if type is permanent
        openingAt nullable: true, validator: { val, obj, errors ->
            if ((obj.type == RoomType.TEMPORARY) && !val) {
                errors.rejectValue('openingAt', 'can not be empty for this type')
                return false
            } else {
                return true
            }
        }

        // only allow closingAt to be null if type is permanent
        closingAt nullable: true, validator: { val, obj, errors ->
            if ((obj.type == RoomType.TEMPORARY) && !val) {
                errors.rejectValue('closingAt', 'can not be empty for this type')
                return false
            } else {
                return true
            }
        }

        type nullable: false
        createdByUid nullable: true
    }

    def setJitsiRoomName(String input) {
        this.jitsiRoomName = input.replaceAll('[^a-zA-Z0-9-_]', "")
    }
}