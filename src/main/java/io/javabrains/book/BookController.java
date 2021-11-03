package io.javabrains.book;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@Controller
public class BookController {

    private static final String COVER_IMG_ROOT = "http://covers.openlibrary.org/b/id/";

    @Autowired
    BookRepository bookRepository;

    @GetMapping(value = "/books/{bookId}")
    public String getBook(@PathVariable String bookId, Model model){
        Optional<Book> optionalBook = bookRepository.findById(bookId);

        if(optionalBook.isPresent()){
            Book book = optionalBook.get();

            String coverImageUrl = "/images/no-image.png";

            if(book.getCoverIds() != null && !book.getCoverIds().isEmpty()){
                coverImageUrl = COVER_IMG_ROOT + book.getCoverIds().get(0) + "-L.jpg";
            }

            model.addAttribute("coverImage", coverImageUrl);
            model.addAttribute("book", book);
            return "book";
        }

        return "book-not-found";
    }

}
