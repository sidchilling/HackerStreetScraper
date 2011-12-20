
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * This class is for slicing and dicing the HTML and turning it into
 * <code>HackerNewsStory</code> objects.
 * 
 * @author siddharthsaha
 */
public class HackerNewsAPI {

	private int numberOfStoriesOnFrontPage = 0;

	protected String getSource(String url) throws HNException {
		try {
			URL hackerStreetUrl = new URL(url);
			BufferedReader urlReader = new BufferedReader(new InputStreamReader(hackerStreetUrl.openStream()));
			String returnSource = "";
			String inputLineFromSource;
			while ((inputLineFromSource = urlReader.readLine()) != null) {
				returnSource += inputLineFromSource;
			}
			urlReader.close();
			return returnSource;
		} catch (MalformedURLException malformedUrlException) {
			throw new HNException();
		} catch (IOException ioException) {
			throw new HNException();
		}
	}

	private int getStoryNumber(String htmlSource) {
		int numberStart = htmlSource.indexOf(">") + 1;
		int numberEnd = htmlSource.indexOf(".");
		return Integer.parseInt(htmlSource.substring(numberStart, numberEnd));
	}

	private String getStoryURL(String htmlSource) {
		int urlStart = htmlSource.indexOf("href=\"") + 6;
		int urlEnd = htmlSource.indexOf("\">", urlStart);
		String url = htmlSource.substring(urlStart, urlEnd);
		if (url.substring(0, 4).equals("item")) {
			url = "http://hackerstreet.in/" + url;
		}
		url = url.replace("&amp;", "&");
		if (url.substring((url.length() - 13)).equals("rel=\"nofollow")) {
			url = url.substring(0, (url.length() - 13));
		}
		if (url.substring((url.length() - 2)).equals("\"")) {
			url = url.substring(0, (url.length() - 2));
		}
		return url;
	}

	private String getStoryDomain(String htmlSource) {
		int domainStart = htmlSource.indexOf("comhead\">");
		if (domainStart != -1) {
			domainStart = domainStart + 10;
			int domainEnd = htmlSource.indexOf("</span>");
			String domain = htmlSource.substring(domainStart, domainEnd);
			String returnStoryDomain = "";
			if (domain.charAt(0) == '=') {
				returnStoryDomain = "http://hackerstreet.in";
				return returnStoryDomain;
			}
			returnStoryDomain = "http://" + domain.substring(1, domain.length() - 2);
			return returnStoryDomain;
		} else {
			return "";
		}
	}

	private String getStoryTitle(String htmlSource) {
		int titleStart = htmlSource.indexOf(">") + 1;
		int titleEnd = htmlSource.indexOf("</a>", titleStart);
		String title = htmlSource.substring(titleStart, titleEnd);
		title = title.trim();
		return title;
	}

	private int getStoryScore(String htmlSource) {
		int scoreStart = htmlSource.indexOf(">") + 1;
		int scoreEnd = htmlSource.indexOf("</span>", scoreStart);
		String scoreString = htmlSource.substring(scoreStart, scoreEnd);
		return Integer.parseInt(scoreString.substring(0, scoreString.indexOf("point")).trim());
	}

	private String getSubmitter(String htmlSource) {
		int submitterStart = htmlSource.indexOf("user?id=");
		int realSubmitterStart = htmlSource.indexOf("=", submitterStart) + 1;
		int submitterEnd = htmlSource.indexOf("\"", realSubmitterStart);
		return htmlSource.substring(realSubmitterStart, submitterEnd);
	}

	private int getCommentCount(String htmlSource) {
		int commentStart = htmlSource.indexOf("item?id=");
		int commentCountStart = htmlSource.indexOf(">", commentStart) + 1;
		int commentEnd = htmlSource.indexOf("</a>", commentStart);
		String commentCountString = htmlSource.substring(commentCountStart, commentEnd);
		if (commentCountString.equals("discuss")) {
			return 0;
		} else if (commentCountString.equals("")) {
			return 0;
		} else {
			commentCountString = commentCountString.split(" ")[0];
			return Integer.parseInt(commentCountString);
		}
	}

	private int getHNId(String htmlSource) {
		int urlStart = htmlSource.indexOf("score_") + 6;
		int urlEnd = htmlSource.indexOf("\"", urlStart);
		return Integer.parseInt(htmlSource.substring(urlStart, urlEnd));
	}

