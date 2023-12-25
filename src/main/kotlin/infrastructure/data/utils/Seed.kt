package infrastructure.data.utils

import basis.models.Film
import basis.models.Seat
import basis.models.Session
import infrastructure.data.DataContext
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

const val numberPlaces = 5

fun seed(dataContext: DataContext): DataContext {
    val data = readJsonSessions(dataContext.connectionString + SESSION)

    if (data.isEmpty()) {
        generateInit(dataContext)
    }

    return dataContext
}

fun generateInit(dataContext: DataContext) {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

    clearJsonFile(dataContext.connectionString + SESSION)
    clearJsonFile(dataContext.connectionString + TICKET)
    clearJsonFile(dataContext.connectionString + FILM)

    val film1 = Film(1, 1, "Spider-man", "cool film", 9, 150)
    val film2 = Film(2, 2, "Mstitely Final", "interesting fim", 12, 181)
    val film3 = Film(3, 3, "Buba", "for children", 7, 150)
    val film4 = Film(4, 4, "Sherlock Holmes", "detective", 15, 800)

    var inputDateTime = "2023-12-26 16:50:00"
    var dateTime = LocalDateTime.parse(inputDateTime, formatter)
    val session1 = Session(1, dateTime, MutableList(numberPlaces) { MutableList(numberPlaces) { Seat.FREE } })


    inputDateTime = "2023-12-25 15:20:30"
    dateTime = LocalDateTime.parse(inputDateTime, formatter)
    val session2 = Session(2, dateTime, MutableList(numberPlaces) { MutableList(numberPlaces) { Seat.FREE } })


    inputDateTime = "2023-12-27 17:00:00"
    dateTime = LocalDateTime.parse(inputDateTime, formatter)
    val session3 = Session(3, dateTime, MutableList(numberPlaces) { MutableList(numberPlaces) { Seat.FREE } })


    inputDateTime = "2023-12-29 21:00:12"
    dateTime = LocalDateTime.parse(inputDateTime, formatter)
    val session4 = Session(4, dateTime, MutableList(numberPlaces) { MutableList(numberPlaces) { Seat.FREE } })

    val films = mutableListOf(film1, film2, film3, film4)
    val sessions = mutableListOf(session1, session2, session3, session4)
        .map { createSessionJSON(it) }.toMutableList()
    dataContext.saveChangesFilms(films)
    dataContext.saveChangesSessions(sessions)
}