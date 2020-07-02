package de.basisit

class UrlMappings {

    static mappings = {

        // Defaults
        "/"(view:"/index")
        "500"(view:'/error')
        "404"(view:'/notFound')

        // Conference endpoint for Jitsi Reservation management
        "/api/conference"(controller: 'conference', action: 'get', method: 'POST')

        // Room
        "/api/room/create"(controller: 'room', action: 'create', method: 'POST')
        "/api/room/create/permanent"(controller: 'room', action: 'createPermanent', method: 'POST')
        "/api/room/create/remote"(controller: 'room', action: 'createFromRemote', method: 'POST')

        "/api/room/edit"(controller: 'room', action: 'edit', method: 'PUT')

        "/api/room/get"(controller: 'room', action: 'get', method: 'GET')
        "/api/room/get/$id"(controller: 'room', action: 'getById', method: 'GET')
        "/api/room/getByPin/$pin"(controller: 'room', action: 'getByPin', method: 'GET')
        "/api/room/get/remote"(controller: 'room', action: 'getFromRemote', method: 'POST')
    }
}
