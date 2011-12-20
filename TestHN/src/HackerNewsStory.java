

public class HackerNewsStory {

	private int id = 0;
	private int number = -1;
	private String title;
	private String domain;
	private String url;
	private int score = -1;
	private String submitter;
	private int commentCount = -1;
	private String commentsUrl;

	public HackerNewsStory() {
	}

	public HackerNewsStory(int id, int number, String title, String domain, String url, int score, String submitter, int commentCount,
			String commentsUrl) {
		this.id = id;
		this.number = number;
		this.title = title;
		this.domain = domain;
		this.url = url;
		this.score = score;
		this.submitter = submitter;
		this.commentCount = commentCount;
		this.commentsUrl = commentsUrl;
	}

	public void printDetails() {
		String printingDetails = number + " : " + title + "\n";
		printingDetails += "URL: " + url + "\n";
		printingDetails += "Domain: " + domain + "\n";
		printingDetails += "Score: " + score + "\n";
		printingDetails += "Submitted By: " + submitter + "\n";
		printingDetails += "Number of comments: " + commentCount + "\n";
		printingDetails += "Discuss URL: " + commentsUrl + "\n";
		printingDetails += "HN Id: " + id + "\n\n";
		System.out.println(printingDetails);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public String getSubmitter() {
		return submitter;
	}

	public void setSubmitter(String submitter) {
		this.submitter = submitter;
	}

	public int getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(int commentCount) {
		this.commentCount = commentCount;
	}

	public String getCommentsUrl() {
		return commentsUrl;
	}

	public void setCommentsUrl(String commentsUrl) {
		this.commentsUrl = commentsUrl;
	}

}
