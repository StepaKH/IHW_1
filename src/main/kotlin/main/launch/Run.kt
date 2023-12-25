package main.launch

import application.models.Actions
import application.models.SessionInf
import infrastructure.auth.AuthServer
import infrastructure.auth.entities.UserOptions
import infrastructure.auth.utils.activateAuthentication
import main.utils.*

fun run(app: App) {
    println("Start program...\n")

    val cinemaService = app.getCinemaService()

    val authServer = AuthServer()

    var currentSession = UserOptions("", "")

    var sessionNumber: Int
    var choice = ""

    while (choice != "10") {

        currentSession = activateAuthentication(authServer, currentSession)

        printIntroduction()

        print("Push button - ")
        choice = readln()

        if (choice == "1") {
            val ticket = getDataForSeat(app.getCinemaService(), Actions.BUY)

            if (ticket == null) {
                println("Ticket not found")
                continue
            }

            val resultFromServer = cinemaService.buyTicketForSession(ticket)

            println(resultFromServer)
        } else if (choice == "2") {
            var ticketId: Int
            try {
                print("Input id of ticket ")
                ticketId = readln().toInt()
            } catch (_: Exception) {
                println("\nInvalid ID\n")
                continue
            }

            val resultFromServer = cinemaService.returnTicketForSession(ticketId)

            println(resultFromServer)
        } else if (choice == "3") {
            sessionNumber = readSessionNumber()
            val session = cinemaService.getAllPlacesForSession(sessionNumber)

            if (session == null) {
                println("Session does not exist")
                continue
            }
            printSessionInformation(session)
        } else if (choice == "4") {
            sessionNumber = readSessionNumber()
            val data = inputDateTime()

            if (!data.isValid) {
                println("Wrong datetime")
                continue
            }

            val resultFromServer = cinemaService.editTimeOfSession(sessionNumber, data.dateTime!!)

            println(resultFromServer)
        } else if (choice == "5") {
            sessionNumber = readSessionNumber()
            print("Input new name: ")
            val text = readln()

            val resultFromServer = cinemaService.editNameOfFilm(sessionNumber, text)

            println(resultFromServer)
        } else if (choice == "6") {
            sessionNumber = readSessionNumber()
            print("Input new description: ")
            val text = readln()

            val resultFromServer = cinemaService.editDescriptionOfFilm(sessionNumber, text)

            println(resultFromServer)
        } else if (choice == "7") {
            val film = inputFilmInformation()

            if (film.rating == -1) {
                println("Invalid syntax")
                continue
            }
            val date = inputDateTime()

            if (!date.isValid) {
                println("Invalid datetime")
                continue
            }

            val response = cinemaService.addNewSession(SessionInf(film, date.dateTime!!))
            println(response)
        } else if (choice == "8") {
            var ticketId: Int
            try {
                print("Input id of ticket ")
                ticketId = readln().toInt()
            } catch (_: Exception) {
                println("\nInvalid ID\n")
                continue
            }

            println(cinemaService.markSeatIsOccupied(ticketId))
        } else if (choice == "9") {
            println("Successfully log out")
            currentSession = UserOptions("", "")
        } else if (choice == "10") {
            println("Exit....")
        } else {
            println("Command is not supported")
        }
    }
}