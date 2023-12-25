package basis.interfaces

import application.answers.BuyTicket
import application.answers.ResponseType
import basis.models.Session
import basis.models.Ticket

interface TicketRepository {
    fun returnTicket(ticket: Ticket, sessions: MutableList<Session>): ResponseType
    fun buyTicket(ticket: Ticket, sessions: MutableList<Session>): BuyTicket
    fun markTicketIsUsed(ticket: Ticket, session: Session, sessions: MutableList<Session>): ResponseType
    fun getTicketById(id: Int) : Ticket?
    fun getAllTickets() : MutableList<Ticket>
}