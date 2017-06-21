package org.task.library.statement;

/**
 * Class, that holds method to process User's commands
 * to work with book library.
 * 
 * @author OlehZanevych
 */
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.task.library.main.Main;
import org.task.library.model.Book;
import org.task.library.repository.BookRepository;

public class BookStatement {
	
	private static BookStatement INSTANCE;
	
	private final BookRepository bookRepository;
	
	public static synchronized BookStatement getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new BookStatement();
		}
		return INSTANCE;
	}
	
	private BookStatement() {
		bookRepository = BookRepository.getInstance();
	}
	
	/**
	 * Processing User's command.
	 * 
	 * @param line Text entered by the User.
	 */
	public void execute(final String line) {
		String normalizedLine = line.trim().replaceAll("\\s+", " ");
		String[] parts = normalizedLine.split(" ", 2);
		String command = parts[0];
		String statement = parts.length != 1 ? parts[1] : null;
		switch (command) {
			case "add":
				add(statement);
				break;
			case "remove":
				remove(statement);
				break;
			case "edit":
				edit(statement);
				break;
			case "all":
				all(statement);
				break;
			case "clean":
				clean();
				break;
			case "exit":
				exit();
				break;
			default:
				nonExistingCommand(command);
		}
	}
	
	/**
	 * Processing command to add new book.
	 * 
	 * @param statement Author and name of the book.
	 */
	protected void add(final String statement) {
		if (statement == null) {
			System.out.println("Invalid `add` command. You must enter the book author and "
					+ "its name in quotes. For example, add Karan Mahajan "
					+"\"The Association of Small Bombs\"");
			return;
		}
		
		Pattern pattern = Pattern.compile("(.+?)[\"](.+)[\"]");
		Matcher matcher = pattern.matcher(statement);
		
		if (matcher.find()) {
			Book book = new Book(matcher.group(1).trim(), matcher.group(2).trim());
			if (bookRepository.isExisted(book)) {
				System.out.println(String.format("Book %s already exists", book));
				return;
			}
			
			Book anotherCaseBook = bookRepository.getAnotherCase(book);
			if (anotherCaseBook != null) {
				System.out.println(String.format("There is a book %s which has same author and name, "
						+ "but only in another case. If you want to replace it with "
						+ "new ones, press y. Otherwise, enter any other character", anotherCaseBook));
				System.out.print("U: ");
				String line = Main.nextLine().trim();
				System.out.print("P: ");
				if (!line.isEmpty() && line.substring(0, 1).toLowerCase().equals("y")) {
					anotherCaseBook.setData(book);
					bookRepository.update(anotherCaseBook);
					System.out.println(String.format("Book %s was updated", anotherCaseBook));
				} else {
					System.out.println("Adding canceled");
				}
				return;
			}
			
			bookRepository.add(book);
			System.out.println(String.format("Book %s was added", book));
		} else {
			System.out.println("Invalid statement of `add` command. You must enter the book author and "
					+ "its name in quotes. For example, add Karan Mahajan "
					+ "\"The Association of Small Bombs\"");
		}
	}
	
	/**
	 * Processing command to remove book by name.
	 * 
	 * @param name Name of the book which will be removed.
	 */
	protected void remove(final String name) {
		if (name == null) {
			System.out.println("Invalid `remove` command. You must enter the name of the book. "
					+ "For example, remove A Little Princess");
			return;
		}
		
		List<Book> books = bookRepository.findByName(name);
		
		if (books.isEmpty()) {
			System.out.println(String.format("Book with name \"%s\" doesn't exist. Please check "
					+ "if you have entered it correct", name));
			return;
		}
		
		if (books.size() == 1) {
			Book book = books.get(0);
			bookRepository.remove(book);
			System.out.println(String.format("Book %s was removed", book));
			return;
		}
		
		int i = 1;
		System.out.println(String.format("There are %d books with name \"%s\" "
				+ "written by different authors:", books.size(), name));
		for (Book book: books) {
			System.out.println(String.format("%3s %3d. %s", "", i, book.getAuthor()));
			++i;
		}
		System.out.println("Please enter number of author whose book want to remove "
				+ "or any non digital character or 0 to cancel");
		System.out.print("U: ");
		String line = Main.nextLine().trim();
		System.out.print("P: ");
		char firstCharacter = line.charAt(0);
		if (firstCharacter < '1' || firstCharacter > '9') {
			System.out.println("Removing canceled");
			return;
		}
		Pattern pattern = Pattern.compile("(\\d+)");
		Matcher matcher = pattern.matcher(line);
		matcher.find();
		int position = Integer.parseInt(matcher.group(1));
		if (position > books.size()) {
			System.out.println(String.format("Number %d doesn't match any author. Removing canceled",
					position));
			return;
		}
		Book book = books.get(position - 1);
		bookRepository.remove(book);
		System.out.println(String.format("Book %s was removed", book));
	}
	
	/**
	 * Processing command to edit book by name.
	 * 
	 * @param name Name of the book which will be edited.
	 */
	protected void edit(final String name) {
		if (name == null) {
			System.out.println("Invalid `edit` command. You must enter the name of the book. "
					+ "For example, edit A Little Princess");
			return;
		}
		
		List<Book> books = bookRepository.findByName(name);
		
		if (books.isEmpty()) {
			System.out.println(String.format("Book with name \"%s\" doesn't exist. Please check "
					+ "if you have entered it correct", name));
			return;
		}
		
		if (books.size() == 1) {
			editBook(books.get(0));
			return;
		}
		
		int i = 1;
		System.out.println(String.format("There are %d books with name \"%s\" "
				+ "written by different authors:", books.size(), name));
		for (Book book: books) {
			System.out.println(String.format("%3s %3d. %s", "", i, book.getAuthor()));
			++i;
		}
		System.out.println("Please enter number of author whose book want to edit "
				+ "or any non digital character or 0 to cancel");
		System.out.print("U: ");
		String line = Main.nextLine().trim();
		System.out.print("P: ");
		char firstCharacter = line.charAt(0);
		if (firstCharacter < '1' || firstCharacter > '9') {
			System.out.println("Editing canceled");
			return;
		}
		Pattern numericalPattern = Pattern.compile("(\\d+)");
		Matcher numericalMatcher = numericalPattern.matcher(line);
		numericalMatcher.find();
		int position = Integer.parseInt(numericalMatcher.group(1));
		if (position > books.size()) {
			System.out.println(String.format("Number %d doesn't match any author. Editing canceled",
					position));
			return;
		}
		editBook(books.get(position - 1));
	}
	
	/**
	 * Processing command to search books.
	 * 
	 * @param statement Statement of searching books command.
	 */
	protected void all(final String statement) {
		if (statement == null) {
			System.out.println("Please use `all books` command. Simple command `all` "
					+ "doesn't exist ( ͡° ͜ʖ");
			return;
		}
		
		String[] parts = statement.split(" ", 2);
		if (!parts[0].equals("books")) {
			System.out.println(String.format("Command `all %s` doesn't exist. Maybe "
					+ "you wanted to write `all books`?", parts[0]));
			return;
		}
		List<Book> books;
		HashMap<String, String> parameters = null;
		if (parts.length == 1) {
			books = bookRepository.getAll();
			
		} else {
			Pattern pattern = Pattern.compile("(name|author)[\\s][\"](.+?)[\"]");
			Matcher matcher = pattern.matcher(parts[1]);
			parameters = new HashMap<>();
			while (matcher.find()) {
				String parameterName = matcher.group(1);
				if (parameters.containsKey(parameterName)) {
					System.out.println(String.format("Invalid search entry. Parameter "
							+ "`%s` assignments twice", parameterName));
					return;
				} else {
					parameters.put(parameterName, matcher.group(2));
				}
			}
			books = bookRepository.getAll(parameters);
		}
		boolean isSearch = parameters != null && !parameters.isEmpty();
		if (books.isEmpty()) {
			if (isSearch) {
				System.out.println("No books found :((");
			} else {
				System.out.println("Pity to say, but we do not have any books :((");
			}
		} else {
			if (isSearch) {
				System.out.println("Found books: ");
			} else {
				System.out.println("Our books: ");
			}
			for (Book book: books) {
				System.out.println(String.format("%5s %10s", "", book));
			}
		}
	}
	
	/**
	 * Removing all books.
	 */
	protected void clean() {
		bookRepository.removeAll();
		System.out.println("All books removed :((");
	}
	
	/**
	 * Exiting application.
	 */
	protected void exit() {
		Main.exit();
		System.out.println("Goodbye :))");
	}
	
	/**
	 * Processing of a non existing command.
	 * 
	 * @param command Non existing command name.
	 */
	protected void nonExistingCommand(final String command) {
		if (command.isEmpty()) {
			System.out.println("Please don't write empty commands ( ͡° ͜ʖ ͡°)");
		} else {
			System.out.println(String.format("Сommand `%s` doesn't exist. "
					+ "Please write carefully (¬‿¬)", command));
		}
	}
	
	/**
	 * Editing book.
	 * 
	 * @param book Instance of book which will be edited.
	 */
	protected void editBook(final Book book) {
		System.out.println(String.format("Please enter new author and name of the book %s", book));
		System.out.print("U: ");
		String statement = Main.nextLine().trim().replaceAll("\\s+", " ");
		System.out.print("P: ");
		
		Pattern pattern = Pattern.compile("(.+?)[\"](.+)[\"]");
		Matcher matcher = pattern.matcher(statement);
		
		if (matcher.find()) {
			Book newBook = new Book(matcher.group(1).trim(), matcher.group(2).trim());
			if (bookRepository.isExisted(newBook)) {
				System.out.println(String.format("Book %s already exists. Editing canceled", newBook));
				return;
			}
			
			Book anotherCaseBook = bookRepository.getAnotherCase(newBook);
			if (anotherCaseBook != null) {
				System.out.println(String.format("There is a book %s which has same author and name, "
						+ "but only in another case. If you want to replace it with "
						+ "new ones, press y. Otherwise, to cancel the operation enter any other "
						+ "character", anotherCaseBook));
				System.out.print("U: ");
				String line = Main.nextLine().trim();
				System.out.print("P: ");
				if (!line.isEmpty() && line.substring(0, 1).toLowerCase().equals("y")) {
					anotherCaseBook.setData(newBook);
					bookRepository.update(anotherCaseBook);
					System.out.println(String.format("Book %s was updated", anotherCaseBook));
				} else {
					System.out.println("Editing canceled");
				}
				return;
			}
			
			book.setData(newBook);
			bookRepository.update(book);
			System.out.println(String.format("Book %s was updated", book));
		} else {
			System.out.println("Invalid statement. You must enter the book author and its name "
					+ "in quotes. For example, Karan Mahajan \"The Association of Small Bombs\". "
					+ "Editing canceled");
		}
	}

}
