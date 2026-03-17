@import java.time.LocalDate
@import java.util.List

@param String formAction
@param String submitLabel
@param String title
@param String author
@param String description
@param String publisher
@param LocalDate publicationDate
@param String isbn
@param List<String> errors

@if(errors != null && !errors.isEmpty())
    <div class="error-box">
        <ul>
            @for(String error : errors)
                <li>${error}</li>
            @endfor
        </ul>
    </div>
@endif

<form class="book-form" action="${formAction}" method="post">
    <div class="form-group">
        <label for="title">Title</label>
        <input id="title" type="text" name="title" value="${title != null ? title : ""}">
    </div>

    <div class="form-group">
        <label for="author">Author</label>
        <input id="author" type="text" name="author" value="${author != null ? author : ""}">
    </div>

    <div class="form-group">
        <label for="description">Description</label>
        <textarea id="description" name="description">${description != null ? description : ""}</textarea>
    </div>

    <div class="form-group">
        <label for="publisher">Publisher</label>
        <input id="publisher" type="text" name="publisher" value="${publisher != null ? publisher : ""}">
    </div>

    <div class="form-group">
        <label for="publicationDate">Publication date</label>
        <input
                id="publicationDate"
                type="date"
                name="publicationDate"
                value="${publicationDate != null ? publicationDate.toString() : ""}">
    </div>

    <div class="form-group">
        <label for="isbn">ISBN</label>
        <input id="isbn" type="text" name="isbn" value="${isbn != null ? isbn : ""}">
    </div>

    <div class="form-actions">
        <button type="submit">${submitLabel}</button>
    </div>
</form>