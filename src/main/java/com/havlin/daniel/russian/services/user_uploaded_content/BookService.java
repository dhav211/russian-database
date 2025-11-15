package com.havlin.daniel.russian.services.user_uploaded_content;

import com.havlin.daniel.russian.entities.user_uploaded_content.Book;
import com.havlin.daniel.russian.entities.user_uploaded_content.Chapter;
import com.havlin.daniel.russian.entities.user_uploaded_content.ChapterText;
import com.havlin.daniel.russian.repositories.user_uploaded_content.BookRepository;
import com.havlin.daniel.russian.repositories.user_uploaded_content.ChapterRepository;
import com.havlin.daniel.russian.repositories.user_uploaded_content.ChapterTextRepository;
import jakarta.transaction.Transactional;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

@Service
public class BookService {
    private static final Logger log = LoggerFactory.getLogger(BookService.class);
    final private BookRepository bookRepository;
    final private ChapterRepository chapterRepository;
    final private ChapterTextRepository chapterTextRepository;

    public BookService(BookRepository bookRepository, ChapterRepository chapterRepository, ChapterTextRepository chapterTextRepository) {
        this.bookRepository = bookRepository;
        this.chapterRepository = chapterRepository;
        this.chapterTextRepository = chapterTextRepository;
    }

    @Transactional
    public CreatedBook createBook(ZipFile epubFile) {
        Map<String, String> testBook = processEPUB(epubFile);
        BookDetails details = parseEpubContent(testBook);
        Book book = new Book();
        String errorMessage = "";

        if (!details.hasErrors) {
            book.setAuthor(details.author);
            book.setDescription(details.description);
            book.setTitle(details.title);
            bookRepository.save(book);
            Map<Chapter, ChapterText> chapters = new HashMap<>();

            for (ChapterDetails chapterDetails : details.chapterDetails) {
                Chapter chapter = new Chapter();
                chapter.setBook(book);
                chapter.setNumber(chapterDetails.number);
                chapter.setName(chapterDetails.title);

                ChapterText chapterText = new ChapterText();
                chapterText.setText(chapterDetails.text);

                chapters.put(chapter, chapterText);
            }
            book.setChapters(chapters.keySet());
            chapterRepository.saveAllAndFlush(chapters.keySet());

            // set the chapter texts now that we have the chapter ids
            for (Map.Entry<Chapter, ChapterText> entry : chapters.entrySet()) {
                entry.getValue().setChapterId(entry.getKey().getId());
            }

            chapterTextRepository.saveAll(chapters.values());
        } else {
            errorMessage = details.isNotRussian ? "ePUB file language not detected as Russian" : details.errorMessage;
        }

        return new CreatedBook(book, errorMessage);
    }

