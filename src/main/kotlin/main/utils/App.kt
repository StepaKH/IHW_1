package main.utils

import application.services.realizations.CinemaService
import basis.interfaces.FilmRepository
import basis.interfaces.SessionRepository
import basis.interfaces.TicketRepository

class App {

    private var cinemaService: CinemaService? = null
    private var ticketRepository: TicketRepository? = null
    private var sessionRepository: SessionRepository? = null
    private var filmRepository: FilmRepository? = null

    fun addCinemaService(cinemaService: CinemaService) {
        this.cinemaService = cinemaService
    }

    fun addTicketRepository(ticketRepository: TicketRepository) {
        this.ticketRepository = ticketRepository
    }

    fun addSessionRepository(sessionRepository: SessionRepository) {
        this.sessionRepository = sessionRepository
    }

    fun addFilmRepository(filmRepository: FilmRepository) {
        this.filmRepository = filmRepository
    }

    fun getCinemaService(): CinemaService {
        return cinemaService!!
    }

    fun getTicketRepository(): TicketRepository {
        return ticketRepository!!
    }

    fun getSessionRepository(): SessionRepository {
        return sessionRepository!!
    }

    fun getFilmRepository(): FilmRepository {
        return filmRepository!!
    }
}