package application.services.interfaces

import application.models.SessionInf
import application.models.SessionWithFilm
import java.time.LocalDateTime

interface CinemaPartService {
    fun getAllPlacesForSession(sessionId: Int): SessionWithFilm?
    fun editTimeOfSession(sessionId: Int, newTime: LocalDateTime): String
    fun editNameOfFilm(filmId: Int, newName: String): String
    fun editDescriptionOfFilm(filmId: Int, newDescription: String): String
    fun addNewSession(session: SessionInf): String
}