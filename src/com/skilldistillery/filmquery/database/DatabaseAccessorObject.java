package com.skilldistillery.filmquery.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.skilldistillery.filmquery.entities.Actor;
import com.skilldistillery.filmquery.entities.Film;
import com.skilldistillery.filmquery.entities.Language;

public class DatabaseAccessorObject implements DatabaseAccessor {

  private static final String URL = "jdbc:mysql://localhost:3306/sdvid";
  private static final String user = "student";
  private static final String pass = "student";

  @Override
  public Film getFilmById(int filmId) {
    Film film = null;

    try {
      Connection conn = DriverManager.getConnection(URL, user, pass);
      String sql = "SELECT id, title, description, release_year, language_id, rental_duration, rental_rate, length, replacement_cost, rating, special_features FROM film WHERE id = ?";
      PreparedStatement stmt = conn.prepareStatement(sql);
      stmt.setInt(1, filmId);
      ResultSet rs = stmt.executeQuery();
      if (rs.next()) {
        film = new Film();
        film.setId(rs.getInt(1));
        film.setTitle(rs.getString(2));
        film.setDescription(rs.getString(3));
        film.setYear(rs.getInt(4));
        film.setLanguageID(rs.getInt(5));
        film.setRentalDuration(rs.getInt(6));
        film.setRentalRate(rs.getDouble(7));
        film.setFilmLength(rs.getInt(8));
        film.setReplacementCost(rs.getDouble(9));
        film.setRating(rs.getString(10));
        film.setSpecialFeatures(rs.getString(11));
        film.setLanguage(getLanguageByFilmId(rs.getInt(5)));
        film.setActors(getActorsByFilmId(filmId));
      } else {
        System.out.println("Sorry, no film(s) were found with that criteria.");
        return null;
      }
    } catch (SQLException e) {
      System.err.println("On the internet, no one knows you're a dog.");
      e.printStackTrace();
    }
    return film;
  }

  static {
    try {
      Class.forName("com.mysql.jdbc.Driver");
    } catch (ClassNotFoundException e) {
      System.err.println("Unable to load DB driver.  Exiting..");
      e.printStackTrace();
      System.exit(1);
    }
  }

  @Override
  public Actor getActorById(int actorId) {
    Actor actor = null;

    try {
      Connection conn = DriverManager.getConnection(URL, user, pass);
      String sql = "SELECT id FROM actor WHERE id = ?";
      PreparedStatement stmt = conn.prepareStatement(sql);
      stmt.setInt(1, actorId);
      ResultSet rs = stmt.executeQuery();

      if (rs.next()) {
        actor = new Actor();
        actor.setId(rs.getInt(1));

      }

    } catch (SQLException e) {
      System.err.println("On the internet, no one knows you're a dog.");
      e.printStackTrace();
    }
    return actor;
  }

  @Override
  public ArrayList<Actor> getActorsByFilmId(int filmId) {
    ArrayList<Actor> cast = new ArrayList<>();

    try {
      Connection conn = DriverManager.getConnection(URL, user, pass);
      String sql = "SELECT actor.id, first_name, last_name FROM actor JOIN film_actor ON actor.id = film_actor.actor_id JOIN film ON film_actor.film_id = film.id WHERE film.id = ?";
      PreparedStatement stmt = conn.prepareStatement(sql);
      stmt.setInt(1, filmId);
      ResultSet rs = stmt.executeQuery();
      while (rs.next()) {
        int actorID = rs.getInt(1);
        String firstName = rs.getString(2);
        String lastName = rs.getString(3);
        Actor actor = new Actor(actorID, firstName, lastName);
        cast.add(actor);
      }
      rs.close();
      stmt.close();
      conn.close();

    } catch (SQLException e) {
      e.printStackTrace();
    }
    return cast;

  }

  @Override
  public List<Film> getFilmByKeyword(String filmKeyword) {
    List<Film> films = new ArrayList<>();

    try {
      Connection conn = DriverManager.getConnection(URL, user, pass);
      String sql = "SELECT id, title, description, release_year, language_id, rental_duration, rental_rate, "
          + "length, replacement_cost, rating, special_features FROM film WHERE title LIKE ? OR description LIKE ?";
      PreparedStatement stmt = conn.prepareStatement(sql);
      stmt.setString(1, "%" + filmKeyword + "%");
      stmt.setString(2, "%" + filmKeyword + "%");
      ResultSet rs = stmt.executeQuery();
      while (rs.next()) {
        Film film = new Film();
        film.setId(rs.getInt(1));
        film.setTitle(rs.getString(2));
        film.setDescription(rs.getString(3));
        film.setYear(rs.getInt(4));
        film.setLanguageID(rs.getInt(5));
        film.setRentalDuration(rs.getInt(6));
        film.setRentalRate(rs.getDouble(7));
        film.setFilmLength(rs.getInt(8));
        film.setReplacementCost(rs.getDouble(9));
        film.setRating(rs.getString(10));
        film.setSpecialFeatures(rs.getString(11));
        film.setActors(getActorsByFilmId(rs.getInt(1)));
        film.setLanguage(getLanguageByFilmId(rs.getInt(5)));
        films.add(film);

      }
      if (films.size() == 0) {
        System.out.println("Sorry, no film(s) were found with that criteria.");
        return null;
      }
      rs.close();
      stmt.close();
      conn.close();

    } catch (SQLException e) {
      System.err.println("On the internet, no one knows you're a dog.");
      e.printStackTrace();
    }
    return films;

  }

  @Override
  public Language getLanguageByFilmId(int filmLangId) {
    Language lang = null;

    try {
      Connection conn = DriverManager.getConnection(URL, user, pass);
      String sql = "SELECT id, name FROM language WHERE id = ? ";
      PreparedStatement stmt = conn.prepareStatement(sql);
      stmt.setInt(1, filmLangId);
      ResultSet rs = stmt.executeQuery();

      if (rs.next()) {
        lang = new Language();
        lang.setId(rs.getInt(1));
        lang.setName(rs.getString(2));

      }

    } catch (SQLException e) {
      System.err.println("On the internet, no one knows you're a dog.");
      e.printStackTrace();
    }
    return lang;

  }

}
