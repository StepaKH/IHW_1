package application.services.realizations

import application.models.SessionInf
import application.models.SessionWithFilm
import application.answers.ResponseType
import application.services.handlers.ResponseData
import application.services.interfaces.CinemaPartService
import application.services.interfaces.TicketService
import basis.models.Ticket
import basis.interfaces.FilmRepository
import basis.interfaces.SessionRepository
import basis.interfaces.TicketRepository
import java.time.LocalDateTime

class CinemaService (
    private val ticketRepository: TicketRepository,
    private val sessionRepository: SessionRepository,
    private val filmRepository: FilmRepository) : TicketService, CinemaPartService {
        override fun getAllPlacesForSession(sessionId: Int): SessionWithFilm? {
            return sessionRepository.getAllSeatsForSession(sessionId)
        }

        override fun editTimeOfSession(sessionId: Int, newTime: LocalDateTime): String {
            val films = filmRepository.getAllFilms()

            return ResponseData(sessionRepository.changeTimeForSession(sessionId, newTime, films)).getResult()
        }

        override fun addNewSession(session: SessionInf): String {
            val films = filmRepository.getAllFilms()

            return ResponseData(sessionRepository.addSession(session, films)).getResult()
        }

        override fun editNameOfFilm(filmId: Int, newName: String): String {
            return ResponseData(filmRepository.changeNameOfFilm(filmId, newName)).getResult()
        }

        override fun editDescriptionOfFilm(filmId: Int, newDescription: String): String {
            return ResponseData(filmRepository.changeDescriptionOfFilm(filmId, newDescription)).getResult()
        }

        override fun markSeatIsOccupied(ticketId: Int): String {
            val ticket = ticketRepository.getTicketById(ticketId)
            if (ticket != null) {
                val session = sessionRepository.getSessionById(ticket.sessionId)
                val sessions = sessionRepository.getAllSessions()

                if (session == null) {
                    return ResponseData(ResponseType.SESSION_NOT_EXIST).getResult()
                }

                return ResponseData(ticketRepository.markTicketIsUsed(ticket, session, sessions)).getResult()
            }
            return ResponseData(ResponseType.TICKET_IS_NOT_EXIST).getResult()
        }

        override fun buyTicketForSession(ticket: Ticket): String {
            val ticketInDb = ticketRepository.getTicketById(ticket.id)
            if (ticketInDb != null) {
                return ResponseData(ResponseType.CANT_BOUGHT_TICKET).getResult()
            }

            val sessions = sessionRepository.getAllSessions()

            val response = ticketRepository.buyTicket(ticket, sessions)
            var ticketId = " TICKET ID =  " + response.ticketId
            if (response.ticketId == -1) {
                ticketId = ""
            }

            return ResponseData(response.responseType).getResult() + ticketId
        }

        override fun returnTicketForSession(ticketId: Int): String {
            val ticketInDb = ticketRepository.getTicketById(ticketId)

            if (ticketInDb != null) {
                val sessions = sessionRepository.getAllSessions()

                return ResponseData(ticketRepository.returnTicket(ticketInDb, sessions)).getResult()
            }
            return ResponseData(ResponseType.TICKET_IS_NOT_EXIST).getResult()
        }
}