	private String getCommentsUrl(String htmlSource) {
		return "http://hackerstreet.in/item?id=" + getHNId(htmlSource);
	}

	private List<HackerNewsStory> getStories(String htmlSource) {
		List<HackerNewsStory> newsStories = new ArrayList<HackerNewsStory>();
		Pattern pattern = Pattern.compile("span id=score");
		Matcher matcher = pattern.matcher(htmlSource);
		numberOfStoriesOnFrontPage = 0;
		while (matcher.find()) {
			numberOfStoriesOnFrontPage++;
		}
		for (int storyIndex = 0; storyIndex < numberOfStoriesOnFrontPage; storyIndex++) {
			HackerNewsStory newStory = new HackerNewsStory();
			newsStories.add(newStory);
		}
		Document htmlDocument = Jsoup.parse(htmlSource);
		Elements storyDetailsElements = htmlDocument.select("td.title");
		Elements storyOtherDetailsElements = htmlDocument.select("td.subtext");
		List<Integer> storyNumbers = new ArrayList<Integer>();
		for (int storyNumber = 0; storyNumber < storyDetailsElements.size() - 1; storyNumber = storyNumber + 2) {
			String storySourceHtml = storyDetailsElements.get(storyNumber).html();
			storyNumbers.add(getStoryNumber(storySourceHtml));
		}
		List<String> storyUrls = new ArrayList<String>();
		List<String> storyDomains = new ArrayList<String>();
		List<String> storyTitles = new ArrayList<String>();
		List<Integer> storyScores = new ArrayList<Integer>();
		List<String> storySubmitters = new ArrayList<String>();
		List<Integer> storyCommentCounts = new ArrayList<Integer>();
		List<String> storyCommentUrls = new ArrayList<String>();
		List<Integer> storyIds = new ArrayList<Integer>();
		for (int storyDetailsElementsIndex = 1; storyDetailsElementsIndex < storyDetailsElements.size() - 1; storyDetailsElementsIndex = storyDetailsElementsIndex + 2) {
			String storyDetailSourceHtml = storyDetailsElements.get(storyDetailsElementsIndex).html();
			storyUrls.add(getStoryURL(storyDetailSourceHtml));
			storyDomains.add(getStoryDomain(storyDetailSourceHtml));
			storyTitles.add(getStoryTitle(storyDetailSourceHtml));
		}
		for (int storyOtherDetailsElementsIndex = 0; storyOtherDetailsElementsIndex < storyOtherDetailsElements.size(); storyOtherDetailsElementsIndex++) {
			String storyOtherDetailsHtml = storyOtherDetailsElements.get(storyOtherDetailsElementsIndex).html();
			storyScores.add(getStoryScore(storyOtherDetailsHtml));
			storySubmitters.add(getSubmitter(storyOtherDetailsHtml));
			storyCommentCounts.add(getCommentCount(storyOtherDetailsHtml));
			storyCommentUrls.add(getCommentsUrl(storyOtherDetailsHtml));
			storyIds.add(getHNId(storyOtherDetailsHtml));
		}
		for (int storyIndex = 0; storyIndex < numberOfStoriesOnFrontPage; storyIndex++) {
			newsStories.get(storyIndex).setNumber(storyNumbers.get(storyIndex));
			newsStories.get(storyIndex).setUrl(storyUrls.get(storyIndex));
			newsStories.get(storyIndex).setDomain(storyDomains.get(storyIndex));
			newsStories.get(storyIndex).setTitle(storyTitles.get(storyIndex));
			newsStories.get(storyIndex).setScore(storyScores.get(storyIndex));
			newsStories.get(storyIndex).setSubmitter(storySubmitters.get(storyIndex));
			newsStories.get(storyIndex).setCommentCount(storyCommentCounts.get(storyIndex));
			newsStories.get(storyIndex).setCommentsUrl(storyCommentUrls.get(storyIndex));
			newsStories.get(storyIndex).setId(storyIds.get(storyIndex));
		}
		return newsStories;
	}

	public List<HackerNewsStory> getTopStories() throws Exception {
		return getStories(getSource("http://hackerstreet.in"));
	}

	public List<HackerNewsStory> getNewestStories() throws Exception {
		return getStories(getSource("http://hackerstreet.in/newest"));
	}

	public List<HackerNewsStory> getBestStories() throws Exception {
		return getStories(getSource("http://hackerstreet.in/best"));
	}

}
