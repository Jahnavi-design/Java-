import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.*;
import java.net.InetSocketAddress;
import java.util.ArrayList;

class Book {
    String id, title, author;
    Book(String id, String title, String author) {
        this.id = id;
        this.title = title;
        this.author = author;
    }
}

public class LibraryServer {
    static ArrayList<Book> books = new ArrayList<>();

    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/", new HomeHandler());
        server.createContext("/add", new AddHandler());
        server.setExecutor(null);
        System.out.println("Server started at http://localhost:8000");
        server.start();
    }

    static class HomeHandler implements HttpHandler {
        public void handle(HttpExchange t) throws IOException {
            t.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
            StringBuilder response = new StringBuilder();
            response.append("<html><head><title>Library</title>");
            response.append("<style>");
            response.append("body {"
                    + "background-image: url('https://images.unsplash.com/photo-1512820790803-83ca734da794?auto=format&fit=crop&w=1350&q=80');"
                    + "background-size: cover;"
                    + "font-family: Arial, sans-serif;"
                    + "display: flex;"
                    + "justify-content: center;"
                    + "align-items: center;"
                    + "flex-direction: column;"
                    + "color: #fff;"
                    + "min-height: 100vh;"
                    + "text-align: center;"
                    + "}");
            response.append("table {border-collapse: collapse; margin-top: 20px; width: 70%; background: rgba(0,0,0,0.7);}");
            response.append("th, td {padding: 10px; border: 1px solid #fff;}");
            response.append("th {background-color: rgba(255,255,255,0.2);}");
            response.append("a {text-decoration: none; color: #ffd700; font-weight: bold;}");
            response.append("h1 {text-shadow: 2px 2px 4px #000;}"); 
            response.append("</style></head><body>");
            response.append("<h1>My Library</h1>");
            response.append("<p>Welcome! Here are your books:</p>");
            response.append("<a href='/add'>Add New Book</a>");
            response.append("<table><tr><th>ID</th><th>Title</th><th>Author</th><th>Action</th></tr>");
            for (Book b : books) {
                response.append("<tr><td>").append(b.id).append("</td><td>")
                        .append(b.title).append("</td><td>")
                        .append(b.author).append("</td><td>")
                        .append("<a href='/delete/").append(b.id).append("'>Delete</a></td></tr>");
            }
            response.append("</table>");
            response.append("</body></html>");

            t.sendResponseHeaders(200, response.toString().getBytes("UTF-8").length);
            OutputStream os = t.getResponseBody();
            os.write(response.toString().getBytes("UTF-8"));
            os.close();
        }
    }

    static class AddHandler implements HttpHandler {
        public void handle(HttpExchange t) throws IOException {
            t.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
            if ("GET".equals(t.getRequestMethod())) {
                String html = "<html><head><title>Add Book</title><style>"
                        + "body {"
                        + "background-image: url('https://images.unsplash.com/photo-1524995997946-a1c2e315a42f?auto=format&fit=crop&w=1350&q=80');"
                        + "background-size: cover;"
                        + "display: flex;"
                        + "justify-content: center;"
                        + "align-items: center;"
                        + "flex-direction: column;"
                        + "color: #fff;"
                        + "font-family: Arial, sans-serif;"
                        + "min-height: 100vh;"
                        + "text-align: center;"
                        + "}"
                        + "form {background: rgba(0,0,0,0.7); padding: 20px; border-radius: 10px;}"
                        + "input {margin: 10px; padding: 5px; border-radius: 5px; border: none;}"
                        + "button {padding: 8px 15px; border: none; border-radius: 5px; background: #ffd700; cursor: pointer;}"
                        + "h1 {text-shadow: 2px 2px 4px #000;}"
                        + "a {color: #ffd700; text-decoration: none; margin-top: 10px; display: block;}"
                        + "</style></head><body>"
                        + "<h1>Add a New Book</h1>"
                        + "<form method='POST'>"
                        + "ID: <input name='id' required><br>"
                        + "Title: <input name='title' required><br>"
                        + "Author: <input name='author' required><br>"
                        + "<button type='submit'>Add Book</button>"
                        + "</form>"
                        + "<a href='/'>Back to Library</a>"
                        + "</body></html>";
                t.sendResponseHeaders(200, html.getBytes("UTF-8").length);
                OutputStream os = t.getResponseBody();
                os.write(html.getBytes("UTF-8"));
                os.close();
            } else if ("POST".equals(t.getRequestMethod())) {
                InputStreamReader isr = new InputStreamReader(t.getRequestBody(), "utf-8");
                BufferedReader br = new BufferedReader(isr);
                String formData = br.readLine();
                String[] pairs = formData.split("&");
                String id="", title="", author="";
                for(String pair : pairs) {
                    String[] kv = pair.split("=");
                    if(kv[0].equals("id")) id = kv[1];
                    else if(kv[0].equals("title")) title = kv[1].replace("+"," ");
                    else if(kv[0].equals("author")) author = kv[1].replace("+"," ");
                }
                books.add(new Book(id, title, author));
                t.getResponseHeaders().add("Location", "/");
                t.sendResponseHeaders(302, -1);
            }
        }
    }
}
