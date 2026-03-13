@import java.util.List
@import org.example.springbootlabb1ms.book.dto.UpdateBookDTO

@param UpdateBookDTO book
@param Long bookId
@param List<String> errors

<h1>Edit book</h1>

@if(errors != null && !errors.isEmpty())
    <div style="color:red">
        <ul>
            @for(String error : errors)
                <li>${error}</li>
            @endfor
        </ul>
    </div>
@endif

<form action="/books/${bookId}" method="post">

    <div>
        <label for="title">Title</label>
        <input
                id="title"
                type="text"
                name="title"
                value="${book.getTitle() != null ? book.getTitle() : ""}">
    </div>

    <div>
        <label for="author">Author</label>
        <input
                id="author"
                type="text"
                name="author"
                value="${book.getAuthor() != null ? book.getAuthor() : ""}">
    </div>

    <div>
        <label for="description">Description</label>
        <textarea
                id="description"
                name="description">${book.getDescription() != null ? book.getDescription() : ""}</textarea>
    </div>

    <div>
        <label for="publisher">Publisher</label>
        <input
                id="publisher"
                type="text"
                name="publisher"
                value="${book.getPublisher() != null ? book.getPublisher() : ""}">
    </div>

    <div>
        <label for="publicationDate">Publication date</label>
        <input
                id="publicationDate"
                type="date"
                name="publicationDate"
                value="${book.getPublicationDate() != null ? book.getPublicationDate().toString() : ""}">
    </div>

    <div>
        <label for="isbn">ISBN</label>
        <input
                id="isbn"
                type="text"
                name="isbn"
                value="${book.getIsbn() != null ? book.getIsbn() : ""}">
    </div>

    <div>
        <button type="submit">Update book</button>
    </div>

</form>

<p>
    <a href="/books">Back to list</a>
</p>