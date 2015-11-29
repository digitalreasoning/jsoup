package org.jsoup.renderer;

import java.util.List;

import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.NodeVisitor;

import static org.jsoup.renderer.RendererConstants.HtmlTags;

/**
 * The formatting rules, implemented in a breadth-first DOM traverse. Fills in the accumulator that contains text extracted from html string
 */

public class FormattingVisitor implements NodeVisitor
{
	private StringBuilder accum = new StringBuilder();
	private int width = 0;
	private final int blockIndentSize;
	private final int maxLineLength;
	private final int hrLineLength;
	private final int listIndentSize;
	private final String newLine;
	private final boolean includeHyperlinkURLs;
	private final boolean includeAlternateText;
	private final boolean convertNonBreakingSpaces;
	private final List<Character> listBullets;
	private final String tableCellSeparator;

	public FormattingVisitor(final RendererConfig config)
	{
		this.blockIndentSize = config.getBlockIndentSize();
		this.maxLineLength = config.getMaxLineLength();
		this.hrLineLength = config.getHrLineLength();
		this.listIndentSize = config.getListIndentSize();
		this.newLine = config.getNewLine();
		this.includeHyperlinkURLs = config.isIncludeHyperlinkURLs();
		this.includeAlternateText = config.isIncludeAlternateText();
		this.convertNonBreakingSpaces = config.isConvertNonBreakingSpaces();
		this.listBullets = config.getListBullets();
		this.tableCellSeparator = config.getTableCellSeparator();
	}

	public void head(final Node node, final int depth)
	{
		final String name = node.nodeName();
		if (node instanceof TextNode)
		{
			append(((TextNode) node).text());
		}
		else if (name.equals(HtmlTags.LI))
		{
			append(newLine + " * ");
		}
		else if (name.equals(HtmlTags.DT))
		{
			append("  ");
		}
		else if (StringUtil.in(name, HtmlTags.P, HtmlTags.H1, HtmlTags.H2, HtmlTags.H3, HtmlTags.H4, HtmlTags.H5, HtmlTags.TR))
		{
			append(newLine);
		}
	}

	public void tail(final Node node, final int depth)
	{
		final String name = node.nodeName();
		if (StringUtil.in(name, HtmlTags.BR, HtmlTags.DD, HtmlTags.DT, HtmlTags.P, HtmlTags.H1, HtmlTags.H2, HtmlTags.H3, HtmlTags.H4, HtmlTags.H5))
		{
			append(newLine);
		}
		else if (name.equals(HtmlTags.A))
		{
			append(String.format(" <%s>", node.absUrl("href")));
		}
	}

	private void append(String text)
	{
		if (text.startsWith(newLine))
		{
			width = 0;
		}
		if (text.equals(" ") && (accum.length() == 0 || StringUtil.in(accum.substring(accum.length() - 1), " ", "\n")))
		{
			return;
		}

		if (text.length() + width > maxLineLength)
		{
			String words[] = text.split("\\s+");
			for (int i = 0; i < words.length; i++)
			{
				String word = words[i];
				boolean last = i == words.length - 1;
				if (!last)
				{
					word = word + " ";
				}
				if (word.length() + width > maxLineLength)
				{
					accum.append("\n").append(word);
					width = word.length();
				}
				else
				{
					accum.append(word);
					width += word.length();
				}
			}
		}
		else
		{
			accum.append(text);
			width += text.length();
		}
	}

	@Override
	public String toString()
	{
		return accum.toString();
	}
}
