package hska.webshop.resourcestandalone.controller;

import org.springframework.hateoas.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class BookController {
	
    @RequestMapping("/books/admin")
    public Resource<Book> booksAdmin() {
        return new Resource<>(Book.defaultBook());
    }

    @RequestMapping("/books/user")
    public Resource<Book> booksUser() {
        return new Resource<>(Book.defaultBook());
    }
    
    @RequestMapping("/books/public")
    public Resource<Book> booksPublic() {
        return new Resource<>(Book.defaultBook());
    }
    
    @RequestMapping("/movies/admin")
    public Resource<Book> moviesAdmin() {
        return new Resource<>(Book.defaultBook());
    }

    @RequestMapping("/movies/user")
    public Resource<Book> moviesUser() {
        return new Resource<>(Book.defaultBook());
    }

}

