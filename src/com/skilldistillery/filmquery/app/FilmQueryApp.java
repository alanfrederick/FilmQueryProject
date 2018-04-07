package com.skilldistillery.filmquery.app;

import java.util.List;
import java.util.Scanner;

import com.skilldistillery.filmquery.database.DatabaseAccessor;
import com.skilldistillery.filmquery.database.DatabaseAccessorObject;
import com.skilldistillery.filmquery.entities.Actor;
import com.skilldistillery.filmquery.entities.Film;

public class FilmQueryApp {

  DatabaseAccessor db = new DatabaseAccessorObject();

  public static void main(String[] args) {
    FilmQueryApp app = new FilmQueryApp();
    app.launch();
  }

  private void launch() {
    Scanner input = new Scanner(System.in);
    System.out.println("Welcome to the FQA: Film Query Application!");
    startUserInterface(input);

    input.close();
  }

  private void startUserInterface(Scanner input) {

    while (true) {
      System.out.println(
          "\nPlease select from the menu below. \n1. Get film info by ID\n2. Search film by keyword\n0. Exit Application");
      // force user to conform to menu entries
      while (!input.hasNextInt()) {
        System.out.println("Sorry, that was not a valid option.");
        System.out.println(
            "\nPlease select from the menu below. \n1. Search film by ID\n2. Search film by keyword\n0. Exit Application");
        input.next();
      }
      int userInput = input.nextInt();
      switch (userInput) {
      case 1:
        System.out.println("Please enter a movie ID: ");
        int movieID = input.nextInt();
        Film film = db.getFilmById(movieID);
        // System.out.println(film);
        if (film == null) {
          break;
        }
        System.out.println("Film title is: " + film.getTitle());
        System.out.println("Film description: " + film.getDescription());
        System.out.println("Film year: " + film.getYear());
        System.out.println("Film language is : " + film.getLanguage().getName());
        List<Actor> actor = db.getActorsByFilmId(movieID);
        System.out.println("The actors who starred in this movie are:\n");
        for (Actor actor2 : actor) {
          System.out.println(actor2.getFirstName() + " " + actor2.getLastName());
        }
        System.out.println();
        break;
      case 2:
        System.out.println("Please enter a keyword.");
        String filmKeyword = input.next();
        List<Film> film1 = db.getFilmByKeyword(filmKeyword);
        if (film1 == null) {
          break;
        }
        for (Film film2 : film1) {
          System.out.println("Film title is: " + film2.getTitle());
          System.out.println("Film description: " + film2.getDescription());
          System.out.println("Film year: " + film2.getYear());
          System.out.println("Film language is : " + film2.getLanguage().getName());
          System.out.println();
        }
        break;
      case 0:
        System.out.println("Thank you for using the FQA!");
        System.exit(0);
        //gotta have a little fun
      case 8675309:
        System.out.println("\nIt's dangerous to go alone.  Listen to this.");
        System.out.println("\n\u266BJenny I got your number\u266B \n" + "\u266BI need to make you mine\u266B\n"
            + "\u266BJenny don't change your number\u266B\n" + "\u266B867-5309, 867-5309, 867-5309, 867-5309\u266B");
        break;

      }
    }
  }

}
