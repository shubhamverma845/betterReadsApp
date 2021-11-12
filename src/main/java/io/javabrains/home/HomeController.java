package io.javabrains.home;

import io.javabrains.user.BooksByUser;
import io.javabrains.user.BooksByUserRepository;
import io.javabrains.userbooks.UserBooksRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.query.CassandraPageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class HomeController {

    private static final String COVER_IMG_ROOT = "http://covers.openlibrary.org/b/id/";

    @Autowired
    BooksByUserRepository booksByUserRepository;

    @GetMapping("/")
    public String home(@AuthenticationPrincipal OAuth2User principal, Model model) {

        if(principal == null || principal.getAttribute("login") == null){
            return "index";
        }

        String userId = principal.getAttribute("login");

        Slice<BooksByUser> booksSlice = booksByUserRepository.findAllById(userId, CassandraPageRequest.of(0, 100));
        List<BooksByUser> booksByUsers = booksSlice.getContent();

        booksByUsers = booksByUsers.stream().distinct().map(book -> {
            String coverImageUrl = "/images/no-image.png";
            if (book.getCoverIds() != null && !book.getCoverIds().isEmpty()) {
                coverImageUrl = COVER_IMG_ROOT + book.getCoverIds().get(0) + "-M.jpg";
            }
            book.setCoverUrl(coverImageUrl);
            return book;
        }).collect(Collectors.toList());

        model.addAttribute("books", booksByUsers);

        return "home";
    }
}
