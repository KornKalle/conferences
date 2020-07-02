package de.basisit.conferences

import de.basisit.conferences.commands.CreatePermanentRoomCommand
import de.basisit.conferences.commands.CreateRoomRemoteCommand
import de.basisit.conferences.commands.CreateTemporaryRoomCommand
import de.basisit.conferences.commands.EditTemporaryRoomCommand
import de.basisit.conferences.commands.GetRoomRemoteCommand
import grails.plugin.springsecurity.annotation.Secured
import org.springframework.security.access.AccessDeniedException

class RoomController extends RestController {

    def apiKeyService
    def springSecurityService
    def roomService

    /**
     * Creates new Room Object
     *
     * @param CreateTemporaryRoomCommand cmd
     * @return JSON Object
     */
    @Secured('ROLE_USER')
    def create(CreateTemporaryRoomCommand cmd) {
        renderJson(cmd, { roomService.create(cmd) })
    }

    /**
     * Creates a new Room Object via API Key Auth
     *
     * @param CreateRoomRemoteCommand cmd
     * @return HTTP 200 if Key correct, 401 if key incorrect, 400 if NUllPointerException occured
     */
    @Secured('permitAll')
    def createFromRemote(CreateRoomRemoteCommand cmd) {
        if (apiKeyService.validate(request, ApiKey.ApiKeyType.RESERVATIONSYSTEM)) {
            renderJson(cmd, { roomService.createFromRemote(cmd) })
        } else {
            // ApiKey is invalid
            response.status = 401
        }
    }

    /**
     * Creates new Room Object
     *
     * @param CreatePermanentRoomCommand cmd
     * @return JSON Object
     */
    @Secured('ROLE_ADMIN')
    def createPermanent(CreatePermanentRoomCommand cmd) {
        renderJson(cmd, { roomService.createPermanent(cmd) })
    }

    /**
     * Edit existing Room Object of type temporary
     * @param EditTemporaryRoomCommand cmd
     * @return Room Object
     */
    @Secured('ROLE_USER')
    def edit(EditTemporaryRoomCommand cmd) {
        try {
            renderJson(cmd, { roomService.edit(cmd) })
        } catch (AccessDeniedException ignored) {
            response.status = 401
        }
    }

    /**
     * Get all Rooms for certain user
     * @return Room[]
     */
    @Secured('ROLE_USER')
    def get() {
        Room[] rooms = roomService.getRoomsForUid(springSecurityService.principal?.username)
        renderJson(rooms)
    }

    /**
     * Gets Room if it belongs to logged in user
     * @return Room
     */
    @Secured('ROLE_USER')
    def getById() {
        Room room = roomService.getRoomForUidById(springSecurityService.principal?.username, params.int('id'))
        if (room) {
            renderJson(room)
        } else {
            response.status = 401
        }
    }

    /**
     * Get all rooms for certain User via API Key Auth
     * @param GetRoomRemoteCommand cmd
     * @return Room[]
     */
    @Secured('permitAll')
    def getFromRemote(GetRoomRemoteCommand cmd) {
        if (apiKeyService.validate(request, ApiKey.ApiKeyType.RESERVATIONSYSTEM)) {
            Room[] rooms = roomService.getRoomsForUidFromRemote(cmd)
            renderJson(rooms)
        } else {
            // ApiKey is invalid
            response.status = 401
        }
    }

    /**
     * Returns a room object if the given PIN is correct
     * Needs an ApiKey in the RequestHeader 'ApiKey: <key>'.
     *
     * @return Status 200 + Room Object if PIN and Key correct, Status 400 if Key correct and PIN incorrect,
     * Status 401 if Key incorrect
     */
    @Secured('permitAll')
    def getByPin() {
        try {
            if (apiKeyService.validate(request, ApiKey.ApiKeyType.CALLSERVER)) {
                Room room = roomService.getRoomNameByPin(params.int('pin'))

                // room will be null if pin is wrong or expired
                if (room) {
                    renderJson(room)
                } else {
                    throw new NullPointerException()
                }
            } else {
                // ApiKey is invalid
                response.status = 401
            }
        } catch (NullPointerException ignored) {
            // Pin is wrong or expired
            response.status = 400
        }
    }
}
