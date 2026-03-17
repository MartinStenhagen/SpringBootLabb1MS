@import java.util.List
@import org.example.springbootlabb1ms.book.dto.CreateBookDTO

@param CreateBookDTO book
@param List<String> errors

@template.layout.layout(
title = "Create book",
content = @`
    <h1>Create book</h1>

    @if(errors != null && !errors.isEmpty())
        <div class="error-box">
            <ul>
                @for(String error : errors)
                    <li>${error}</li>
                @endfor
            </ul>
        </div>
    @endif

    <form class="book-form" action="/books" method="post">
        <div class="form-group">
            <label for="title">Title</label>
            <input id="title" type="text" name="title" value="${book.getTitle() != null ? book.getTitle() : ""}">
        </div>

        <div class="form-group">
            <label for="author">Author</label>
            <input id="author" type="text" name="author" value="${book.getAuthor() != null ? book.getAuthor() : ""}">
        </div>

        <div class="form-group">
            <label for="description">Description</label>
            <textarea id="description" name="description">${book.getDescription() != null ? book.getDescription() : ""}</textarea>
        </div>

        <div class="form-group">
            <label for="publisher">Publisher</label>
            <input id="publisher" type="text" name="publisher" value="${book.getPublisher() != null ? book.getPublisher() : ""}">
        </div>

        <div class="form-group">
            <label for="publicationDate">Publication date</label>
            <input id="publicationDate" type="date" name="publicationDate"
                   value="${book.getPublicationDate() != null ? book.getPublicationDate().toString() : ""}">
        </div>

        <div class="form-group">
            <label for="isbn">ISBN</label>
            <input id="isbn" type="text" name="isbn" value="${book.getIsbn() != null ? book.getIsbn() : ""}">
        </div>

        <div class="form-actions">
            <button type="submit" class="btn btn-primary">Create book</button>
            <a href="/books" class="btn btn-secondary">Back</a>
        </div>

    </form>
`
)