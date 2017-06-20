# Book Library
A very simple Java project with the console interaface for library managing.

## Available сommands
- *add {book_author} "{book_name}"* – adding new book
- *remove {book_name}* – removing book with such name
- *edit {book_name}* – editing book with such name
- *all books [author "{book_author_substring}"] [name "{book_name_substring}"]* – searching books
- *clean* – removing all books
- *exit* – stopping application execution

## How do I get setup and run?
You should do 3 simple actions:
1. Clone repository (git clone https://github.com/OlehZanevych/library.git)
2. Building project (mvn clean install)
3. Run org.task.library.main.Main as Java Application (mvn exec:java -Dexec.mainClass="org.task.library.main.Main") to start application

To store data, I use database under RDBMS H2. It is automatically created when the application is running for the first time and stored in the folder "data".