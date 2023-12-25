package basis.interfaces

import application.models.SessionInf
import application.models.SessionWithFilm
import application.answers.ResponseType
import basis.models.Film
import basis.models.Session
import java.time.LocalDateTime

interface SessionRepository {
    fun getAllSeatsForSession(id: Int): SessionWithFilm?
    fun addSession(session: SessionInf,  storageFilms: MutableList<Film>): ResponseType
    fun changeTimeForSession(sessionId: Int, newTime: LocalDateTime, storageFilms: MutableList<Film>): ResponseType
    fun getSessionById(id: Int) : Session?
    fun getAllSessions() : MutableList<Session>
}