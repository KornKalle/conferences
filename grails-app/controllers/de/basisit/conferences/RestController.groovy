package de.basisit.conferences

import grails.converters.JSON
import grails.validation.Validateable

class RestController {

    def apiService

    /**
     * Renders a given Object(s) as JSON data object
     *
     * @param model
     * @return JSON Objects
     */
    protected def renderJson(model) {
        def result = [
                success: true,
                data   : model
        ]
        render result as JSON
    }

    /**
     * Renders validateable Command Objects as JSON
     *
     * @param command<Object>
     * @param successHandlerAndModel
     * @return JSON Object
     */
    protected def renderJson(Validateable command, Closure successHandlerAndModel) {
        command.validate()
        def result = [
                success: !command.hasErrors(),
                data   : !command.hasErrors() ? successHandlerAndModel.call() : null,
                errors : command.hasErrors() ? apiService.formatErrors(command) : []
        ]
        render result as JSON
    }

    /**
     * Renders validateable Objects as JSON
     *
     * @param Object
     * @param successHandlerAndModel
     * @return JSON Object
     */
    protected def renderJson(Closure model, Validateable command, Closure successHandler) {
        if (command.validate()) {
            successHandler.call()
        }
        def result = [
                success: !command.hasErrors(),
                data   : model.call(),
                errors : command.hasErrors() ? apiService.formatErrors(command) : []
        ]
        render result as JSON
    }
}
