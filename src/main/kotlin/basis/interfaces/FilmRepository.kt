package basis.interfaces

import application.answers.ResponseType
import basis.models.Film

interface FilmRepository {
    fun changeNameOfFilm(filmId: Int, newName: String): ResponseType
    fun changeDescriptionOfFilm(filmId: Int, newDescription: String): ResponseType
    fun addFilm(film: Film)
    fun getAllFilms() : MutableList<Film>
    fun getFilmById(id: Int) : Film?
}