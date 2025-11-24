package controller;

import k23cnt3.pdpDay07_08.entity.PdpAuthor;
import k23cnt3.pdpDay07_08.entity.PdpBook;
import k23cnt3.pdpDay07_08.service.PdpAuthorService;
import k23cnt3.pdpDay07_08.service.PdpBookService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequestMapping("/pdpbooks")
public class PdpBookController {

```
    @Autowired
    private PdpBookService pdpBookService;

    @Autowired
    private PdpAuthorService pdpAuthorService;

    // ============================
// DANH SÁCH SÁCH
// ============================
    @GetMapping
    public String getPdpBooks(Model model) {
        model.addAttribute("pdpBooks", pdpBookService.getAllPdpBooks());
        return "pdpbooks/pdp-book-list";
    }

    // ============================
// FORM THÊM SÁCH
// ============================
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("pdpBook", new PdpBook());
        model.addAttribute("pdpAuthors", pdpAuthorService.getAllPdpAuthors());
        return "pdpbooks/pdp-book-form";
    }

    // ============================
// LƯU SÁCH
// ============================
    @PostMapping("/new")
    public String createPdpBook(
            @ModelAttribute PdpBook pdpBook,
            @RequestParam List<Long> pdpAuthorIds,
            @RequestParam("imageBook") MultipartFile imageFile
    ) {

        List<PdpAuthor> authors = pdpAuthorService.findPdpAuthorsByIds(pdpAuthorIds);
        pdpBook.setPdpAuthors(authors);

        pdpBookService.savePdpBook(pdpBook);

        return "redirect:/pdpbooks";
    }
```

}
