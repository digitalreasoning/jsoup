package org.jsoup.renderer;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.NodeTraversor;

/**
 * Performs a simple text rendering of HTML markup, similar to the way  email clients provide an automatic conversion of
 * HTML content to text in their alternative MIME encoding of emails.
 */
public class HtmlToTextRenderer
{
	private final RendererConfig config;

	public HtmlToTextRenderer(final RendererConfig config)
	{
		this.config = config;
	}

	public String render(final String html)
	{
		final Document document = Jsoup.parse(html);
		return getPlainText(document);
	}

	private String getPlainText(Element element)
	{
		FormattingVisitor formatter = new FormattingVisitor(config);
		NodeTraversor traversor = new NodeTraversor(formatter);
		traversor.traverse(element);
		return formatter.toString();
	}
}
