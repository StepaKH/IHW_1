package infrastructure.data.functions

import application.models.SessionWithFilm
import basis.models.Film
import basis.models.Session
import infrastructure.data.entities.MergeSessionsWithFilms

class JoinFunctions {
    fun JoinSessionAndFilm(
        sessions: MutableList<Session>,
        films: MutableList<Film>): MutableList<MergeSessionsWithFilms> {
        val joinedData = mutableListOf<MergeSessionsWithFilms>()
        for (session in sessions) {
            for (film in films) {
                if (session.id == film.sessionId) {
                    joinedData.add(MergeSessionsWithFilms(session.id, session.startingHour, film.durationMinutes))
                }
            }
        }

        return joinedData
    }

    fun JoinGettingFullData(
        sessions: MutableList<Session>,
        films: MutableList<Film>): MutableList<SessionWithFilm> {

        val joinedData = mutableListOf<SessionWithFilm>()
        for (session in sessions) {
            for (film in films) {
                if (session.id == film.sessionId) {
                    joinedData.add(SessionWithFilm(
                        session.id,
                        session.startingHour,
                        session.seats,
                        film.name,
                        film.description,
                        film.rating,
                        film.durationMinutes))
                }
            }
        }
        return joinedData
    }
}