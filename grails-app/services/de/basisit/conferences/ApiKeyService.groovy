package de.basisit.conferences

import de.basisit.conferences.ApiKey.ApiKeyType
import grails.gorm.transactions.Transactional

import javax.servlet.http.HttpServletRequest

@Transactional
class ApiKeyService {

    /**
     * Validates if a given API Key is valid.
     * Keys can be invalid if they are expired or of the wrong type
     *
     * @param HttpServletRequest incoming request with X-API-Key in Header
     * @param ApiKeyType expectedType, will be invalid if key is not of expected Type
     * @return true if valid
     */
    Boolean validate(HttpServletRequest request, ApiKeyType expectedType) {
        Boolean isValid = false
        ApiKey apiKey = ApiKey.findByKey(request.getHeader("X-API-Key"))

        // return false instead of a later occuring null pointer exception to allow exception-driven handling of invalid API Key
        if (apiKey == null) {
            return false
        }

        if (apiKey.isValid && (expectedType == apiKey.type)) {
            isValid = true
        }

        return isValid
    }
}
