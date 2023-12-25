package main.utils

import application.models.Actions
import application.models.FilmInf
import application.models.SessionWithFilm
import application.answers.TimeResponse
import application.services.interfaces.CinemaPartService
import basis.models.Ticket
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

fun printIntroduction() {
    println()
    println("Choose options:")
    println("1) Buy ticket for cinema")
    println("2) Return ticket")
    println("3) Look free and sold seats in cinema")
    println("4) Edit options of session in cinema (change time)")
    println("5) Change name of film")
    println("6) Change description of film")
    println("7) Add new session")
    println("8) Mark that the place is occupied by a visitor")
    println("9) Logout")
    println("10) Exit")
    println()
}

fun readSessionNumber(): Int {
    try {
        print("input number of session: ")
        return readln().toInt()
    } catch (_: Exception) {}
    return 0
}

fun printSessionInformation(currentSession: SessionWithFilm) {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

    println("Session ID = ${currentSession.id}\n" +
            "Film name = ${currentSession.name}\n" +
            "Description = ${currentSession.description}\n" +
            "Rating = ${currentSession.rating}\n" +
            "Duration = ${currentSession.durationMinutes}\n" +
            "Datetime = ${currentSession.startingHour.format(formatter)}\n")


    for (item in currentSession.seats) {
        item.forEach { print("$it ") }
        println()
    }
}

fun getDataForSeat(cinemaService: CinemaPartService, action: Actions): Ticket? {
    val sessionNumber = readSessionNumber()

    val session = cinemaService.getAllPlacesForSession(sessionNumber)
    if (session == null) {
        println("Session not found")
        return null
    }
    val id = 0
    printSessionInformation(session)

    try {
        print("Input row of seat what you want to ${action.toString().lowercase()} ")
        val x = readln().toInt()
        print("Input column of seat what you want to ${action.toString().lowercase()} ")
        val y = readln().toInt()

        return Ticket(id, session.id, x - 1, y - 1)
    } catch (_: Exception) { }

    return null
}

fun inputDateTime(): TimeResponse {
    val scanner = Scanner(System.`in`)
    print("Input date in format yyyy-MM-dd HH:mm:ss: ")
    val inputDateTime = scanner.nextLine()
    val response = TimeResponse(true)

    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    try {
        val dateTime = LocalDateTime.parse(inputDateTime, formatter)
        response.dateTime = dateTime
    } catch (e: Exception) {
        response.isValid = false
    }
    return response
}

fun inputFilmInformation(): FilmInf {
    print("Input name of film: ")
    val name = readln()

    print("Input description of film: ")
    val description = readln()

    val rating: Int
    val duration: Int

    try {
        print("Input rating of film: ")
        rating = readln().toUInt().toInt()

        print("Input duration of film: ")
        duration = readln().toUInt().toInt()
    } catch (e: Exception) {
        return FilmInf("", "", -1, -1)
    }

    return FilmInf(name, description, rating, duration)
}