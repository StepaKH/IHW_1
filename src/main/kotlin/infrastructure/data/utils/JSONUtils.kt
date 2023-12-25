package infrastructure.data.utils

import basis.models.Film
import basis.models.Session
import basis.models.Ticket
import infrastructure.data.entities.SessionJSON
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

fun readJsonTickets(connectionString: String): MutableList<Ticket> {
    val file = File(connectionString)
    val existingSessionsJson = file.readText()

    return try {
        Json.decodeFromString<MutableList<Ticket>>(existingSessionsJson)
    } catch (e: Exception) {
        mutableListOf()
    }
}
fun readJsonFilms(connectionString: String): MutableList<Film> {
    val file = File(connectionString)
    val existingSessionsJson = file.readText()

    return try {
        Json.decodeFromString<MutableList<Film>>(existingSessionsJson)
    } catch (e: Exception) {
        mutableListOf()
    }
}
fun readJsonSessions(connectionString: String): MutableList<SessionJSON> {
    val file = File(connectionString)
    val existingSessionsJson = file.readText()

    return try {
        Json.decodeFromString<MutableList<SessionJSON>>(existingSessionsJson)
    } catch (e: Exception) {
        mutableListOf()
    }
}

fun createSessionJSON(session: Session): SessionJSON {
    var date = session.startingHour.toString()
    if (checkIfDataEndsWithZeroZero(date)) {
        date += ":00"
    }

    val sessionJSON = SessionJSON(session.Id, date, session.seats)
    return sessionJSON
}

fun checkIfDataEndsWithZeroZero(stringDateTime: String): Boolean {
    val index = stringDateTime.indexOfFirst { it == 'T' }

    return stringDateTime.length - index - 1 == 5
}

fun clearJsonFile(filePath: String) {
    val file = File(filePath)
    file.writeText("")
}

fun writeToJsonSessions(sessions: MutableList<SessionJSON>, filePath: String) {
    val file = File(filePath)
    val json = Json { prettyPrint = true }

    val jsonArray = json.encodeToString(sessions)
    file.writeText(jsonArray)
}

fun writeToJsonFilms(films: MutableList<Film>, filePath: String) {
    val file = File(filePath)
    val json = Json { prettyPrint = true }

    val jsonArray = json.encodeToString(films)
    file.writeText(jsonArray)
}

fun writeToJsonTickets(tickets: MutableList<Ticket>, filePath: String) {
    val file = File(filePath)
    val json = Json { prettyPrint = true }

    val jsonArray = json.encodeToString(tickets)
    file.writeText(jsonArray)
}