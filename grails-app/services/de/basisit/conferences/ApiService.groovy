package de.basisit.conferences

import grails.converters.JSON
import grails.gorm.transactions.Transactional

@Transactional
class ApiService {

    /**
     * formats errors of validateable object for using by api
     *
     * @param object
     * @return map
     */
    def formatErrors(object) {
        def errors = []
        object?.errors?.allErrors?.each {
            errors.add([it.field, it.codes?.last()])
        }
        return errors
    }
}
