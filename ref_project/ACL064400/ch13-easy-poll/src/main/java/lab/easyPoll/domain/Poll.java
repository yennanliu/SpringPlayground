package lab.easyPoll.domain;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.validation.constraints.Size;

import javax.validation.constraints.NotEmpty;
import org.springframework.format.annotation.NumberFormat;

@Entity
public class Poll {

	@Id
	@GeneratedValue
	@Column(name = "POLL_ID")
	@NumberFormat
	private Long id;

	@Column(name = "QUESTION")
	@NotEmpty
	private String question;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "POLL_ID")
	@OrderBy
	@Size(min = 2, max = 6)
	private Set<Option> options;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public Set<Option> getOptions() {
		return options;
	}

	public void setOptions(Set<Option> options) {
		this.options = options;
	}

	public void addOption(Option o) {
		if (options == null)
			this.options = new HashSet<>();
		this.options.add(o);
	}

	@Override
	public String toString() {
		return getId() + ", " + getQuestion() + ", " + getOptions();
	}
	
	public static Poll of(String question, String... options) {
		Poll p = new Poll();
		p.setQuestion(question);
		Arrays.stream(options).map(Option::of).forEach(p::addOption);
		return p;
	}
	
	public static Poll of(Long id, String question, String... options) {
		Poll p = new Poll();
		p.setId(id);
		p.setQuestion(question);
		Arrays.stream(options).map(Option::of).forEach(p::addOption);
		return p;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((options == null) ? 0 : options.hashCode());
		result = prime * result + ((question == null) ? 0 : question.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Poll other = (Poll) obj;
		if (options == null) {
			if (other.options != null)
				return false;
	//	} else if (!options.equals(other.options))
		} else if (!(options.containsAll(other.options) && other.options.containsAll(options)))
			return false;
		if (question == null) {
			if (other.question != null)
				return false;
		} else if (!question.equals(other.question))
			return false;
		return true;
	}
	
	

}
