

import java.util.List;

public class TestClass {
	
	public static void main(String[] args) throws Exception {
		HackerNewsAPI hackerNewsApi = new HackerNewsAPI();
		List<HackerNewsStory> stories = hackerNewsApi.getTopStories();
		for (int storiesIndex = 0; storiesIndex < stories.size(); storiesIndex++) {
			stories.get(storiesIndex).printDetails();
		}
		System.out.println("\n\n");
		stories = hackerNewsApi.getNewestStories();
		for (int storiesIndex = 0; storiesIndex < stories.size(); storiesIndex++) {
			stories.get(storiesIndex).printDetails();
		}
		System.out.println("\n\n");
		stories = hackerNewsApi.getBestStories();
		for (int storiesIndex = 0; storiesIndex < stories.size(); storiesIndex++) {
			stories.get(storiesIndex).printDetails();
		}
	}
	
}
