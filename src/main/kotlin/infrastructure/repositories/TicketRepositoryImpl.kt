package infrastructure.repositories

import application.answers.BuyTicket
import application.answers.ResponseType
import basis.models.Seat
import basis.models.Session
import basis.models.Ticket
import basis.interfaces.TicketRepository
import infrastructure.data.DataContext
import infrastructure.data.utils.createSessionJSON
import java.time.LocalDateTime

class TicketRepositoryImpl(private val dataContext: DataContext) : TicketRepository {

    override fun returnTicket(ticket: Ticket, sessions: MutableList<Session>): ResponseType {
        if (ticket.isDeleted) {
            return ResponseType.TICKET_IS_NOT_EXIST
        }
        val session = sessions.find { it.id == ticket.sessionId}

        val checkingResult = checkSeatIsValidToAction(session, ticket.row, ticket.column)
        if (checkingResult != ResponseType.SUCCESS) {
            return checkingResult
        }

        try {
            if (session!!.seats[ticket.row][ticket.column] == Seat.FREE &&
                session.seats[ticket.row][ticket.column] == Seat.HERE
            ) {
                return ResponseType.CANT_RETURN
            }
        } catch (e: Exception) {
            return ResponseType.INVALID_ROW_COLUMN
        }

        session.seats[ticket.row][ticket.column] = Seat.FREE

        for (element in sessions) {
            if (element.id == session.id) {
                element.seats = session.seats
                dataContext.saveChangesSessions(sessions.map { createSessionJSON(it) }.toMutableList())
                val newTicketsGroup = deleteTicketById(ticket.id)
                dataContext.saveChangesTickets(newTicketsGroup)
                break
            }
        }

        return ResponseType.SUCCESS
    }

    override fun buyTicket(ticket: Ticket, sessions: MutableList<Session>): BuyTicket {
        val newTicketsGroup = dataContext.getAllTickets()

        val session = sessions.find { it.id == ticket.sessionId }

        val checkingResult = checkSeatIsValidToAction(session, ticket.row, ticket.column)
        if (checkingResult != ResponseType.SUCCESS) {
            return BuyTicket(checkingResult, -1)
        }

        try {
            if (session!!.seats[ticket.row][ticket.column] != Seat.FREE) {
                return BuyTicket(ResponseType.ALREADY_SOLD, -1)
            }
        } catch (e: Exception) {
            return BuyTicket(ResponseType.INVALID_ROW_COLUMN, -1)
        }

        session.seats[ticket.row][ticket.column] = Seat.SOLD

        for (element in sessions) {
            if (element.id == session.id) {
                element.seats = session.seats
                dataContext.saveChangesSessions(sessions.map { createSessionJSON(it) }.toMutableList())
                newTicketsGroup.add(Ticket(newTicketsGroup.size + 1, ticket.sessionId, ticket.row, ticket.column))
                dataContext.saveChangesTickets(newTicketsGroup)
                break
            }
        }

        return BuyTicket(ResponseType.SUCCESS, newTicketsGroup.size)
    }

    override fun markTicketIsUsed(ticket: Ticket, session: Session, sessions: MutableList<Session>): ResponseType {
        if (ticket.isDeleted) {
            return ResponseType.TICKET_IS_NOT_EXIST
        }
        val checkingResult = checkSeatIsValidToAction(session, ticket.row, ticket.column)
        if (checkingResult != ResponseType.SUCCESS) {
            return checkingResult
        }

        try {
            if (session.seats[ticket.row][ticket.column] != Seat.SOLD) {
                return ResponseType.CANT_OCCUPY
            }
        } catch (e: Exception) {
            return ResponseType.INVALID_ROW_COLUMN
        }

        session.seats[ticket.row][ticket.column] = Seat.HERE

        for (element in sessions) {
            if (element.id == session.id) {
                element.seats = session.seats
                dataContext.saveChangesSessions(sessions.map { createSessionJSON(it) }.toMutableList())
                break
            }
        }

        return ResponseType.SUCCESS
    }

    override fun getTicketById(id: Int): Ticket? {
        val ticket = dataContext.getAllTickets().find { it.id == id }
        return ticket
    }

    override fun getAllTickets(): MutableList<Ticket> {
        return dataContext.getAllTickets()
    }

    private fun deleteTicketById(id: Int): MutableList<Ticket> {
        val tickets = dataContext.getAllTickets()

        for (ticket in tickets) {
            if (ticket.id == id) {
                ticket.isDeleted = true
            }
        }

        return tickets
    }

    private fun checkSeatIsValidToAction(session: Session?, row: Int, column: Int): ResponseType {
        if (session == null) {
            return ResponseType.SESSION_NOT_EXIST
        }

        if (session.startingHour.isBefore(LocalDateTime.now())) {
            return ResponseType.TIME_GONE
        }

        return ResponseType.SUCCESS
    }
}