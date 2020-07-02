package de.basisit.conferences

import java.text.DateFormat
import java.text.SimpleDateFormat


class ApiInterceptor {

    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

    ApiInterceptor () {
        matchAll()
    }

    boolean before() {
        Date timestamp = new Date()
        println ("[" + dateFormat.format(timestamp).toString() + "] INGOING - Controller: $controllerName, action: $actionName, $webRequest")
        true }

    boolean after() {
        Date timestamp = new Date()
        println ("[" + dateFormat.format(timestamp).toString()  + "] OUTGOING - Controller: $controllerName, action: $actionName, $webRequest")
        true }

    void afterView() {
        // no-op
    }
}
