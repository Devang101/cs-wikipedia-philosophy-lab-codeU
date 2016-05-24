package com.flatironschool.javacs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ArrayDeque;
import java.util.Deque;

import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import org.jsoup.select.Elements;

public class WikiPhilosophy {

	final static WikiFetcher wf = new WikiFetcher();
  final static Deque<String> parenTracker  = new ArrayDeque<String>();

	/**
	 * Tests a conjecture about Wikipedia and Philosophy.
	 *
	 * https://en.wikipedia.org/wiki/Wikipedia:Getting_to_Philosophy
	 *
	 * 1. Clicking on the first non-parenthesized, non-italicized link
     * 2. Ignoring external links, links to the current page, or red links
     * 3. Stopping when reaching "Philosophy", a page with no links or a page
     *    that does not exist, or when a loop occurs
	 *
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {

        // some example code to get you started

		String url = "https://en.wikipedia.org/wiki/Java_(programming_language)";
		while(!url.equals("https://en.wikipedia.org/wiki/Philosophy"))
		{
			if (url == null)
			{
				System.out.println("No valid links");
			}
			url = firstURL(url);
		}
		System.out.println("you did it!");


        // the following throws an exception so the test fails
        // until you update the code
        // String msg = "Complete this lab by adding your code and removing this statement.";
        // throw new UnsupportedOperationException(msg);
	}

	public static String firstURL(String url) throws IOException
	{
		Elements paragraphs = wf.fetchWikipedia(url);

		Element firstPara = paragraphs.get(0);

		Iterable<Node> iter = new WikiNodeIterable(firstPara);
		for (Node node: iter)
		{
			//add and pop ( ) to stack to check if link is in paren
      if(node instanceof TextNode)
      {
        String word = node.toString();
        if(word.contains("("))
        {
						parenTracker.push("(");
        }
				else if(word.contains(")"))
				{
						parenTracker.pop();
				}
      }

			if (node instanceof Element)
			{
				Element el = (Element) node;
				if (isValidLink(el))
				{
					String newUrl = el.attr("abs:href");
					return newUrl;
				}
			}
		}
		return null;
	}

	public static boolean isValidLink(Element el)
	{
		if (!el.tagName().equals("a")) {
			return false;
		}
		// in italics
		Element current = el;
		while(current.parent() !=null)
		{
			if (current.tagName().equals("i") || current.tagName().equals("em"))
			{
				return false;
			}
			current = current.parent();
		}
		// in parenthesis
		if (!parenTracker.isEmpty())
		{
			return false;
		}
		return true;
	}



}
