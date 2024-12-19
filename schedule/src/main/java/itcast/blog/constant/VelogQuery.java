package itcast.blog.constant;

public class VelogQuery {
    public static final String VELOG_QUERY = """
        query trendingPosts($input: TrendingPostsInput!) {
            trendingPosts(input: $input) {
                title
                user {
                    username
                }
                url_slug
            }
        }
        """;

    public static final String VELOG_VARIABLES = """
        {
            "input": {
                "limit": 20,
                "offset": 40,
                "timeframe": "day"
            }
        }
        """;
}