    /**
     * Convert an epub file into a map of strings which will be parsed later on with jsoup
     * @param epubFile The book file that we will unzip and convert into string for the program to read
     * @return A map which the key is the file address within the epub, and the value is the contents of the address
     */
    Map<String, String> processEPUB(ZipFile epubFile) {
        Map<String, String> content = new HashMap<>();
        Enumeration<? extends ZipEntry> zipEntries = epubFile.entries();

        // This will loop through every entry of the epub, which will be mostly chapters, but will also contain
        // other important data for structuring and displaying an epub
        while (zipEntries.hasMoreElements()) {
            ZipEntry entry = zipEntries.nextElement();
            if (!entry.isDirectory()) {
                StringBuilder fullContent = new StringBuilder();
                // create the stream to read the bytes of the entry
                try (InputStream inputStream = new BufferedInputStream(epubFile.getInputStream(entry))) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));

                    // the buffered reader can read each line individually, here we will add these to a string so we
                    // can work with them easier
                    String line;
                    while ((line = reader.readLine()) != null) {
                        fullContent.append(line);
                    }

                    // add the full entry as a string to the map as the value and the name of the entry which is usually
                    // the chapter number as the key
                    content.put(entry.getName(), fullContent.toString());
                } catch (IOException e) {
                    log.error(e.getMessage());
                }
            }
        }
        return content;
    }

    /**
     * A collection of methods in this class which instructs jsoup to parse the contents of the unzipped up
     * @param content An unzipped epub file from the processEPUB method
     * @return BookDetails is all the data parsed from the epub ready to created into an entity
     */
    BookDetails parseEpubContent(Map<String, String> content) {
        try {
            ContentPath contentPath = getContentPath(Jsoup.parse(
                    content.get("META-INF/container.xml"), "", org.jsoup.parser.Parser.xmlParser()));
            Document parsedContent = Jsoup.parse(content.get(contentPath.contentFilePath));
            BookDetails bookDetails = getBookDetails(parsedContent);

            if (bookDetails.isNotRussian) { // let's not waste our time with a book that isn't russian
                bookDetails.hasErrors = true;
                return bookDetails;
            }

            List<String> chaptersPaths = setChapterPaths(parsedContent);
            Document toc = Jsoup.parse(content.get(contentPath.contentFolderPath + getTableOfContentsPath(parsedContent)),
                    "", org.jsoup.parser.Parser.xmlParser());
            Map<String, ChapterDetails> chapterDetailsMap = getChapterDetails(toc, chaptersPaths);

            for (String path : chaptersPaths) {
                String chapterContent = content.get(contentPath.contentFolderPath + path);
                ChapterDetails chapterDetails = chapterDetailsMap.get(path);
                String chapterText = getChapterText(Jsoup.parse(chapterContent, "", org.jsoup.parser.Parser.xmlParser()));

                if (chapterText.isEmpty()) // let's just skip the cover, which will always be an empty string
                    continue;

                // There is a chance that the chapterDetails will be null, this is the case with author pages, copyright pages, etc
                if (chapterDetails == null) {
                    chapterDetails = ChapterDetails.NotARealChapter(chapterText);
                } else {
                    chapterDetails.text = chapterText;
                }
                bookDetails.chapterDetails.add(chapterDetails);
            }

            return bookDetails;
        } catch (NullPointerException e) {
            return BookDetails.Error(e.getMessage());
        }
    }

    /**
     * The table of contents will be able to supply us with the chapter number and title, this will be used when creating
     * the chapter entities in JPA
     * @param toc The parsed Table of Contents documents from jsoup
     * @param chapterPaths A list contain all the paths to the chapters such as 'ch1-32.xhtml'
     * @return The map will contain the chapters number and title, the key will be the path for easy access later on
     * @throws NullPointerException jsoup has a chance to not find a tag or an attribute which will throw an exception
     */
    private Map<String, ChapterDetails> getChapterDetails(Document toc, List<String> chapterPaths) throws NullPointerException{
        Map<String, ChapterDetails> chapterDetailsMap = new HashMap<>();
        Elements chapterNavPoints = toc.getElementsByTag("navPoint");

        for (Element navPoint : chapterNavPoints) {
            ChapterDetails chapterDetails = new ChapterDetails();

            // Find the title of the chapter
            Elements text = Objects.requireNonNull(navPoint.getElementsByTag("text"));
            chapterDetails.title = text.text();

            // Find the chapter number
            Attribute playOrder = Objects.requireNonNull(navPoint.attribute("playOrder"));
            chapterDetails.number = Integer.parseInt(playOrder.getValue());

            // Get the chapter source, which should be the same as on of the chapter paths
            Elements contentElements = Objects.requireNonNull(navPoint.getElementsByTag("content"));
            Element content = Objects.requireNonNull(contentElements.first());
            Attribute contentAttribute = Objects.requireNonNull(content.attribute("src"));
            String chapterSrc = Objects.requireNonNull(contentAttribute.getValue());

            // Here we will link up the chapter path with the chapter src.
            // there may exist some paths that differ slight from the source, such as an id tacked on.
            // the contain method is used to bypass this possibility
            for (String chapterPath : chapterPaths) {
                if (chapterSrc.contains(chapterPath)) {
                    chapterDetailsMap.put(chapterPath, chapterDetails);
                }
            }
        }
        return chapterDetailsMap;
    }

    /**
     * Find the content folder and package path in the container.xml file
     * @param container The container.xml is an entry point to an epub, this will help us find the content
     * @return A content path is a POJO which gives us the main content folder, and also the content file which gives us
     * all the information on the epub
     * @throws NullPointerException Jsoup has a chance of pulling a null on a tag or attribute
     */
    private ContentPath getContentPath(Document container) throws NullPointerException {
        ContentPath contentPath = new ContentPath();
        Elements rootfile = container.getElementsByTag("rootfile");
        Element first = Objects.requireNonNull(rootfile.first());
        Attribute fullPathAttribute = Objects.requireNonNull(first.attribute("full-path"));
        String key = Objects.requireNonNull(fullPathAttribute.getValue());
        StringBuilder folderPath = new StringBuilder();

        // We are just getting the name of the folder where the content package is located in
        // this will add in finding the individual chapters later on
        for (char c : key.toCharArray()) {
            folderPath.append(c);
            if (c == '/')
                break;
        }

        contentPath.contentFolderPath = folderPath.toString();
        contentPath.contentFilePath = key;

        return contentPath;
    }

    /**
     * Find the name, author, language, etc of the book in the content file.
     * @param content The main content file of the epub
     * @return A POJO of the information of the book
     * @throws NullPointerException Jsoup can pull a null value on a tag or attribute
     */
    private BookDetails getBookDetails(Document content) throws NullPointerException {
        BookDetails bookDetails = new BookDetails();

        Element metadata = Objects.requireNonNull(content.getElementsByTag("metadata").first());

        // Checks the tag name to get the data of the book
        for (Element child : metadata) {
            switch (child.tagName()) {
                case "dc:title" -> bookDetails.title = child.text();
                case "dc:creator" -> bookDetails.author = child.text();
                case "dc:description" -> bookDetails.description = child.text();
                case "dc:language" -> bookDetails.isNotRussian = !child.text().equals("ru");
            }
        }

        return bookDetails;
    }

    /**
     * Gets all the chapter paths from the content file. Chapter paths are links to the files which contain the text of
     * the chapter.
     * @param content The main content folder parsed with Jsoup
     * @return A list of all the chapter paths for the book
     * @throws NullPointerException Jsoup can pull a null value on a tag or attribute
     */
    private List<String> setChapterPaths(Document content) throws NullPointerException {
        Map<String, String> idAndPaths = new LinkedHashMap<>();

        // Get all the IDs for the chapters, these will help point to the actual chapter paths in the manifest
        // We are putting them in a map for quicker retrieval later
        Element spine = Objects.requireNonNull(content.getElementsByTag("spine").first());
        Elements itemsRef = spine.getElementsByTag("itemref");
        for (Element itemRef : itemsRef) {
            Attribute idRef = Objects.requireNonNull(itemRef.attribute("idref"));
            idAndPaths.put(idRef.getValue(), "");
        }

        // Here we are going through the actual manifest checking to see if each item's id exists in the map
        // if it does exist them we can put the chapter path in map with the correct id
        Elements items = content.getElementsByTag("item");
        for (Element item : items) {
            Attribute itemId = Objects.requireNonNull(item.attribute("id"));
            if (idAndPaths.containsKey(itemId.getValue())) {
                Attribute chapterHref = Objects.requireNonNull(item.attribute("href"));
                idAndPaths.put(itemId.getValue(), chapterHref.getValue());
            }
        }

        return new ArrayList<>(idAndPaths.values());
    }

    private String getTableOfContentsPath(Document content) throws NullPointerException {
        Element spine = Objects.requireNonNull(content.getElementsByTag("spine").first());
        Attribute tocAttribute = Objects.requireNonNull(spine.attribute("toc"));
        Element tocElement = Objects.requireNonNull(content.getElementById(tocAttribute.getValue()));
        Attribute tocHrefAttribute = Objects.requireNonNull(tocElement.attribute("href"));
        return  tocHrefAttribute.getValue();
    }

    /**
     * Gets the text from the chapter file, this will remove any html tags from the page. Will only get text nested within
     * paragraph tags
     * @param parsedChapter The chapter file parsed with jsoup
     * @return The chapter text without html tags
     */
    private String getChapterText(Document parsedChapter) {
        StringBuilder text = new StringBuilder();
        Elements paragraphs = parsedChapter.getElementsByTag("p");

        for (Element element : paragraphs) {
            text.append(element.text());
            text.append('\n');
        }

        return text.toString();
    }

    private static class ContentPath {
        static ContentPath NotFound() {
            ContentPath badPath = new ContentPath();
            badPath.contentFilePath = "";
            badPath.contentFolderPath = "";
            badPath.notFound = true;

            return badPath;
        }

        String contentFilePath;
        String contentFolderPath;
        boolean notFound;
    }

    private static class BookDetails {
        static BookDetails Error(String errorMessage) {
            BookDetails badBook = new BookDetails();
            badBook.author = "";
            badBook.description = "";
            badBook.title = "";
            badBook.isNotRussian = false;
            badBook.hasErrors = true;
            badBook.errorMessage = errorMessage;
            badBook.chapterTexts = new ArrayList<>();

            return badBook;
        }

        String author;
        String title;
        String description;
        List<String> chapterTexts = new ArrayList<>();
        List<ChapterDetails> chapterDetails = new ArrayList<>();
        boolean isNotRussian;
        boolean hasErrors;
        String errorMessage;
    }

    private static class ChapterDetails {
        // This will be like cover pages, copyright pages, titles, etc
        static ChapterDetails NotARealChapter(String text) {
            ChapterDetails notRealChapter = new ChapterDetails();

            notRealChapter.text = text;
            notRealChapter.title = "";
            notRealChapter.number = 0;

            return  notRealChapter;
        }
        String text;
        String title;
        int number;
    }
}
