import java.util.ArrayList;
import java.util.Scanner;

class Book {
    int id;
    String title;
    String author;

    Book(int id, String title, String author) {
        this.id = id;
        this.title = title;
        this.author = author;
    }
}

public class Library {
    static ArrayList<Book> books = new ArrayList<>();
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        while(true) {
            System.out.println("\n1. Add Book\n2. View Books\n3. Search Book\n4. Delete Book\n5. Exit");
            System.out.print("Choose an option: ");
            int choice = sc.nextInt();
            sc.nextLine(); // consume newline
            switch(choice) {
                case 1: addBook(); break;
                case 2: viewBooks(); break;
                case 3: searchBook(); break;
                case 4: deleteBook(); break;
                case 5: System.exit(0);
                default: System.out.println("Invalid option!");
            }
        }
    }

    static void addBook() {
        System.out.print("Enter Book ID: ");
        int id = sc.nextInt();
        sc.nextLine();
        System.out.print("Enter Title: ");
        String title = sc.nextLine();
        System.out.print("Enter Author: ");
        String author = sc.nextLine();
        books.add(new Book(id, title, author));
        System.out.println("Book added successfully!");
    }

    static void viewBooks() {
        if(books.isEmpty()) {
            System.out.println("No books available!");
            return;
        }
        for(Book b : books) {
            System.out.println("ID: " + b.id + ", Title: " + b.title + ", Author: " + b.author);
        }
    }

    static void searchBook() {
        System.out.print("Enter Book ID to search: ");
        int id = sc.nextInt();
        for(Book b : books) {
            if(b.id == id) {
                System.out.println("Found: " + b.title + " by " + b.author);
                return;
            }
        }
        System.out.println("Book not found!");
    }

    static void deleteBook() {
        System.out.print("Enter Book ID to delete: ");
        int id = sc.nextInt();
        for(Book b : books) {
            if(b.id == id) {
                books.remove(b);
                System.out.println("Book deleted successfully!");
                return;
            }
        }
        System.out.println("Book not found!");
    }
}
