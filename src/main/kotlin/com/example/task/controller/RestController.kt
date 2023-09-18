package com.example.task.controller

import com.example.task.service.PrimeSearchService
import com.example.task.dto.Response
import com.example.task.dto.ResultResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
/*import io.swagger.annotations.Api
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses*/
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
//@Api(value = "prime", description = "Rest API for prime number search", tags = ["Prime number API"])
class RestController(val primeSearchService: PrimeSearchService) {

    @Value("\${service.max.thread_number}")
    private lateinit var maxThreadNumber: Number

    @Operation(summary = "Start the prime number search", description = "Start prime number search on single or multiple " +
                                                                        "thread depends on the thread number path parameter")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "202", description = "Prime number search started"),
            ApiResponse(responseCode = "200", description = "Prime number search already started"),
            ApiResponse(responseCode = "400", description = "Thread number need to be between 1 and maxThreadNumber"),
        ]
    )
    @GetMapping("/start/{thread_number}")
    fun start(@PathVariable("thread_number") threadNumber : Int): ResponseEntity<Response> {
        val response = Response()
        return if (!primeSearchService.isRunning) {
            if (threadNumber > 0 && threadNumber <= maxThreadNumber.toInt()) {
                primeSearchService.start(threadNumber)
                response.message = "The prime number search started"
                ResponseEntity(response, HttpStatusCode.valueOf(202))
            } else {
                response.message = "Thread number need to be between 0 and $maxThreadNumber"
                ResponseEntity(response, HttpStatusCode.valueOf(400))
            }
        } else {
            response.message = "The prime number search already started"
            ResponseEntity(response, HttpStatusCode.valueOf(200))
        }
    }

    @Operation(summary = "Retrieve found prime numbers", description = "Retrieve the found prime numbers on the given interval")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successfully retrieves the prime number list"),
            ApiResponse(responseCode = "400", description = "Badly given interval for the retrieve"),
            ApiResponse(responseCode = "404", description = "The service has not yet search for the given interval"),
        ]
    )
    @GetMapping("/get_prime_interval/{start}-{end}")
    fun getPrimeInterval(@PathVariable("start") start: Int, @PathVariable("end") end: Int): ResponseEntity<ResultResponse> {
        val response = ResultResponse()
        if (start > end) {
            response.message = "The start number cannot be higher than the end number"
            return ResponseEntity(response, HttpStatusCode.valueOf(400))
        }
        if (start > primeSearchService.getLastFoundPrimeNumber()) {
            response.message = "The service has not yet searched in this interval"
            return ResponseEntity(response, HttpStatusCode.valueOf(401))
        }
        response.message = "The found prime numbers in the given interval"
        var primeNumberWithinTheInterval = primeSearchService.getPrimeNumberWithinTheInterval(start, end)
        response.resultCount = primeNumberWithinTheInterval.size
        if (primeNumberWithinTheInterval.size > 100) {
            response.result = primeNumberWithinTheInterval.subList(0, 100)
            response.nextPageNeeded = true
            response.nextStartIndex = primeNumberWithinTheInterval.get(100)
        } else {
            response.result = primeNumberWithinTheInterval
        }

        //need protection against too many hits being retrieved
        return ResponseEntity(response, HttpStatusCode.valueOf(200))
    }

    @Operation(summary = "Stop the prime number search", description = "Stop prime number search if it's already running")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Prime number search stopped"),
        ]
    )
    @GetMapping("/stop")
    fun stop(): ResponseEntity<Response> {
        val response = Response()
        primeSearchService.stop()
        response.message = "The prime number search stopped"
        return ResponseEntity(response, HttpStatusCode.valueOf(200))
    }
}