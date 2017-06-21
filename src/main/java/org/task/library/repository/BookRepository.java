package org.task.library.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;
import org.task.library.model.Book;

/**
 * Class, that holds all methods to work with book entities.
 * 
 * @author OlehZanevych
 */
public class BookRepository {
	
	private static final String QUERY = "FROM Book ORDER BY LOWER(name), LOWER(author)";
	
	private static final String FIND_BY_NAME_QUERY = "FROM Book WHERE name = :name "
			+ "ORDER BY LOWER(name), LOWER(author)";
	
	private static final String IS_EXISTED_QUERY = "SELECT TRUE FROM Book WHERE name = :name "
			+ "AND author = :author";
	
	private static final String GET_ANOTHER_CASE = "FROM Book WHERE LOWER(name) = LOWER(:name) "
			+ "AND LOWER(author) = LOWER(:author)";
	
	private static BookRepository INSTANCE;
	
	private final Session session;
	
	public static synchronized BookRepository getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new BookRepository();
		}
		return INSTANCE;
	}
	
	private BookRepository() {
		session = new Configuration().configure()
				.buildSessionFactory().openSession();
	}
	
	public Book add(final Book book) {
		session.beginTransaction();
		session.save(book);
		session.getTransaction().commit();
		return book;
	}
	
	public Book update(final Book book) {
		session.beginTransaction();
		session.merge(book);
		session.getTransaction().commit();
		return book;
	}
	
	public void remove(final Book book) {
		session.beginTransaction();
		session.remove(book);
		session.getTransaction().commit();
	}
	
	public List<Book> getAll() {
		return session.createQuery(QUERY, Book.class).list();
	}
	
	/**
	 * Searching books by matching case insensitive like conditions
	 * for books name and author.
	 * 
	 * @param parameters Appropriate parameters whose values
	 * should be substrings of the corresponding fields
	 * of the books you want.
	 * 
	 * @return List of found books.
	 */
	@SuppressWarnings("unchecked")
	public List<Book> getAll(final HashMap<String, String> parameters) {
		if (parameters.isEmpty()) {
			return getAll();
		}
		
		@SuppressWarnings("deprecation")
		Criteria criteria = session.createCriteria(Book.class)
				.addOrder(Order.asc("name").ignoreCase())
				.addOrder(Order.asc("author").ignoreCase());
		for (Entry<String, String> parameter: parameters.entrySet()) {
			criteria.add(Restrictions.ilike(parameter.getKey(), parameter.getValue(),
					MatchMode.ANYWHERE));
		}
		return criteria.list();
	}
	
	/**
	 * Checking if exist such book (with the same name and author) already.
	 * 
	 * @param book Instance of book.
	 * 
	 * @return Exists or not.
	 */
	public boolean isExisted(final Book book) {
		Query<Boolean> query = session.createQuery(IS_EXISTED_QUERY, Boolean.class);
		query.setParameter("name", book.getName());
		query.setParameter("author", book.getAuthor());
		return query.uniqueResult() == null ? false : true;
	}
	
	/**
	 * Trying to get book which has same name and author,
	 * but only in another case.
	 * 
	 * @param book Instance of book.
	 * 
	 * @return Instance of such book, if exists.
	 */
	public Book getAnotherCase(final Book book) {
		Query<Book> query = session.createQuery(GET_ANOTHER_CASE, Book.class);
		query.setParameter("name", book.getName());
		query.setParameter("author", book.getAuthor());
		return query.uniqueResult();
	}
	
	public List<Book> findByName(final String name) {
		Query<Book> query = session.createQuery(FIND_BY_NAME_QUERY, Book.class);
		query.setParameter("name", name);
		return query.list();
	}
	
	public void removeAll() {
		session.beginTransaction();
		session.createQuery("DELETE Book").executeUpdate();
		session.getTransaction().commit();
	}
	
	@Override
	protected void finalize() {
		session.close();
    }

}
