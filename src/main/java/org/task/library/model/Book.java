package org.task.library.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Entity, that describes Book.
 * 
 * @author OlehZanevych
 */
@Entity
@Table(
		name = "t_books",
		uniqueConstraints = @UniqueConstraint(columnNames={"name", "author"})
)
public class Book {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull
	@Size(min = 1, max = 255)
	private String name;
	
	@NotNull
	@Size(min = 1, max = 255)
	private String author;

	public Book() {
		
	}

	public Book(final String author, final String name) {
		this.author = author;
		this.name = name;
	}
	
	public void setData(final Book book) {
		author = book.author;
		name = book.name;
	}

	public Long getId() {
		return id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(final String author) {
		this.author = author;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((author == null) ? 0 : author.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Book other = (Book) obj;
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		if (author == null) {
			if (other.author != null) {
				return false;
			}
		} else if (!author.equals(other.author)) {
			return false;
		}
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return String.format("%s \"%s\"", author, name);
	}

}
