package org.jsoup.renderer;

import java.util.Arrays;
import java.util.List;

/**
 * Configurations used for {@link HtmlToTextRenderer} class.
 */
public class RendererConfig
{
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


	private RendererConfig(final int blockIndentSize, final int maxLineLength, final int hrLineLength, final int listIndentSize, final String newLine,
	                       final boolean includeHyperlinkURLs, final boolean includeAlternateText,
	                       final boolean convertNonBreakingSpaces, final List<Character> listBullets, final String tableCellSeparator)
	{
		this.blockIndentSize = blockIndentSize;
		this.maxLineLength = maxLineLength;
		this.hrLineLength = hrLineLength;
		this.listIndentSize = listIndentSize;
		this.newLine = newLine;
		this.includeHyperlinkURLs = includeHyperlinkURLs;
		this.includeAlternateText = includeAlternateText;
		this.convertNonBreakingSpaces = convertNonBreakingSpaces;
		this.listBullets = listBullets;
		this.tableCellSeparator = tableCellSeparator;
	}

	public int getBlockIndentSize()
	{
		return blockIndentSize;
	}

	public int getMaxLineLength()
	{
		return maxLineLength;
	}

	public int getHrLineLength()
	{
		return hrLineLength;
	}

	public int getListIndentSize()
	{
		return listIndentSize;
	}

	public String getNewLine()
	{
		return newLine;
	}

	public boolean isIncludeHyperlinkURLs()
	{
		return includeHyperlinkURLs;
	}

	public boolean isIncludeAlternateText()
	{
		return includeAlternateText;
	}

	public boolean isConvertNonBreakingSpaces()
	{
		return convertNonBreakingSpaces;
	}

	public List<Character> getListBullets()
	{
		return listBullets;
	}

	public String getTableCellSeparator()
	{
		return tableCellSeparator;
	}

	public static class RendererConfigBuilder
	{
		private int blockIndentSize = 4;
		// Default value is the maximum line length for sending email data specified in RFC2049
		private int maxLineLength = 76;
		private int hrLineLength = 72;
		private int listIndentSize = 6;
		// Default value is as specified in RFC1521
		private String newLine = "\r\n";
		private boolean includeHyperlinkURLs = false;
		private boolean includeAlternateText = false;
		private boolean convertNonBreakingSpaces = true;
		private List<Character> listBullets = Arrays.asList('*', 'o', '+', '#');
		private String tableCellSeparator = "\t";

		public RendererConfigBuilder setBlockIndentSize(final int blockIndentSize)
		{
			this.blockIndentSize = blockIndentSize;
			return this;
		}

		public RendererConfigBuilder setMaxLineLength(final int maxLineLength)
		{
			this.maxLineLength = maxLineLength;
			if (maxLineLength > 0)
			{
				hrLineLength = Math.max(2, maxLineLength - 4);
			}
			return this;
		}

		public RendererConfigBuilder setHrLineLength(final int hrLineLength)
		{
			this.hrLineLength = hrLineLength;
			return this;
		}

		public RendererConfigBuilder setListIndentSize(final int listIndentSize)
		{
			this.listIndentSize = listIndentSize;
			return this;
		}

		public RendererConfigBuilder setNewLine(final String newLine)
		{
			this.newLine = newLine;
			return this;
		}

		public RendererConfigBuilder setIncludeHyperlinkURLs(final boolean includeHyperlinkURLs)
		{
			this.includeHyperlinkURLs = includeHyperlinkURLs;
			return this;
		}

		public RendererConfigBuilder setIncludeAlternateText(final boolean includeAlternateText)
		{
			this.includeAlternateText = includeAlternateText;
			return this;
		}

		public RendererConfigBuilder setConvertNonBreakingSpaces(final boolean convertNonBreakingSpaces)
		{
			this.convertNonBreakingSpaces = convertNonBreakingSpaces;
			return this;
		}

		public RendererConfigBuilder setListBullets(final List<Character> listBullets)
		{
			this.listBullets = listBullets;
			return this;
		}

		public RendererConfigBuilder setTableCellSeparator(final String tableCellSeparator)
		{
			this.tableCellSeparator = tableCellSeparator;
			return this;
		}

		public RendererConfig createRendererConfig()
		{
			return new RendererConfig(blockIndentSize, maxLineLength, hrLineLength, listIndentSize,
			                          newLine, includeHyperlinkURLs, includeAlternateText,
			                          convertNonBreakingSpaces, listBullets, tableCellSeparator);
		}
	}
}
