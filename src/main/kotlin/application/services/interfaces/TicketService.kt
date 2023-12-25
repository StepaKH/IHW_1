package application.services.interfaces

import basis.models.Ticket

interface TicketService {
    fun buyTicketForSession(ticket: Ticket): String
    fun returnTicketForSession(ticketId: Int): String
    fun markSeatIsOccupied(ticketId: Int): String
}