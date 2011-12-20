

public class HackerNewsUser {

	private int karma = -10000;
	private String name = "";
	private String userPageUrl = "";
	private String threadsPageUrl = "";

	public HackerNewsUser() {
	}

	public HackerNewsUser(String name) throws Exception {
		this.name = name;
		this.userPageUrl = "http://hackerstreet.in/user?id=" + this.name;
		this.threadsPageUrl = "http://hackerstreet.in/threads?id=" + this.name;
		refreshKarma();
	}

	public HackerNewsUser(int karma, String name, String userPageUrl, String threadsPageUrl) {
		this.karma = karma;
		this.name = name;
		this.userPageUrl = userPageUrl;
		this.threadsPageUrl = threadsPageUrl;
	}

	public void setKarma(int karma) {
		this.karma = karma;
	}

	public int getKarma() {
		return karma;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setUserPageUrl(String userPageUrl) {
		this.userPageUrl = userPageUrl;
	}

	public String getUserPageUrl() {
		return userPageUrl;
	}

	public void setThreadsPageUrl(String threadsPageUrl) {
		this.threadsPageUrl = threadsPageUrl;
	}

	public String getThreadsPageUrl() {
		return threadsPageUrl;
	}

	public void refreshKarma() throws Exception {
		HackerNewsAPI hackerNewsApi = new HackerNewsAPI();
		String htmlSource = hackerNewsApi.getSource(userPageUrl);
		int karmaStart = htmlSource.indexOf("<td align=top>karma:</td><td>") + 30;
		int karmaEnd = htmlSource.indexOf("</td>", karmaStart);
		String karma = htmlSource.substring(karmaStart, karmaEnd);
		if (karma != null && !karma.trim().equals("")) {
			this.karma = Integer.parseInt(karma);
		} else {
			throw new HNException();
		}
	}

}
