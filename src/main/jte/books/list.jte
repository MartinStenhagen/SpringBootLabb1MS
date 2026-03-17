@import java.util.List
@import org.example.springbootlabb1ms.book.dto.BookDTO
@import org.springframework.data.domain.Page


@param List<BookDTO> books
@param String title
@param String author
@param Integer currentPage
@param Page<BookDTO> bookPage

<h1>Books</h1>

<p>
    <a href="/books/new">Add new book</a>
</p>

<form action="/books" method="get">
    <div>
        <label for="title">Title</label>
        <input id="title" type="text" name="title" value="${title != null ? title : ""}">
    </div>

    <div>
        <label for="author">Author</label>
        <input id="author" type="text" name="author" value="${author != null ? author : ""}">
    </div>

    <div>
        <button type="submit">Search</button>
    </div>
</form>

@if(books == null || books.isEmpty())
    <p>No books found.</p>
@else
    <table>
        <thead>
        <tr>
            <th>Title</th>
            <th>Author</th>
            <th>Publisher</th>
            <th>Publication date</th>
            <th>ISBN</th>
            <th>Actions</th>
        </tr>
        </thead>

        <tbody>
        @for(BookDTO book : books)
            <tr>
                <td>${book.getTitle()}</td>
                <td>${book.getAuthor()}</td>
                <td>${book.getPublisher()}</td>
                <td>${book.getPublicationDate() != null ? book.getPublicationDate().toString() : ""}</td>
                <td>${book.getIsbn()}</td>
                <td>
                    <a href="/books/${book.getId()}/edit">Edit</a>

                    <form action="/books/${book.getId()}/delete" method="post" style="display:inline;">
                        <button type="submit">Delete</button>
                    </form>
                </td>
            </tr>
        @endfor
        </tbody>
    </table>
    <div>
        @if(!bookPage.isFirst())
            <a href="/books?page=${currentPage - 1}&title=${title != null ? title : ""}&author=${author != null ? author : ""}">Previous</a>
        @endif

        <span>Page ${currentPage + 1} of ${bookPage.getTotalPages()}</span>

        @if(!bookPage.isLast())
            <a href="/books?page=${currentPage + 1}&title=${title != null ? title : ""}&author=${author != null ? author : ""}">Next</a>
        @endif
    </div>
@endif