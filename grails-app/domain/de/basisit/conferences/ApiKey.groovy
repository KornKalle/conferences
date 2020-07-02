package de.basisit.conferences

class ApiKey {


    // Allows checking for purpose for which API Key was created to prevent abuse
    enum ApiKeyType {
        CALLSERVER,
        RESERVATIONSYSTEM
    }

    ApiKeyType type
    String key

    Date createdAt = new Date()
    Date validUntil
    String comment
    Date lastConnectedAt

    static constraints = {
        type nullable: false
        key nullable: false, blank: false, minSize: 64
        validUntil nullable: true
        comment nullable: true
        lastConnectedAt nullable: true
    }

    static mapping = {
        key column: "`key`"
    }

    Boolean getIsValid() {
        if (this.validUntil) {
            return (this.validUntil > new Date())
        } else {
            return true
        }
    }
}
