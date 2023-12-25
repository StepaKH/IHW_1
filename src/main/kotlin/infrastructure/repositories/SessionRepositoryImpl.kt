package infrastructure.repositories

import application.models.SessionInf
import application.models.SessionWithFilm
import application.answers.ResponseType
import basis.models.Film
import basis.models.Seat
import basis.models.Session
import basis.interfaces.SessionRepository
import infrastructure.data.DataContext
import infrastructure.data.functions.JoinFunctions
import infrastructure.data.utils.createSessionJSON
import infrastructure.data.utils.numberPlaces
import java.time.LocalDateTime

class SessionRepositoryImpl(private val dataContext: DataContext) : SessionRepository {
    override fun getAllSeatsForSession(id: Int): SessionWithFilm? {
        val joinedData = JoinFunctions()
            .JoinGettingFullData(
                dataContext.getAllSessions(),
                dataContext.getAllFilms()
            )
        return joinedData.find { it.id == id }
    }

    override fun addSession(session: SessionInf, storageFilms: MutableList<Film>): ResponseType {
        val start = session.startingHour
        val end = session.startingHour.plusMinutes(session.film.durationMinutes.toLong())

        val storageSessions = dataContext.getAllSessions()
        val joinedData = JoinFunctions().JoinSessionAndFilm(storageSessions, storageFilms)

        for (currentSession in joinedData) {
            if (isNewIntervalInterceptSmt(
                    currentSession.startingHour,
                    currentSession.startingHour.plusMinutes(currentSession.durationMinutes.toLong()),
                    start, end)) {
                return ResponseType.NEW_INTERVAL_INTERCEPT_OLD_ONE
            }
        }

        val newSession = Session(storageSessions.size + 1, session.startingHour,
            MutableList(numberPlaces) { MutableList(numberPlaces) { Seat.FREE } })
        val newFilm = Film(
            storageFilms.size + 1, newSession.id, session.film.name, session.film.description,
            session.film.rating, session.film.durationMinutes
        )
        storageSessions.add(newSession)
        storageFilms.add(newFilm)
        dataContext.saveChangesSessions(storageSessions.map { createSessionJSON(it) }.toMutableList())
        dataContext.saveChangesFilms(storageFilms)
        return ResponseType.SESSION_ADDED
    }

    override fun changeTimeForSession(sessionId: Int, newTime: LocalDateTime,  storageFilms: MutableList<Film>): ResponseType {
        val storageSessions = dataContext.getAllSessions()

        val joinedData = JoinFunctions().JoinSessionAndFilm(storageSessions, storageFilms)

        val element = joinedData.find { it.sessionId == sessionId }

        if (element == null) {
            return ResponseType.SESSION_NOT_EXIST
        }

        val start = element.startingHour
        val end = newTime.plusMinutes(element.durationMinutes.toLong())


        for (currentSession in joinedData) {
            if (isNewIntervalInterceptSmt(
                    currentSession.startingHour,
                    currentSession.startingHour.plusMinutes(currentSession.durationMinutes.toLong()),
                    start, end
                )
            ) {
                return ResponseType.NEW_INTERVAL_INTERCEPT_OLD_ONE
            }
        }

        for (item in storageSessions) {
            if (item.id == element.sessionId) {
                item.startingHour = newTime
                dataContext.saveChangesSessions(storageSessions.map { createSessionJSON(it) }.toMutableList())
                break
            }
        }

        return ResponseType.SUCCESS
    }

    override fun getSessionById(id: Int): Session? {
        return dataContext.getAllSessions().find { it.id == id }
    }

    override fun getAllSessions(): MutableList<Session> {
        return dataContext.getAllSessions()
    }

    private fun isNewIntervalInterceptSmt(
        sessionStart: LocalDateTime, sessionEnd: LocalDateTime?,
        start: LocalDateTime, end: LocalDateTime?
    ): Boolean {
        return (sessionStart >= start && sessionStart <= end) ||
                (sessionEnd!! >= start && sessionEnd <= end) ||
                (sessionStart < start && sessionEnd > end)
    }
}