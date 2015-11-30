package org.jsoup.renderer;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.Scanner;

import org.junit.Before;
import org.junit.Test;

public class HtmlToTextRendererTest
{
	HtmlToTextRenderer htmlToTextRenderer;

	@Before
	public void setUp()
	{
		htmlToTextRenderer = new HtmlToTextRenderer(new RendererConfig.RendererConfigBuilder().createRendererConfig());
	}

	@Test
	public void testHRTagRendering() throws FileNotFoundException
	{
		String html = "BEFORE HR <HR>IN HR</HR> AFTER HR";

		String text = "BEFORE HR \r\n"
		              + "------------------------------------------------------------------------\r\n"
		              + "IN HR AFTER HR";

		assert htmlToTextRenderer.render(html).equals(text);
	}

	@Test
	public void testListTagsRendering() throws FileNotFoundException
	{
		String html = "<ol>\n"
		              + "  <li>Coffee\n"
		              + "      <ul>\n"
		              + "        <li>Cappuccino</li>\n"
		              + "        <li>Americano</li>\n"
		              + "         <li>Caffe Latte</li>\n"
		              + "     </ul>\n"
		              + "  </li>\n"
		              + "  <li>Tea \n"
		              + "    <ol>\n"
		              + "      <li>Green Tea</li>\n"
		              + "      <li>Black Tea</li>  \n"
		              + "    </ol>\n"
		              + "  </li>\n"
		              + "  <li>Milk</li>\n"
		              + "</ol>\n"
		              + "\n"
		              + "\n"
		              + "<ul>\n"
		              + "  <li>Coke\n"
		              + "    <ul>\n"
		              + "      <li>Diet</li>\n"
		              + "      <li>Regular</li>  \n"
		              + "    </ul>\n"
		              + "  </li>\n"
		              + "  <li>Pepsi</li>\n"
		              + "  <li>Mountain Dew</li>\n"
		              + "</ul>";

		String text = "\r\n"
		              + "1. Coffee \r\n"
		              + "      o Cappuccino \r\n"
		              + "      o Americano \r\n"
		              + "      o Caffe Latte \r\n"
		              + "2. Tea \r\n"
		              + "      1. Green Tea \r\n"
		              + "      2. Black Tea \r\n"
		              + "3. Milk \r\n"
		              + "* Coke \r\n"
		              + "      o Diet \r\n"
		              + "      o Regular \r\n"
		              + "* Pepsi \r\n"
		              + "* Mountain Dew ";

		assert htmlToTextRenderer.render(html).equals(text);

		html = "<dl>\n"
		       + "   <dt>Kashmir</dt>\n"
		       + "   <dd>\n"
		       + "      <dl>\n"
		       + "         <dt>Azad Kashmir</dt>\n"
		       + "         <dd>Part of Pakistan</dd>\n"
		       + "         <dt>Jammu and Kashmir</dt>\n"
		       + "         <dd>Part of India</dd>\n"
		       + "      </dl>\n"
		       + "   </dd>\n"
		       + "   <dt>Aksai Chin</dt>\n"
		       + "   <dd>Part of China</dd>\n"
		       + "</dl>";

		text = "Kashmir\r\n"
		       + "Azad Kashmir\r\n"
		       + "      Part of Pakistan\r\n"
		       + "Jammu and Kashmir\r\n"
		       + "      Part of India\r\n"
		       + "\r\n"
		       + "Aksai Chin\r\n"
		       + "Part of China\r\n";

		assert htmlToTextRenderer.render(html).equals(text);
	}

	@Test
	public void testAddressTagRendering() throws FileNotFoundException
	{
		String html = "<a href=\"mailto:someone@example.com?Subject=Hello%20again\">Send mail!</a>";
		String textWithoutHyperlinkURLs = "Send mail!";
		assert htmlToTextRenderer.render(html).equals(textWithoutHyperlinkURLs);

		htmlToTextRenderer = new HtmlToTextRenderer(
				new RendererConfig.RendererConfigBuilder().setIncludeHyperlinkURLs(true).createRendererConfig());

		String textWithHyperlinkURLs = "Send mail! <mailto:someone@example.com?Subject=Hello%20again>";
		assert htmlToTextRenderer.render(html).equals(textWithHyperlinkURLs);
	}

	@Test
	public void testAltAttribRendering() throws FileNotFoundException
	{
		String html = "<img src=\"smiley.gif\" alt=\"Smiley face\">";
		String textWithoutAlternateText = "";
		assert htmlToTextRenderer.render(html).equals(textWithoutAlternateText);

		htmlToTextRenderer = new HtmlToTextRenderer(
				new RendererConfig.RendererConfigBuilder().setIncludeAlternateText(true).createRendererConfig());

		String textWithAlternateText = "[Smiley face]";
		assert htmlToTextRenderer.render(html).equals(textWithAlternateText);
	}

	@Test
	public void testTableRendering() throws FileNotFoundException
	{
		String html = "<table>\n"
		              + "  <tr>\n"
		              + "    <th>Month</th>\n"
		              + "    <th>Savings</th>\n"
		              + "  </tr>\n"
		              + "  <tr>\n"
		              + "    <td>January</td>\n"
		              + "    <td>$100</td>\n"
		              + "  </tr>\n"
		              + "  <tr>\n"
		              + "    <td>February</td>\n"
		              + "    <td>$80</td>\n"
		              + "  </tr>\n"
		              + "</table>\n";
		String text = "\r\n"
		              + "Month\t Savings\t \r\n"
		              + "January\t $100\t \r\n"
		              + "February\t $80\t ";
		assert htmlToTextRenderer.render(html).equals(text);
	}

	@Test
	public void testFullMimeHtmlToTextRendering() throws FileNotFoundException
	{
		// test conversion of a full mime html part email body
		File in = getFile("/htmltests/mime-html-alternative.txt");
		File out = getFile("/htmltests/mime-plaintext-alternative.txt");

		// Can't use APIs from java versions > 1.5 since Jsoup is limited to 1.5 APIs. Using old school methods.
		Scanner scan = new Scanner(in);
		String html = scan.useDelimiter("\\Z").next();
		scan.close();

		scan = new Scanner(out);
		String text = scan.useDelimiter("\\Z").next();
		scan.close();
		assert htmlToTextRenderer.render(html).replaceAll("\r\n", "\n").equals(text);

	}

	public static File getFile(String resourceName)
	{
		try
		{
			return new File(HtmlToTextRendererTest.class.getResource(resourceName).toURI());
		}
		catch (URISyntaxException e)
		{
			throw new IllegalStateException(e);
		}
	}
}