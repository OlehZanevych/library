# Book Library
A very simple Java project with the console interface for library managing.

## Available commands
- *add {book_author} "{book_name}"* – adding new book
- *remove {book_name}* – removing book by name
- *edit {book_name}* – editing book by name
- *all books [author "{book_author_substring}"] [name "{book_name_substring}"]* – case insensitive book searching
- *clean* – removing all books
- *exit* – stopping application execution

## How do I get setup and run?
You should do 3 simple actions:
1. Clone repository (git clone https://github.com/OlehZanevych/library.git)
2. Building project (mvn clean install)
3. Run org.task.library.main.Main as Java Application (mvn exec:java -Dexec.mainClass="org.task.library.main.Main") to start application

## P.S.
To store data, I use database under RDBMS H2. It is automatically created when the application is running for the first time and stored in the folder "data". In order to make it easy for you to understand the work of the application, I prepared an example of its execution https://github.com/OlehZanevych/library/blob/master/example.txt